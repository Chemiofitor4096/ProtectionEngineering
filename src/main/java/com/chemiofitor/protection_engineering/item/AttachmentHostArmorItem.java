package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

import static com.chemiofitor.protection_engineering.registry.PEDataComponents.ATTACHMENTS;

/**
 * 实现了 {@link IAttachmentHost} 的护甲基类。
 */
public abstract class AttachmentHostArmorItem extends ArmorItem implements IAttachmentHost {

    protected AttachmentHostArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    /** 该模组护甲不显示附魔光效 (#改装台.md) */
    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    /** 附件宿主护甲不支持附魔 */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    // ── IAttachmentHost ─────────────────────────────────────────

    @Override
    public abstract Set<SlotType> supportedSlots();

    @Override
    public AttachmentsData getAttachments(ItemStack host) {
        return host.getOrDefault(ATTACHMENTS.get(), AttachmentsData.EMPTY);
    }

    @Override
    public void setAttachments(ItemStack host, AttachmentsData data) {
        host.set(ATTACHMENTS.get(), data);
    }
    // ── Tick ────────────────────────────────────────────────────

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof LivingEntity living)) return;
        if (living.getItemBySlot(getEquipmentSlot()) != stack) return;
        if (level.isClientSide()) return;

        AttachmentsData data = getAttachments(stack);
        boolean mutated = false;
        for (var entry : data.slots().entrySet()) {
            var key = entry.getKey();
            ItemStack attachmentStack = entry.getValue().copy();
            if (attachmentStack.getItem() instanceof IAttachment attachment) {
                attachment.onTick(attachmentStack, stack, living, key);
                if (!ItemStack.matches(entry.getValue(), attachmentStack)) {
                    data = data.with(key, attachmentStack);
                    mutated = true;
                }
            }
        }
        if (mutated) {
            setAttachments(stack, data);
        }
    }

    // ── Tooltip ─────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        AttachmentsData data = getAttachments(stack);

        tooltip.add(Component.translatable("tooltip.protectionengineering.installed_attachments")
                .withStyle(ChatFormatting.GOLD));

        for (SlotType slot : supportedSlots()) {
            ItemStack attached = data.get(slot);

            MutableComponent line = Component.literal("  ");

            if (attached.isEmpty()) {
                // 空槽位：灰色前缀 + 灰色 "—"
                line.append(Component.translatable(slot.getTranslationKey()).append(": ")
                        .withStyle(ChatFormatting.GRAY));
                line.append(Component.literal("—").withStyle(ChatFormatting.GRAY));
            } else {
                // 已安装：白色前缀 + 绿色物品名
                line.append(Component.translatable(slot.getTranslationKey()).append(": "));
                line.append(attached.getHoverName().copy().withStyle(ChatFormatting.GREEN));

                // 可开关附件：追加状态指示
                if (attached.getItem() instanceof AttachmentItem ai && ai.isToggleable()) {
                    int state = ai.getState(attached);
                    boolean showDot = state == IAttachment.STATE_READY || state == IAttachment.STATE_ACTIVE;
                    line.append(Component.literal(" " + (showDot ? "●" : "○"))
                            .withStyle(showDot ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
                }
            }
            tooltip.add(line);
        }
    }
}
