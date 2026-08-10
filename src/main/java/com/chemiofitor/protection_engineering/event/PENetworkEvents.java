package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.network.PEChannel;
import com.chemiofitor.protection_engineering.network.ThrustJetpackMessage;
import com.chemiofitor.protection_engineering.network.ToggleAttachmentMessage;

/**
 * 网络通道注册 — 附件开关（ToggleAttachmentMessage）和喷气背包推力（ThrustJetpackMessage）。
 * <p>
 * 由 {@code ProtectionEngineering} 构造器调用（仅需一次）。
 */
public class PENetworkEvents {

    private static boolean registered = false;

    /** 注册全部消息（幂等，多次调用安全） */
    public static synchronized void register() {
        if (registered) return;
        registered = true;

        int id = 0;
        PEChannel.CHANNEL.registerMessage(id++, ToggleAttachmentMessage.class,
                ToggleAttachmentMessage::encode, ToggleAttachmentMessage::decode,
                ToggleAttachmentMessage::handle);
        PEChannel.CHANNEL.registerMessage(id++, ThrustJetpackMessage.class,
                ThrustJetpackMessage::encode, ThrustJetpackMessage::decode,
                ThrustJetpackMessage::handle);
    }
}
