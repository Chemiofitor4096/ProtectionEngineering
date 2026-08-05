package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NeoForge.EVENT_BUS 事件 — 需在 ProtectionEngineering 构造器中显式注册。
 */
public class PENeoForgeEvents {

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

        // 合并同槽位同属性的 ADD_VALUE 护甲/韧性修饰符，
        // 避免 tooltip 同时显示"护甲自带 +X 护甲"与"附件 +Y 护甲"两行。
        mergeArmorModifiers(event);
    }

    // ── 护甲值合并 ─────────────────────────────────────────────
    // 护甲默认护甲值（ArmorMaterial → ATTRIBUTE_MODIFIERS）与附件通过事件添加的
    // 护甲值是两条独立修饰符，tooltip 会逐条显示。这里按（属性, 槽位组）把 ADD_VALUE
    // 条目求和后保留第一条的 ID、移除其余来源 —— 数值与分条完全等价，只是显示归一。
    // ⚠️ 必须保留原 ID 而非新建统一 ID：实体穿戴属性按修饰符 ID 去重，
    // 4 件护甲若共用同一 ID，穿戴时只会算一件的护甲值。
    // 无附件（单条）时跳过。

    private static void mergeArmorModifiers(ItemAttributeModifierEvent event) {
        List<ItemAttributeModifiers.Entry> snapshot = new ArrayList<>(event.getModifiers());

        // 按 (属性, 槽位组) 分组 ADD_VALUE 的护甲/韧性条目
        Map<List<Object>, List<ItemAttributeModifiers.Entry>> groups = new HashMap<>();
        for (ItemAttributeModifiers.Entry entry : snapshot) {
            AttributeModifier modifier = entry.modifier();
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) continue;
            if (!isArmorOrToughness(entry.attribute())) continue;
            groups.computeIfAbsent(List.of(entry.attribute().value(), entry.slot()), k -> new ArrayList<>()).add(entry);
        }

        for (List<ItemAttributeModifiers.Entry> group : groups.values()) {
            if (group.size() <= 1) continue;
            double sum = 0;
            for (ItemAttributeModifiers.Entry entry : group) sum += entry.modifier().amount();

            ItemAttributeModifiers.Entry first = group.getFirst();
            // 移除其余来源（附件添加的 / 默认之外的条目）
            for (int i = 1; i < group.size(); i++) {
                ItemAttributeModifiers.Entry entry = group.get(i);
                event.removeModifier(entry.attribute(), entry.modifier().id());
            }
            // 用总和替换第一条 —— 保留其 ID（默认即 minecraft:armor.<slot>，跨部件唯一）
            event.replaceModifier(first.attribute(),
                    new AttributeModifier(first.modifier().id(), sum, first.modifier().operation()),
                    first.slot());
        }
    }

    private static boolean isArmorOrToughness(Holder<Attribute> attribute) {
        Attribute value = attribute.value();
        return value == Attributes.ARMOR.value() || value == Attributes.ARMOR_TOUGHNESS.value();
    }

    // ── 装备变更 ──────────────────────────────────────────────

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        var slot = event.getSlot();
        if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) return;

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
