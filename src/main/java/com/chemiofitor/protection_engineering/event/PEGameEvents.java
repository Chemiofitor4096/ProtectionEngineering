package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
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

/**
 * 游戏事件。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID)
public class PEGameEvents {

    /** 来自客户端的喷气背包推力请求 — JetpackItem.onTick 消费 */
    public static final Set<UUID> thrustRequests = new HashSet<>();

    // ── 应激反馈背包 ──────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        var source = event.getSource();

        // ── 隔热鞋底：免疫脚下热源伤害 ──────────────────────
        if ((source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.CAMPFIRE))
                && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IAttachmentHost host) {
            for (var entry : host.getAttachments(player.getItemBySlot(EquipmentSlot.FEET)).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof InsulatedSolesItem) {
                    event.setCanceled(true);
                    return;
                }
            }
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
                        // 执行闪避
                        DodgeJetpackItem.executeDodge(player, attacker);
                        // 进入冷却（创造模式跳过）
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

        // ── 投射物直接命中 → 主动防御系统拦截 ───────────
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
    }

    // ── 摔落伤害减免 ──────────────────────────────────────────

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // 距离减免（缓冲鞋底：等效摔落高度 -1）
        float distReduction = getTotalFallDistanceReduction(player);
        if (distReduction > 0) {
            event.setDistance(Math.max(0, event.getDistance() - distReduction));
        }

        // 伤害倍率减免
        float dmgReduction = Math.min(getTotalFallDamageReduction(player), 1.0f);
        if (dmgReduction > 0) {
            event.setDamageMultiplier(event.getDamageMultiplier() * (1f - dmgReduction));
        }
    }

    /** 遍历所有护甲附件，累加摔落伤害减免比例 */
    private static float getTotalFallDamageReduction(Player player) {
        float total = 0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof IAttachment att) {
                    total += att.getFallDamageReduction();
                }
            }
        }
        return total;
    }

    private static float getTotalFallDistanceReduction(Player player) {
        float total = 0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof IAttachment att) {
                    total += att.getFallDistanceReduction();
                }
            }
        }
        return total;
    }

    // ── 状态效果免疫 ──────────────────────────────────────────

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        if (hasImmunity(player, effect)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        if (hasImmunity(player, effect)) {
            player.removeEffect(effect);
        }
    }

    private static boolean hasImmunity(Player player, Holder<MobEffect> effect) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;

            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (!(entry.getValue().getItem() instanceof IAttachment attachment)) continue;
                if (attachment.getImmunities().contains(effect)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ── 静音鞋底：阻止 Warden 振动探测 ──────────────────────────

    /** 静音鞋底抑制的 GameEvent — 与潜行抑制行为一致 (见 GameEventTagsProvider.IGNORE_VIBRATIONS_SNEAKING) */
    private static boolean isSilencedGameEvent(Holder<GameEvent> event) {
        return event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING);
    }

    @SubscribeEvent
    public static void onVanillaGameEvent(VanillaGameEvent event) {
        if (!(event.getCause() instanceof Player player)) return;
        if (!isSilencedGameEvent(event.getVanillaEvent())) return;

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (boots.getItem() instanceof IAttachmentHost host
                && host.getAttachments(boots).get(SlotTypes.FOOT).getItem() instanceof SilentSolesItem) {
            event.setCanceled(true);
        }
    }

}
