package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.item.SpyglassItem;
import com.chemiofitor.protection_engineering.registry.PEDataComponents;
import com.chemiofitor.protection_engineering.registry.PEItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/**
 * 激素针 HUD 叠加层 + FOV 缩放效果。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT)
public class PEHormoneOverlay {

    public static final ResourceLocation OVERLAY_TEX =
            ProtectionEngineering.asResource("textures/gui/hormone_overlay.png");
    public static final ResourceLocation LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(ProtectionEngineering.MODID, "hormone_overlay");
    public static final ResourceLocation SPYGLASS_LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(ProtectionEngineering.MODID, "spyglass_scope");
    private static final ResourceLocation SPYGLASS_SCOPE_TEX =
            ResourceLocation.withDefaultNamespace("textures/misc/spyglass_scope.png");

    // ── 叠加层注册 (MOD bus) ────────────────────────────────

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID,
                (gui, delta) -> renderOverlay(gui, delta));
        event.registerAboveAll(SPYGLASS_LAYER_ID,
                (gui, delta) -> renderSpyglassScope(gui));
    }

    // ── 叠加层渲染 ──────────────────────────────────────────

    private static void renderOverlay(GuiGraphics gui, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        float alpha = getOverlayAlpha(player);
        if (alpha <= 0) return;

        int w = gui.guiWidth();
        int h = gui.guiHeight();

        RenderSystem.depthFunc(519);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
        RenderSystem.setShaderTexture(0, OVERLAY_TEX);

        gui.blit(OVERLAY_TEX, 0, 0, -90, 0, 0, w, h, w, h);

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        RenderSystem.depthFunc(515);
    }

    // ── 望远镜遮罩 ──────────────────────────────────────────

    // 完全匹配原版 Gui.renderSpyglassOverlay 实现
    private static void renderSpyglassScope(GuiGraphics gui) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.getCameraType() != net.minecraft.client.CameraType.FIRST_PERSON) return;
        if (!isSpyglassActive(mc.player)) return;

        int w = gui.guiWidth();
        int h = gui.guiHeight();
        float f = Math.min(w, h);
        float f1 = Math.min((float)w / f, (float)h / f) * 1.125F;
        int i = (int)(f * f1);
        int j = (int)(f * f1);
        int k = (w - i) / 2;
        int l = (h - j) / 2;
        int i1 = k + i;
        int j1 = l + j;

        RenderSystem.enableBlend();
        gui.blit(SPYGLASS_SCOPE_TEX, k, l, -90, 0, 0, i, j, i, j);
        RenderSystem.disableBlend();
        gui.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), 0, j1, w, h, -90, 0xFF000000);
        gui.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), 0, 0, w, l, -90, 0xFF000000);
        gui.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), 0, l, k, j1, -90, 0xFF000000);
        gui.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), i1, l, w, j1, -90, 0xFF000000);
    }

    private static boolean isSpyglassActive(LocalPlayer player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof SpyglassItem sg
                        && sg.getState(entry.getValue()) == IAttachment.STATE_READY) {
                    return true;
                }
            }
        }
        return false;
    }

    /** 计算叠加层透明度：激活后前 8 秒恒定 35%，后 2 秒淡出 */
    private static float getOverlayAlpha(LocalPlayer player) {
        long now = player.level().getGameTime();
        long cooldownEnd = getHormoneCooldownEnd(player);
        if (cooldownEnd <= 0) return 0;

        long activatedAt = cooldownEnd - 1200; // COOLDOWN_TICKS
        long elapsed = now - activatedAt;
        if (elapsed < 0 || elapsed >= 200) return 0; // EFFECT_DURATION = 200

        if (elapsed < 160) return 0.35f;
        return 0.35f * (1f - (elapsed - 160) / 40f);
    }

    // ── FOV 缩放 (FORGE bus) ─────────────────────────────────

    @EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT)
    public static class FovHandler {
        @SubscribeEvent
        public static void onFovModifier(ComputeFovModifierEvent event) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            // 望远镜缩放
            if (isSpyglassActive(player)) {
                event.setNewFovModifier(event.getNewFovModifier() * 0.1f);
                return;
            }

            float bonus = getFovBonus(player);
            if (bonus > 0) {
                event.setNewFovModifier(event.getNewFovModifier() * (1f + bonus));
            }
        }

        /** FOV +5%，激活后前 3 秒生效，线性回退 */
        private static float getFovBonus(LocalPlayer player) {
            long now = player.level().getGameTime();
            long cooldownEnd = getHormoneCooldownEnd(player);
            if (cooldownEnd <= 0) return 0;

            long activatedAt = cooldownEnd - 1200;
            long elapsed = now - activatedAt;
            if (elapsed < 0 || elapsed >= 60) return 0;

            return 0.05f * (1f - (float) elapsed / 60f);
        }
    }

    // ── 工具 ────────────────────────────────────────────────

    private static long getHormoneCooldownEnd(LocalPlayer player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                ItemStack attached = entry.getValue();
                if (attached.getItem() == PEItems.HORMONE_INJECTOR.get()) {
                    return attached.getOrDefault(PEDataComponents.ATTACHMENT_COOLDOWN.get(), 0L);
                }
            }
        }
        return 0L;
    }
}
