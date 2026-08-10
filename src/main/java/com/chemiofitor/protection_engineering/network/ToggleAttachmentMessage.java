package com.chemiofitor.protection_engineering.network;

import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.item.AttachmentItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 客户端 → 服务端：切换指定槽位附件的开关状态。
 */
public class ToggleAttachmentMessage {

    private final int armorIndex;
    private final ResourceLocation slotTypeId;

    public ToggleAttachmentMessage(int armorIndex, ResourceLocation slotTypeId) {
        this.armorIndex = armorIndex;
        this.slotTypeId = slotTypeId;
    }

    public static void encode(ToggleAttachmentMessage msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.armorIndex);
        buf.writeResourceLocation(msg.slotTypeId);
    }

    public static ToggleAttachmentMessage decode(FriendlyByteBuf buf) {
        return new ToggleAttachmentMessage(buf.readVarInt(), buf.readResourceLocation());
    }

    public static void handle(ToggleAttachmentMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if (player == null) return;

            int idx = msg.armorIndex;
            if (idx < 0 || idx >= EquipmentSlot.values().length) return;
            EquipmentSlot slot = EquipmentSlot.values()[idx];
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) return;

            SlotType type = SlotType.byId(msg.slotTypeId);
            AttachmentsData data = host.getAttachments(armor);
            ItemStack attachment = data.get(type).copy();
            if (attachment.isEmpty()) return;

            if (attachment.getItem() instanceof AttachmentItem attItem) {
                attItem.onActivatePress(attachment, armor, player);
            }
            host.setAttachments(armor, data.with(type, attachment));
        });
        ctx.get().setPacketHandled(true);
    }
}
