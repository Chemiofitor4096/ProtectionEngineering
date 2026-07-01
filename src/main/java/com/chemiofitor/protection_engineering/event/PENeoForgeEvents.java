package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

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
