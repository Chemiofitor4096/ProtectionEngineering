package com.chemiofitor.protection_engineering.entity;

import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.item.ApsItem;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * 制导导弹 —— 发射后上升爬升，然后追踪锁定的目标。
 * <p>
 * 10 秒寿命，命中或到期后大爆炸（不破坏方块）。
 * 可被主动防御系统 (APS) 拦截。
 */
public class MissileEntity extends Projectile {

    private static final int LAUNCH_PHASE = 15; // 爬升阶段 ticks (0.75s)
    private static final int RAMP_TICKS = 20; // 转向渐入时长 (1.0s)
    private static final double TURN_RATE = 0.45; // 远距大幅修正
    private static final double TURN_RATE_CLOSE = 0.12; // 近处稳定追踪
    private static final double CLOSE_DIST = 12.0; // 近距离阈值
    private static final float EXPLOSION_DAMAGE = 40f;
    private static final float EXPLOSION_RADIUS = 8f;
    private static final int ENGINE_SOUND_INTERVAL = 10;
    private static final int WARNING_SOUND_INTERVAL = 40;

    private int life;
    @Nullable private UUID targetEntityUUID;
    @Nullable private Vec3 targetPos;
    @Nullable private UUID shooterUUID;
    private int engineSoundTimer;
    private int warningSoundTimer;

