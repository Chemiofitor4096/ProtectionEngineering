package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.item.ApsItem;
import com.chemiofitor.protection_engineering.item.HormoneInjectorItem;
import com.chemiofitor.protection_engineering.item.MissilePackItem;
import com.chemiofitor.protection_engineering.item.MomentumJetpackItem;
import com.chemiofitor.protection_engineering.item.NightVisionGogglesItem;
import com.chemiofitor.protection_engineering.item.RocketLauncherItem;
import com.chemiofitor.protection_engineering.item.SpyglassItem;
import com.chemiofitor.protection_engineering.network.PEChannel;
import com.chemiofitor.protection_engineering.network.ThrustJetpackMessage;
import com.chemiofitor.protection_engineering.network.ToggleAttachmentMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * 客户端热键绑定 — N/H/J/K/R/G/Z 分别触发对应附件。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT)
public class PEKeyBindings {

    public static final KeyMapping TOGGLE_NIGHT_VISION = new KeyMapping(
            "key.protectionengineering.toggle_night_vision",
            GLFW.GLFW_KEY_N,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping ACTIVATE_HORMONE = new KeyMapping(
            "key.protectionengineering.activate_hormone",
            GLFW.GLFW_KEY_H,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping THRUST_JETPACK = new KeyMapping(
            "key.protectionengineering.thrust_jetpack",
            GLFW.GLFW_KEY_J,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping TOGGLE_APS = new KeyMapping(
            "key.protectionengineering.toggle_aps",
            GLFW.GLFW_KEY_K,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping ACTIVATE_ROCKET_LAUNCHER = new KeyMapping(
            "key.protectionengineering.activate_rocket_launcher",
            GLFW.GLFW_KEY_R,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping ACTIVATE_MISSILE = new KeyMapping(
            "key.protectionengineering.activate_missile",
            GLFW.GLFW_KEY_G,
            "key.categories.protectionengineering"
    );

    public static final KeyMapping TOGGLE_SPYGLASS = new KeyMapping(
            "key.protectionengineering.toggle_spyglass",
            GLFW.GLFW_KEY_Z,
            "key.categories.protectionengineering"
    );

    /** 按键注册在 MOD 总线（RegisterKeyMappingsEvent 为 IModBusEvent） */
    @Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT,
            bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class KeyRegistration {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(TOGGLE_NIGHT_VISION);
            event.register(ACTIVATE_HORMONE);
            event.register(THRUST_JETPACK);
            event.register(TOGGLE_APS);
            event.register(ACTIVATE_ROCKET_LAUNCHER);
            event.register(ACTIVATE_MISSILE);
            event.register(TOGGLE_SPYGLASS);
        }
    }

    private static int clientThrustEndTick = 0;
    private static double clientThrustSpeed = 1.5;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        while (TOGGLE_NIGHT_VISION.consumeClick()) {
            tryActivateAttachment(NightVisionGogglesItem.class);
        }
        while (ACTIVATE_HORMONE.consumeClick()) {
            tryActivateAttachment(HormoneInjectorItem.class);
        }
        while (TOGGLE_APS.consumeClick()) {
            tryActivateAttachment(ApsItem.class);
        }
        while (ACTIVATE_ROCKET_LAUNCHER.consumeClick()) {
            tryActivateAttachment(RocketLauncherItem.class);
        }
        while (ACTIVATE_MISSILE.consumeClick()) {
            tryActivateAttachment(MissilePackItem.class);
        }
        while (TOGGLE_SPYGLASS.consumeClick()) {
            tryActivateAttachment(SpyglassItem.class);
        }

        var player = Minecraft.getInstance().player;
        if (player == null) return;

        // 喷气背包推力：按一次 → 发包 + 客户端预测
        while (THRUST_JETPACK.consumeClick()) {
            PEChannel.CHANNEL.sendToServer(new ThrustJetpackMessage());
            clientThrustEndTick = player.tickCount + (hasMomentumJetpack(player) ? 100 : 60);
            clientThrustSpeed = hasMomentumJetpack(player) ? 2.0 : 1.5;
        }

        // 客户端预测助推
        if (player.tickCount < clientThrustEndTick && player.isFallFlying()) {
            Vec3 look = player.getLookAngle();
            Vec3 vel = player.getDeltaMovement();
            double s = clientThrustSpeed;
            player.setDeltaMovement(vel.add(
                    look.x * 0.1 + (look.x * s - vel.x) * 0.5,
                    look.y * 0.1 + (look.y * s - vel.y) * 0.5,
                    look.z * 0.1 + (look.z * s - vel.z) * 0.5));
        }
    }

    private static boolean hasMomentumJetpack(Player player) {
        return AttachmentUtil.has(player, MomentumJetpackItem.class);
    }

    private static void tryActivateAttachment(Class<? extends IAttachment> type) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        AttachmentUtil.findFirst(player, type).ifPresent(match ->
            PEChannel.CHANNEL.sendToServer(new ToggleAttachmentMessage(
                    match.armorSlot().ordinal(), match.attachSlot().id())));
    }
}
