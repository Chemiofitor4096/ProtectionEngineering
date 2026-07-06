package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.entity.RocketProjectile;
import com.chemiofitor.protection_engineering.registry.PEEntities;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 火箭发射器 —— 肩部附件，消耗烟花火箭以霰弹形式向前散射。
 * 至多 12 发，从双肩位置随机散布发射。
 */
public class RocketLauncherItem extends AttachmentItem {

    private static final int MAX_ROCKETS = 12;
    private static final float LAUNCH_SPEED = 2.5f;
    private static final float SPREAD_ANGLE = 20f;
    private static final float SHOULDER_OFFSET = 0.4f;

    public RocketLauncherItem(Properties properties) {
        super(properties, SlotTypes.SHOULDER);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }

    @Override
    public long getCooldownDuration() { return PEServerConfig.ROCKET_COOLDOWN_TICKS.get(); }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.rocket_launcher";
    }

    // ── 一次性激活：霰弹发射 ──────────────────────────────────

    @Override
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        Level level = player.level();
        if (level.isClientSide()) return;
        boolean isCreative = player.getAbilities().instabuild;

        int count = countFireworks(player);
        int toFire = Math.min(count, MAX_ROCKETS);
        if (toFire == 0) {
            level.playSound(null, player, PESounds.LAUNCH_FAIL.get(),
                    SoundSource.PLAYERS, 0.8f, 1.0f);
            return;
        }

        ItemStack renderTemplate = findOneFirework(player);
        if (!isCreative) consumeFireworks(player, toFire);

        Vec3 baseDir = player.getLookAngle();
        Vec3 eyePos = player.getEyePosition();
        Vec3 right = baseDir.cross(new Vec3(0, 1, 0)).normalize();
        RandomSource random = player.getRandom();

        for (int i = 0; i < toFire; i++) {
            float yaw = ((random.nextFloat() - 0.5f) * SPREAD_ANGLE);
            float pitch = ((random.nextFloat() - 0.5f) * SPREAD_ANGLE * 0.4f);
            Vec3 dir = baseDir.yRot((float) Math.toRadians(yaw))
                    .xRot((float) Math.toRadians(pitch));

            boolean fromRight = (i % 2 == 1);
            Vec3 shoulderOffset = right.scale(fromRight ? SHOULDER_OFFSET : -SHOULDER_OFFSET)
                    .add(baseDir.scale(0.5));
            Vec3 spawnPos = eyePos.add(shoulderOffset);

            RocketProjectile rocket = new RocketProjectile(PEEntities.ROCKET_PROJECTILE.get(), level);
            rocket.setOwner(player);
            rocket.setPos(spawnPos);
            rocket.shoot(dir.x, dir.y, dir.z, LAUNCH_SPEED, 0);
            rocket.setRenderItem(renderTemplate.copy());

            level.addFreshEntity(rocket);
        }

        level.playSound(null, player, PESounds.ROCKET_LAUNCH.get(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private int countFireworks(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == Items.FIREWORK_ROCKET) count += stack.getCount();
        }
        return count;
    }

    private void consumeFireworks(Player player, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == Items.FIREWORK_ROCKET) {
                int consume = Math.min(stack.getCount(), remaining);
                stack.shrink(consume);
                remaining -= consume;
            }
        }
    }

    private ItemStack findOneFirework(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == Items.FIREWORK_ROCKET) return stack.copy();
        }
        return ItemStack.EMPTY;
    }
}
