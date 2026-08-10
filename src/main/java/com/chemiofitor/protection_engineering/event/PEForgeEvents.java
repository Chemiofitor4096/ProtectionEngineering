package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Forge.EVENT_BUS 事件 — 需在 ProtectionEngineering 构造器中显式注册。
 */
public class PEForgeEvents {

    // ── 属性修饰符（护甲板等附件 bonus） ──────────────────────

    @SubscribeEvent
    public void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof IAttachmentHost host)) return;

        for (var entry : host.getAttachments(stack).slots().entrySet()) {
            if (entry.getValue().getItem() instanceof IAttachment attachment) {
                attachment.addAttributeModifiers(event);
            }
        }

        // 合并同属性（当前事件已按装备槽触发）的 ADDITION 护甲/韧性修饰符，
        // 避免 tooltip 同时显示"护甲自带 +X 护甲"与"附件 +Y 护甲"两行。
        mergeArmorModifiers(event);
    }

    // ── 护甲值合并 ─────────────────────────────────────────────
    // 护甲默认护甲值（ArmorMaterial → getDefaultAttributeModifiers）与附件通过事件添加的
    // 护甲值是两条独立修饰符，tooltip 会逐条显示。这里按属性把 ADDITION
    // 条目求和后保留第一条的 ID、移除其余来源 —— 数值与分条完全等价，只是显示归一。
    // ⚠️ 必须保留原 ID 而非新建统一 ID：实体穿戴属性按修饰符 ID 去重，
    // 4 件护甲若共用同一 ID，穿戴时只会算一件的护甲值。
    // 无附件（单条）时跳过。

    private static void mergeArmorModifiers(ItemAttributeModifierEvent event) {
        List<Map.Entry<Attribute, AttributeModifier>> snapshot =
                new ArrayList<>(event.getModifiers().entries());

        // 按属性分组 ADDITION 的护甲/韧性条目
        Map<Attribute, List<Map.Entry<Attribute, AttributeModifier>>> groups = new HashMap<>();
        for (var entry : snapshot) {
            AttributeModifier modifier = entry.getValue();
            if (modifier.getOperation() != AttributeModifier.Operation.ADDITION) continue;
            if (!isArmorOrToughness(entry.getKey())) continue;
            groups.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(entry);
        }

        for (List<Map.Entry<Attribute, AttributeModifier>> group : groups.values()) {
            if (group.size() <= 1) continue;
            double sum = 0;
            for (var entry : group) sum += entry.getValue().getAmount();

            Map.Entry<Attribute, AttributeModifier> first = group.get(0);
            // 移除全部原条目
            for (var entry : group) {
                event.removeModifier(entry.getKey(), entry.getValue());
            }
            // 用总和加入单条 —— 保留第一条的 ID（默认即 minecraft:armor.<slot>，跨部件唯一）
            AttributeModifier firstModifier = first.getValue();
            event.addModifier(first.getKey(),
                    new AttributeModifier(firstModifier.getId(), firstModifier.getName(),
                            sum, AttributeModifier.Operation.ADDITION));
        }
    }

    private static boolean isArmorOrToughness(Attribute attribute) {
        return attribute == Attributes.ARMOR || attribute == Attributes.ARMOR_TOUGHNESS;
    }

    // ── 装备变更 ──────────────────────────────────────────────

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        var slot = event.getSlot();
        if (slot.getType() != EquipmentSlot.Type.ARMOR) return;

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();

        if (from.getItem() == to.getItem()) return;

        LivingEntity entity = event.getEntity();

        if (from.getItem() instanceof IAttachmentHost) {
            for (var entry : ((IAttachmentHost) from.getItem()).getAttachments(from).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof IAttachment att) {
                    att.onUnequip(entry.getValue(), from, entity);
                }
            }
        }

        if (to.getItem() instanceof IAttachmentHost host) {
            var data = host.getAttachments(to);
            for (var entry : data.slots().entrySet()) {
                var attStack = entry.getValue();
                if (attStack.getItem() instanceof IAttachment att) {
                    att.onEquip(attStack, to, entity);
                    data = data.with(entry.getKey(), attStack);
                }
            }
            host.setAttachments(to, data);
        }
    }
}
