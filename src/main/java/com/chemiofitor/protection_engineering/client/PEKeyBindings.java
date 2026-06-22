package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.AttachmentHostArmorItem;
import com.chemiofitor.protection_engineering.item.HormoneInjectorItem;
import com.chemiofitor.protection_engineering.item.ApsItem;
import com.chemiofitor.protection_engineering.item.MissilePackItem;
import com.chemiofitor.protection_engineering.item.MomentumJetpackItem;
import com.chemiofitor.protection_engineering.item.NightVisionGogglesItem;
import com.chemiofitor.protection_engineering.item.RocketLauncherItem;
import com.chemiofitor.protection_engineering.item.SpyglassItem;
import com.chemiofitor.protection_engineering.network.ToggleAttachmentPayload;
import com.chemiofitor.protection_engineering.network.ThrustJetpackPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

/**
 * 客户端热键绑定 — N/H/J/K/R/G/Z 分别触发对应附件。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT)
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

    private static int clientThrustEndTick = 0;
    private static double clientThrustSpeed = 1.5;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
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
            PacketDistributor.sendToServer(new ThrustJetpackPayload());
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
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof MomentumJetpackItem) return true;
            }
        }
        return false;
    }

    private static void tryActivateAttachment(Class<? extends IAttachment> type) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof AttachmentHostArmorItem host)) continue;

            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (type.isInstance(entry.getValue().getItem())) {
                    PacketDistributor.sendToServer(new ToggleAttachmentPayload(
                            slot.ordinal(), entry.getKey().id()));
                    return;
                }
            }
        }
    }
}
