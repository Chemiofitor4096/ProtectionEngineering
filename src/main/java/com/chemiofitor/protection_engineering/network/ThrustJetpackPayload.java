package com.chemiofitor.protection_engineering.network;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * 客户端 → 服务端：喷气背包推力请求。
 */
public record ThrustJetpackPayload() implements CustomPacketPayload {

    public static final Type<ThrustJetpackPayload> TYPE =
            new Type<>(ProtectionEngineering.asResource("thrust_jetpack"));

    public static final StreamCodec<FriendlyByteBuf, ThrustJetpackPayload> STREAM_CODEC =
            StreamCodec.unit(new ThrustJetpackPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
