package com.chemiofitor.protection_engineering.entity;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 不可见推进实体 — 模拟烟花火箭挂载助推。
 * 挂载到玩家身上，每 tick 施加烟花公式推力，到时间后自毁。
 */
public class ThrustEntity extends Entity {

    /** 正在助推的玩家 UUID — 用于防止重复生成 */
    public static final Set<UUID> ACTIVE = new HashSet<>();

    @Nullable
    private LivingEntity target;
    private int life;
    private int maxLife;
    private double targetSpeed = 1.5;
    private boolean soulFire;

    public ThrustEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.noCulling = true;
    }

    public void init(LivingEntity target, double targetSpeed, boolean soulFire, int durationTicks) {
        this.target = target;
        this.targetSpeed = targetSpeed;
        this.soulFire = soulFire;
        this.maxLife = durationTicks;
        this.setPos(target.position());
        ACTIVE.add(target.getUUID());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) { return false; }

    @Override
    public boolean shouldRender(double x, double y, double z) { return false; }

    @Override
    public void remove(RemovalReason reason) {
        if (target != null) ACTIVE.remove(target.getUUID());
        super.remove(reason);
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null || !target.isAlive() || life >= maxLife) {
            ProtectionEngineering.LOGGER.debug("Thrust DISCARD life={}/{}", life, maxLife);
            this.discard();
            return;
        }

        // 跟随玩家
        this.setPos(target.position());

        // 烟花公式：向 lookDir * targetSpeed lerp 50%，速度自然收敛不无限涨
        if (target.isFallFlying()) {
            Vec3 look = target.getLookAngle();
            Vec3 vel = target.getDeltaMovement();
            target.setDeltaMovement(vel.add(
                    look.x * 0.1 + (look.x * targetSpeed - vel.x) * 0.5,
                    look.y * 0.1 + (look.y * targetSpeed - vel.y) * 0.5,
                    look.z * 0.1 + (look.z * targetSpeed - vel.z) * 0.5));

            // 粒子
            if (target.level() instanceof ServerLevel sl) {
                Vec3 pos = target.position();
                sl.sendParticles(
                        soulFire ? ParticleTypes.SOUL : ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        pos.x, pos.y, pos.z, 3, 0.3, 0.05, 0.3, 0.02);
            }
        } else {
            this.discard();
            return;
        }

        life++;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Life")) this.life = tag.getInt("Life");
        if (tag.contains("Speed")) this.targetSpeed = tag.getDouble("Speed");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Life", life);
        tag.putDouble("Speed", targetSpeed);
    }

    @Override
    public boolean isAttackable() { return false; }

    @Override
    public boolean isPickable() { return false; }
}
