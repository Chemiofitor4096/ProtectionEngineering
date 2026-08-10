package com.chemiofitor.protection_engineering.network;

import com.chemiofitor.protection_engineering.event.PEGameEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 客户端 → 服务端：喷气背包推力请求（无负载）。
 * <p>
 * 仅记录请求，实际推力在 PlayerTickEvent 中处理。
 */
public class ThrustJetpackMessage {

    public ThrustJetpackMessage() {}

    public static void encode(ThrustJetpackMessage msg, FriendlyByteBuf buf) {}

    public static ThrustJetpackMessage decode(FriendlyByteBuf buf) {
        return new ThrustJetpackMessage();
    }

    public static void handle(ThrustJetpackMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var sender = ctx.get().getSender();
            if (sender != null) {
                PEGameEvents.thrustRequests.add(sender.getUUID());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
