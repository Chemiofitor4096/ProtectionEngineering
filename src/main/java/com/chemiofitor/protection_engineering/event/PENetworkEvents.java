package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.item.AttachmentItem;
import com.chemiofitor.protection_engineering.network.ThrustJetpackPayload;
import com.chemiofitor.protection_engineering.network.ToggleAttachmentPayload;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * 网络包处理 — 附件开关（ToggleAttachmentPayload）和喷气背包推力（ThrustJetpackPayload）。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID)
public class PENetworkEvents {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        // 附件开关
        registrar.playToServer(
                ToggleAttachmentPayload.TYPE,
                ToggleAttachmentPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = context.player();

                    int idx = payload.armorIndex();
                    if (idx < 0 || idx >= EquipmentSlot.values().length) return;
                    EquipmentSlot slot = EquipmentSlot.values()[idx];
                    ItemStack armor = player.getItemBySlot(slot);
                    if (!(armor.getItem() instanceof IAttachmentHost host)) return;

                    SlotType type = SlotType.byId(payload.slotTypeId());
                    AttachmentsData data = host.getAttachments(armor);
                    ItemStack attachment = data.get(type).copy();
                    if (attachment.isEmpty()) return;

                    if (attachment.getItem() instanceof AttachmentItem attItem) {
                        attItem.onActivatePress(attachment, armor, player);
                    }
                    host.setAttachments(armor, data.with(type, attachment));
                })
        );

        // 喷气背包推力 → 仅记录请求，实际推力在 PlayerTickEvent.Pre 中处理
        registrar.playToServer(
                ThrustJetpackPayload.TYPE,
                ThrustJetpackPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    PEGameEvents.thrustRequests.add(context.player().getUUID());
                })
        );
    }
}
