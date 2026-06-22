package com.chemiofitor.protection_engineering.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 火箭射弹 — 直线飞行，5 秒寿命，命中/到期后安全爆炸。
 */
public class RocketProjectile extends Projectile {

    private static final int MAX_LIFE = 100;
    private static final float EXPLOSION_DAMAGE = 6f;
    private static final float EXPLOSION_RADIUS = 2f;
    private static final EntityDataAccessor<ItemStack> DATA_RENDER_ITEM =
            SynchedEntityData.defineId(RocketProjectile.class, EntityDataSerializers.ITEM_STACK);

    private int life;

    public RocketProjectile(EntityType<? extends RocketProjectile> type, Level level) {
        super(type, level);
    }

    public void setRenderItem(ItemStack stack) {
        this.entityData.set(DATA_RENDER_ITEM, stack.copy());
    }

    public ItemStack getRenderItem() {
        return this.entityData.get(DATA_RENDER_ITEM);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RENDER_ITEM, new ItemStack(Items.FIREWORK_ROCKET));
    }

    @Override
    public void tick() {
        // 保存上帧朝向（不调 super.tick()，需手动维护）
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        Vec3 velocity = this.getDeltaMovement();

        if (!this.level().isClientSide()) {
            life++;
            if (life >= MAX_LIFE) {
                explode();
                return;
            }

            // 碰撞检测
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
                return;
            }
        }

        // 移动
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        // 显式设置朝向
        double hDist = velocity.horizontalDistance();
        this.setYRot((float) Math.toDegrees(Math.atan2(-velocity.x, velocity.z)));
        this.setXRot((float) Math.toDegrees(Math.atan2(-velocity.y, hDist)));
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        explode();
    }

    private void explode() {
        if (this.isRemoved()) return;

        Level level = this.level();
        Vec3 pos = this.position();

        level.explode(this, pos.x(), pos.y(), pos.z(), 0, Level.ExplosionInteraction.NONE);

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

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.life = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Life", life);
    }

    @Override
    public boolean isAttackable() { return false; }

    @Override
    protected double getDefaultGravity() { return 0; }
}
