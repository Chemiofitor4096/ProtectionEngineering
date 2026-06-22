package com.chemiofitor.protection_engineering.network;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * 客户端 → 服务端：切换指定槽位附件的开关状态。
 */
public record ToggleAttachmentPayload(int armorIndex, ResourceLocation slotTypeId)
        implements CustomPacketPayload {

    public static final Type<ToggleAttachmentPayload> TYPE =
            new Type<>(ProtectionEngineering.asResource("toggle_attachment"));

    public static final StreamCodec<FriendlyByteBuf, ToggleAttachmentPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,  ToggleAttachmentPayload::armorIndex,
                    ResourceLocation.STREAM_CODEC, ToggleAttachmentPayload::slotTypeId,
                    ToggleAttachmentPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