    public MissileEntity(EntityType<? extends MissileEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public void setTarget(@Nullable Entity entity, @Nullable BlockPos blockPos) {
        if (entity != null) {
            this.targetEntityUUID = entity.getUUID();
            this.targetPos = entity.position(); // 瞄准脚底，避免从上方飞过
        } else if (blockPos != null) {
            this.targetEntityUUID = null;
            this.targetPos = Vec3.atCenterOf(blockPos);
        }
    }

    public void setShooterEntity(@Nullable LivingEntity shooter) {
        this.setOwner(shooter);
        if (shooter != null) {
            this.shooterUUID = shooter.getUUID();
        }
    }

    @Nullable
    public UUID getTargetEntityUUID() {
        return targetEntityUUID;
    }

    // ── Tick ──────────────────────────────────────────────────

    @Override
    public void tick() {
        // 保存上帧朝向（不调 super.tick()，需手动维护，供渲染器 lerp）
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        if (!this.level().isClientSide()) {
            life++;
            if (life >= PEServerConfig.MISSILE_MAX_LIFE.get()) {
                explode();
                return;
            }
            if (life > LAUNCH_PHASE) {
                steer();
            }

            // 碰撞检测
            HitResult hitResult = net.minecraft.world.entity.projectile.ProjectileUtil
                    .getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
                return;
            }

            playEngineSound();
            warnTargetIfPlayer();
        } else {
            // 客户端生成尾迹粒子，避免 ServerLevel.sendParticles 发包丢失
            spawnTrailParticles();
        }

        // 手动移动（用 steer 更新后的速度）
        Vec3 velocity = this.getDeltaMovement();
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        // 显式设置朝向（不依赖 updateRotation()，保证正确同步到客户端）
        double hDist = velocity.horizontalDistance();
        this.setYRot((float) Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));
        this.setXRot((float) Math.toDegrees(Math.atan2(-velocity.y, hDist)));
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        explode();
    }

    // ── 制导 ──────────────────────────────────────────────────

    /** 每 tick 将速度方向向目标方向插值，靠近时加速转向并减速 */
    private void steer() {
        Vec3 desiredDir = getDesiredDirection();
        if (desiredDir == null) return;

        Vec3 currentDir = this.getDeltaMovement().normalize();
        double dist = this.position().distanceTo(targetPos != null ? targetPos : this.position());

        // 距离因子：近处收窄，远处放宽
        double t = Math.clamp(dist / CLOSE_DIST, 0.0, 1.0);
        // 时间渐入：爬升结束后从 0→1 平滑过渡 (RAMP_TICKS)，避免生硬切入
        double timeRamp = Math.clamp((life - LAUNCH_PHASE) / (double) RAMP_TICKS, 0.0, 1.0);
        // 小→大→小：渐入 × 距离因子 (近处稳定)
        double turnRate = Mth.lerp(t, TURN_RATE_CLOSE, TURN_RATE) * timeRamp;
        float speed = (float) Mth.lerp(t,
                PEServerConfig.MISSILE_CLOSE_SPEED.get(), PEServerConfig.MISSILE_FLIGHT_SPEED.get());

        // 计算转向角度，限制最大转角
        double dot = currentDir.dot(desiredDir);
        double angle = Math.acos(Math.clamp(dot, -1.0, 1.0));
        double turn = Math.min(angle, turnRate);

        if (turn < 0.001) return;

        // 插值朝向
        Vec3 axis = currentDir.cross(desiredDir).normalize();
        if (axis.lengthSqr() < 0.001) {
            // 方向相同或相反 — 直接使用目标方向
            this.setDeltaMovement(desiredDir.scale(speed));
        } else {
            // 绕 axis 旋转 current 朝向 desired
            Vec3 newDir = currentDir.scale(Math.cos(turn))
                    .add(axis.cross(currentDir).scale(Math.sin(turn)))
                    .add(axis.scale(axis.dot(currentDir) * (1 - Math.cos(turn))))
                    .normalize();
            this.setDeltaMovement(newDir.scale(speed));
        }
    }

    /** 获取目标方向。实体目标跨维度/死亡则退化为方块坐标或直线飞行。 */
    @Nullable
    private Vec3 getDesiredDirection() {
        Vec3 target = null;

        // 优先追踪实体
        if (targetEntityUUID != null) {
            Entity e = findTargetEntity();
            if (e != null) {
                target = e.position(); // 瞄准脚底
                // 更新 targetPos 作为后备
                this.targetPos = target;
            } else {
                // 实体丢失 → 使用之前记录的位置
                target = this.targetPos;
                this.targetEntityUUID = null; // 不再尝试查找
            }
        } else {
            target = this.targetPos;
        }

        if (target == null) return null;

        Vec3 from = this.position();
        Vec3 toTarget = target.subtract(from);
        if (toTarget.lengthSqr() < 0.01) return null;

        return toTarget.normalize();
    }

    @Nullable
    private Entity findTargetEntity() {
        if (targetEntityUUID == null) return null;
        Level level = this.level();
        if (level instanceof ServerLevel sl) {
            Entity e = sl.getEntity(targetEntityUUID);
            if (e == null || !e.isAlive()) return null;
            // 跨维度检查
            if (e.level() != level) return null;
            return e;
        }
        return null;
    }

    // ── 爆炸 ──────────────────────────────────────────────────

    private void explode() {
        if (this.isRemoved()) return;

        Level level = this.level();
        Vec3 pos = this.position();

        // 不破坏方块，爆炸威力 4 (TNT 级，视觉 ≈8 格伤害区)
        level.explode(this, pos.x(), pos.y(), pos.z(), 4.0f, Level.ExplosionInteraction.NONE);

        // 中心闪光 + 伤害半径边界粒子环
        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                    pos.x(), pos.y() + 1.0, pos.z(),
                    1, 0, 0, 0, 0);
            for (int i = 0; i < 12; i++) {
                double angle = 2.0 * Math.PI * i / 12;
                sl.sendParticles(ParticleTypes.EXPLOSION,
                        pos.x() + Math.cos(angle) * EXPLOSION_RADIUS,
                        pos.y() + 0.5,
                        pos.z() + Math.sin(angle) * EXPLOSION_RADIUS,
                        1, 0, 0, 0, 0.1);
            }
        }

        // 伤害范围内生物
        AABB aabb = AABB.ofSize(pos, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
            if (e == this.getOwner()) continue;
            float dist = (float) e.position().distanceTo(pos);
            if (dist <= EXPLOSION_RADIUS) {
                e.hurt(this.damageSources().explosion(this, this.getOwner()),
                        EXPLOSION_DAMAGE * (1f - dist / EXPLOSION_RADIUS));
            }
        }

        this.discard();
    }

    // ── 音效 ──────────────────────────────────────────────────

    private void playEngineSound() {
        engineSoundTimer++;
        if (engineSoundTimer >= ENGINE_SOUND_INTERVAL) {
            engineSoundTimer = 0;
            this.level().playSound(null, this, PESounds.MISSILE_FLIGHT.get(),
                    SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    /** 向被锁定的玩家循环播放警报音效。若目标开启了 APS 则不播放。 */
    private void warnTargetIfPlayer() {
        if (targetEntityUUID == null) return;

        // 检查目标玩家是否开启了 APS
        Entity target = findTargetEntity();
        if (!(target instanceof Player targetPlayer)) return;
        if (hasApsActive(targetPlayer)) return;

        warningSoundTimer++;
        if (warningSoundTimer >= WARNING_SOUND_INTERVAL) {
            warningSoundTimer = 0;
            targetPlayer.level().playSound(null, targetPlayer, PESounds.MISSILE_WARNING.get(),
                    SoundSource.HOSTILE, 0.7f, 1.0f);
        }
    }

    /** 检查玩家是否激活了主动防御系统 */
    private boolean hasApsActive(Player player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                ItemStack attached = entry.getValue();
                if (attached.getItem() instanceof ApsItem aps) {
                    if (aps.isInActiveWindow(attached)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ── 粒子 ──────────────────────────────────────────────────

    private void spawnTrailParticles() {
        var level = this.level();
        Vec3 pos = this.position();
        Vec3 back = this.getDeltaMovement().normalize();

        // 火焰核心
        Vec3 p0 = pos.add(back.scale(-0.3));
        level.addParticle(ParticleTypes.FLAME,
                p0.x, p0.y, p0.z, 0, 0, 0);

        // 烟雾 ×2
        Vec3 p1 = pos.add(back.scale(-0.6));
        for (int i = 0; i < 2; i++)
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    p1.x, p1.y, p1.z,
                    (level.random.nextDouble() - 0.5) * 0.4,
                    (level.random.nextDouble() - 0.5) * 0.4,
                    (level.random.nextDouble() - 0.5) * 0.4);
    }

    // ── 朝向 ──────────────────────────────────────────────────

    private void updateRotationFromVelocity() {
        Vec3 vel = this.getDeltaMovement();
        if (vel.lengthSqr() > 0.001) {
            double hDist = vel.horizontalDistance();
            this.setYRot((float) (Math.toDegrees(Math.atan2(-vel.x, vel.z))));
            this.setXRot((float) (Math.toDegrees(Math.atan2(-vel.y, hDist))));
        }
    }

    // ── 碰撞过滤 ──────────────────────────────────────────────

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target == this.getOwner()) return false;
        if (target instanceof MissileEntity) return false;
        if (target instanceof RocketProjectile) return false;
        return super.canHitEntity(target);
    }

    // ── 渲染 ──────────────────────────────────────────────────

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 65536;
    }

    // ── NBT ────────────────────────────────────────────────────

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.life = tag.getInt("Life");
        this.engineSoundTimer = tag.getInt("EngineSound");
        this.warningSoundTimer = tag.getInt("WarningSound");
        if (tag.hasUUID("TargetUUID")) {
            this.targetEntityUUID = tag.getUUID("TargetUUID");
        }
        if (tag.contains("TargetX")) {
            this.targetPos = new Vec3(
                    tag.getDouble("TargetX"),
                    tag.getDouble("TargetY"),
                    tag.getDouble("TargetZ"));
        }
        if (tag.hasUUID("ShooterUUID")) {
            this.shooterUUID = tag.getUUID("ShooterUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Life", life);
        tag.putInt("EngineSound", engineSoundTimer);
        tag.putInt("WarningSound", warningSoundTimer);
        if (targetEntityUUID != null) {
            tag.putUUID("TargetUUID", targetEntityUUID);
        }
        if (targetPos != null) {
            tag.putDouble("TargetX", targetPos.x);
            tag.putDouble("TargetY", targetPos.y);
            tag.putDouble("TargetZ", targetPos.z);
        }
        if (shooterUUID != null) {
            tag.putUUID("ShooterUUID", shooterUUID);
        }
    }

    @Override
    public boolean isAttackable() { return false; }

    @Override
    protected double getDefaultGravity() { return 0; } // 导弹不落下
}
