package com.chemiofitor.protection_engineering.network;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 模组网络通道 — Forge SimpleChannel（1.20.1 无 CustomPacketPayload/StreamCodec）。
 * <p>
 * 消息在 {@link com.chemiofitor.protection_engineering.event.PENetworkEvents#register()} 中注册。
 */
public final class PEChannel {

    private PEChannel() {}

    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ProtectionEngineering.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
}
