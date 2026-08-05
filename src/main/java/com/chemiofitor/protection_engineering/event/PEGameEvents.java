package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.item.ApsItem;
import com.chemiofitor.protection_engineering.item.DodgeJetpackItem;
import com.chemiofitor.protection_engineering.item.InsulatedSolesItem;
import com.chemiofitor.protection_engineering.item.SilentSolesItem;
import com.chemiofitor.protection_engineering.registry.PEDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.chemiofitor.protection_engineering.api.IAttachment.STATE_COOLING;

@EventBusSubscriber(modid = ProtectionEngineering.MODID)
public class PEGameEvents {

    public static final Set<UUID> thrustRequests = new HashSet<>();

    // ── 伤害事件 ──────────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        var source = event.getSource();

        // ── 隔热鞋底：免疫脚下热源伤害 ──────────────────────
        if ((source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.CAMPFIRE))
                && AttachmentUtil.anyOnSlot(player, EquipmentSlot.FEET,
                        s -> s.getItem() instanceof InsulatedSolesItem)) {
            event.setCanceled(true);
            return;
        }

        var attacker = source.getEntity();
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);

        // ── 近战攻击 → 应激反馈背包 ──────────────────────
        if ((source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK))
                && !(attacker instanceof Projectile)) {
            if (chestplate.getItem() instanceof IAttachmentHost host) {
                ItemStack backSlot = host.getAttachments(chestplate).get(SlotTypes.BACK);
                if (backSlot.getItem() instanceof DodgeJetpackItem) {
                    if (DodgeJetpackItem.shouldDodge(player, backSlot, attacker)) {
                        DodgeJetpackItem.executeDodge(player, attacker);
                        if (!player.getAbilities().instabuild) {
                            long now = player.level().getGameTime();
                            backSlot.set(PEDataComponents.ATTACHMENT_STATE.get(), STATE_COOLING);
                            backSlot.set(PEDataComponents.ATTACHMENT_COOLDOWN.get(),
                                    now + PEServerConfig.DODGE_COOLDOWN_TICKS.get());
                        }
                        host.setAttachments(chestplate,
                                host.getAttachments(chestplate).with(SlotTypes.BACK, backSlot));
                        player.setItemSlot(EquipmentSlot.CHEST, chestplate);
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }

        // ── 投射物 → APS 拦截 ───────────────────────────
        if (attacker instanceof Projectile projectile) {
            if (chestplate.getItem() instanceof IAttachmentHost host) {
                for (var entry : host.getAttachments(chestplate).slots().entrySet()) {
                    ItemStack attached = entry.getValue();
                    if (attached.getItem() instanceof ApsItem) {
                        if (ApsItem.interceptDirectHit(player, attached, projectile)) {
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }

        // ── 通用伤害减免 ────────────────────────────────────
        float reduction = (float) AttachmentUtil.reduce(player, att -> {
            var types = att.getProtectedDamageTypes();
            var tags = att.getProtectedDamageTypeTags();
            // 两者皆空 = 全类型（向后兼容）；否则任一匹配即生效
            if (!types.isEmpty() || !tags.isEmpty()) {
                boolean match = types.stream().anyMatch(source::is)
                        || tags.stream().anyMatch(source::is);
                if (!match) return 0f;
            }
            return att.getDamageReduction();
        });
        if (reduction > 0) {
            reduction = Math.min(reduction, 1.0f);
            event.setAmount(event.getAmount() * (1f - reduction));
        }
    }

    // ── 摔落减免 ──────────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        float distReduction = (float) AttachmentUtil.reduce(player, IAttachment::getFallDistanceReduction);
        if (distReduction > 0) {
            event.setDistance(Math.max(0, event.getDistance() - distReduction));
        }

        float dmgReduction = Math.min((float) AttachmentUtil.reduce(player, IAttachment::getFallDamageReduction), 1.0f);
        if (dmgReduction > 0) {
            event.setDamageMultiplier(event.getDamageMultiplier() * (1f - dmgReduction));
        }
    }

    // ── 效果免疫 ──────────────────────────────────────────────

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (hasImmunity(player, event.getEffectInstance().getEffect())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (hasImmunity(player, event.getEffectInstance().getEffect())) {
            player.removeEffect(event.getEffectInstance().getEffect());
        }
    }

    private static boolean hasImmunity(Player player, Holder<MobEffect> effect) {
        return AttachmentUtil.any(player, stack ->
            stack.getItem() instanceof IAttachment att && att.getImmunities().contains(effect));
    }

    // ── 静音鞋底 ──────────────────────────────────────────────

    @SubscribeEvent
    public static void onVanillaGameEvent(VanillaGameEvent event) {
        if (!(event.getCause() instanceof Player player)) return;
        if (!isSilencedGameEvent(event.getVanillaEvent())) return;

        if (AttachmentUtil.get(player, EquipmentSlot.FEET, SlotTypes.FOOT).getItem() instanceof SilentSolesItem) {
            event.setCanceled(true);
        }
    }

    private static boolean isSilencedGameEvent(Holder<GameEvent> event) {
        return event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING);
    }
}
