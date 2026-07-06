package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.entity.MissileEntity;
import com.chemiofitor.protection_engineering.registry.PEEntities;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 便携式导弹背包 —— 背部附件，消耗物品栏中的 {@link MissileItem} 发射制导导弹。
 */
public class MissilePackItem extends AttachmentItem {

    private static final int COOLDOWN_TICKS = 1200; // 60 秒

    public MissilePackItem(Properties properties) {
        super(properties, SlotTypes.BACK);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }

    @Override
    public long getCooldownDuration() { return COOLDOWN_TICKS; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.missile";
    }

    // ── 一次性激活：锁定目标 → 发射导弹 ──────────────────────

    @Override
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        Level level = player.level();
        if (level.isClientSide()) return;

        boolean isCreative = player.getAbilities().instabuild;

        // 先检查有没有导弹在物品栏
        if (!isCreative && !hasMissile(player)) {
            level.playSound(null, player, PESounds.LAUNCH_FAIL.get(),
                    SoundSource.PLAYERS, 0.8f, 1.0f);
            player.displayClientMessage(
                    Component.translatable("message.protectionengineering.missile_empty"), true);
            return;
        }

        // Raycast 锁定目标
        double targetRange = PEServerConfig.MISSILE_TARGET_RANGE.get();
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 reachVec = eyePos.add(lookVec.scale(targetRange));

        AABB entityAABB = player.getBoundingBox()
                .expandTowards(lookVec.scale(targetRange))
                .inflate(1.0);
        EntityHitResult entityHit = net.minecraft.world.entity.projectile.ProjectileUtil
                .getEntityHitResult(level, player, eyePos, reachVec, entityAABB,
                        e -> !e.isSpectator() && e.isPickable() && e != player);

        BlockHitResult blockHit = level.clip(new net.minecraft.world.level.ClipContext(
                eyePos, reachVec,
                net.minecraft.world.level.ClipContext.Block.OUTLINE,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                player));

        Entity targetEntity = null;
        BlockPos targetBlock = null;
        double entityDist = entityHit != null ? eyePos.distanceToSqr(entityHit.getLocation()) : Double.MAX_VALUE;
        double blockDist = blockHit != null ? eyePos.distanceToSqr(blockHit.getLocation()) : Double.MAX_VALUE;

        if (entityDist <= blockDist && entityHit != null) {
            targetEntity = entityHit.getEntity();
        } else if (blockHit != null && blockHit.getType() != HitResult.Type.MISS) {
            targetBlock = blockHit.getBlockPos();
        }

        // 无有效目标（实体和方块都未命中）→ 不发射，不消耗导弹
        if (targetEntity == null && targetBlock == null) {
            level.playSound(null, player, PESounds.LAUNCH_FAIL.get(),
                    SoundSource.PLAYERS, 0.8f, 1.0f);
            player.displayClientMessage(
                    Component.translatable("message.protectionengineering.missile_no_target"), true);
            return;
        }

        // 确认有目标后才消耗导弹
        if (!isCreative) {
            consumeMissile(player);
        }

        level.playSound(null, player, PESounds.MISSILE_LOCK.get(),
                SoundSource.PLAYERS, 0.8f, 1.0f);

        String targetName = getTargetName(targetEntity, targetBlock, level);
        player.displayClientMessage(
                Component.translatable("message.protectionengineering.missile_target", targetName), true);

        // 发射导弹
        MissileEntity missile = new MissileEntity(PEEntities.MISSILE.get(), level);
        missile.setShooterEntity(player);
        missile.setTarget(targetEntity, targetBlock);
        missile.setPos(player.getEyePosition().add(0, 0.5, 0));

        Vec3 look = player.getLookAngle();
        Vec3 launchDir = new Vec3(look.x * 0.3, 1.0, look.z * 0.3).normalize();
        missile.shoot(launchDir.x, launchDir.y, launchDir.z, 1.5f, 0);
        level.addFreshEntity(missile);

        level.playSound(null, player, PESounds.MISSILE_LAUNCH.get(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    // ── 消耗导弹 ──────────────────────────────────────────────

    private boolean hasMissile(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof MissileItem) return true;
        }
        return false;
    }

    private boolean consumeMissile(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof MissileItem) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }

    private String getTargetName(@Nullable Entity entity, @Nullable BlockPos blockPos, Level level) {
        if (entity != null) {
            if (entity instanceof Player p) return p.getGameProfile().getName();
            if (entity.hasCustomName()) return entity.getCustomName().getString();
            return entity.getDisplayName().getString();
        }
        if (blockPos != null) return level.getBlockState(blockPos).getBlock().getName().getString();
        return "???";
    }
}
