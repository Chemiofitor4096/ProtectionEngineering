package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 主动防御系统 —— 肩部附件，手动开启后免疫投射物并产生爆炸反击。
 * 开启后持续拦截 10 秒，冷却 30 秒。
 */
public class ApsItem extends AttachmentItem {

    private static final float EXPLOSION_DAMAGE = 10f;
    private static final float EXPLOSION_RADIUS = 1f;

    public ApsItem(Properties properties) {
        super(properties, SlotTypes.SHOULDER);
    }

    ApsItem(Properties properties, boolean advanced) {
        super(properties, SlotTypes.SHOULDER);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ACTIVE_COOLDOWN; }

    @Override
    protected void onStateEnter(ItemStack stack, int newState, LivingEntity entity) {
        if (newState == STATE_ACTIVE) {
            entity.level().playSound(null, entity, PESounds.APS_ACTIVATE.get(),
                    SoundSource.PLAYERS, 0.8f, 1.0f);
        }
    }

    @Override
    public long getActiveDuration() {
        return this instanceof AdvancedApsItem
                ? (long) (PEServerConfig.APS_ACTIVE_DURATION.get() * 1.5)
                : PEServerConfig.APS_ACTIVE_DURATION.get();
    }

    @Override
    public long getCooldownDuration() { return PEServerConfig.APS_COOLDOWN_TICKS.get(); }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.aps";
    }

    // ── Tick：巡航拦截 ───────────────────────────────────────

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity,
                       com.chemiofitor.protection_engineering.api.SlotType slot) {
        super.onTick(attachment, host, entity, slot); // 基类状态机

        if (!(entity instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        // 激活窗口中才拦截
        if (!isInActiveWindow(attachment)) return;

        Level level = player.level();
        AABB aabb = player.getBoundingBox().inflate(PEServerConfig.APS_INTERCEPT_RANGE.get());
        List<Projectile> projectiles = level.getEntitiesOfClass(Projectile.class, aabb,
                p -> p.getOwner() != player);

        for (Projectile proj : projectiles) {
            Vec3 pos = proj.position();
            proj.discard();
            createSafeExplosion(level, player, pos, EXPLOSION_DAMAGE, EXPLOSION_RADIUS);
        }
    }

    // ── 阻止直接命中 ──────────────────────────────────────────

    public static boolean interceptDirectHit(Player player, ItemStack apsStack, Projectile projectile) {
        if (!(apsStack.getItem() instanceof ApsItem aps)) return false;
        boolean intercepting = player.getAbilities().instabuild
                ? aps.isActive(apsStack)
                : aps.isInActiveWindow(apsStack);
        if (!intercepting) return false;

        double range = PEServerConfig.APS_INTERCEPT_RANGE.get();
        Vec3 incoming = player.position().subtract(projectile.position());
        double hDist = incoming.horizontalDistance();
        Vec3 explodePos;
        if (hDist > 0.01) {
            explodePos = player.position().add(
                    new Vec3(incoming.x / hDist, 0, incoming.z / hDist).normalize().scale(range));
        } else {
            explodePos = player.position().add(player.getLookAngle().reverse().scale(range));
        }

        projectile.discard();
        createSafeExplosion(player.level(), player, explodePos, EXPLOSION_DAMAGE, EXPLOSION_RADIUS);
        return true;
    }

    // ── 爆炸（不伤自己、不破坏方块）────────────────────────

    private static void createSafeExplosion(Level level, Player player, Vec3 pos,
                                            float damage, float radius) {
        level.explode(player, pos.x(), pos.y(), pos.z(), radius, Level.ExplosionInteraction.NONE);
        AABB aabb = AABB.ofSize(pos, radius * 2, radius * 2, radius * 2);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
            if (e == player) continue;
            float dist = (float) e.position().distanceTo(pos);
            if (dist <= radius) {
                e.hurt(player.damageSources().explosion(player, player), damage * (1f - dist / radius));
            }
        }
    }
}
