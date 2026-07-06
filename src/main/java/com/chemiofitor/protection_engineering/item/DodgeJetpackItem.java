package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * 应激反馈背包 —— 背部附件。
 * 受到近战攻击时免疫伤害并自动往受击方向的反方向弹射，
 * 冷却 10 秒。周围存在危险源时不生效。
 */
public class DodgeJetpackItem extends AttachmentItem {

    private static final int DANGER_SCAN_RADIUS = 5;

    public DodgeJetpackItem(Properties properties) {
        super(properties, SlotTypes.BACK);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }

    @Override
    public long getCooldownDuration() { return PEServerConfig.DODGE_COOLDOWN_TICKS.get(); }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.dodge_jetpack";
    }

    // ── 应激反馈逻辑 ──────────────────────────────────────────

    public static boolean shouldDodge(Player player, ItemStack backpack, @Nullable Entity attacker) {
        if (!player.getAbilities().instabuild) {
            DodgeJetpackItem item = (DodgeJetpackItem) backpack.getItem();
            if (item.getState(backpack) != STATE_READY) return false;
        }
        if (isNearDanger(player)) return false;
        if (!(attacker instanceof LivingEntity)) return false;
        if (attacker.distanceToSqr(player) > 9.0) return false;
        return true;
    }

    public static void executeDodge(Player player, @Nullable Entity attacker) {
        Level level = player.level();
        Vec3 dodgeDir = getDodgeDirection(player, attacker);
        applyMomentumDodge(player, dodgeDir);

        level.playSound(null, player, PESounds.DODGE_WARNING.get(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, player, PESounds.DODGE_JET.get(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private static @NotNull Vec3 getDodgeDirection(Player player, @Nullable Entity attacker) {
        if (attacker != null) {
            Vec3 dir = player.position().subtract(attacker.position());
            double hDist = dir.horizontalDistance();
            if (hDist > 0.01) {
                return new Vec3(dir.x / hDist, 0, dir.z / hDist).normalize();
            }
        }
        Vec3 look = player.getLookAngle();
        return new Vec3(-look.x, 0, -look.z).normalize();
    }

    private static void applyMomentumDodge(Player player, Vec3 direction) {
        double strength = PEServerConfig.DODGE_STRENGTH.get();
        Vec3 vel = player.getDeltaMovement();
        player.setDeltaMovement(
                direction.x * strength,
                vel.y,
                direction.z * strength
        );
        player.hurtMarked = true;
        player.fallDistance = 0;
    }

    // ── 危险源扫描 ────────────────────────────────────────────

    static boolean isNearDanger(Player player) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();

        int r = DANGER_SCAN_RADIUS;
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                for (int dy = -2; dy <= 2; dy++) {
                    BlockPos checkPos = playerPos.offset(dx, dy, dz);
                    double hDist = Math.sqrt(dx * dx + dz * dz);
                    BlockState state = level.getBlockState(checkPos);

                    if (hDist <= 3 && state.is(Blocks.LAVA)) return true;
                    if (hDist <= 2 && Math.abs(dy) <= 1
                            && (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE))) return true;
                    if (hDist <= 2 && Math.abs(dy) <= 1 && state.is(Blocks.CACTUS)) return true;
                    if (hDist <= 2 && state.is(Blocks.POINTED_DRIPSTONE)) return true;
                }
            }
        }

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                BlockPos ground = playerPos.offset(dx, -1, dz);
                int airCount = 0;
                for (int dy = 0; dy < 6; dy++) {
                    if (level.getBlockState(ground.below(dy)).isAir()) airCount++;
                    else break;
                }
                if (airCount >= 5) return true;
            }
        }
        return false;
    }
}
