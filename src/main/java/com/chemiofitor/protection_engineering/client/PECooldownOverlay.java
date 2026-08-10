package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.config.PEConfig;
import com.chemiofitor.protection_engineering.item.AttachmentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 附件状态 HUD 覆盖层 — 右上角显示激活/冷却倒计时。
 * 直接读取统一状态机，无需逆向推导。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class PECooldownOverlay {

    private static final int LINE_HEIGHT = 12;

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("cooldown_overlay", PECooldownOverlay::render);
    }

    private static void render(ForgeGui gui, GuiGraphics graphics, float partialTick,
                               int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.options.hideGui) return;

        List<StatusEntry> entries = collectStatus(player);
        if (entries.isEmpty()) return;

        Font font = mc.font;
        int screenW = graphics.guiWidth();
        int xOffset = PEConfig.HUD_OFFSET_X.get();
        int y = PEConfig.HUD_OFFSET_Y.get();

        Component title = Component.translatable("hud.protectionengineering.cooldown_title");
        graphics.drawString(font, title, screenW - xOffset - 120, y, 0xFFCCCCCC, false);
        y += LINE_HEIGHT;

        for (StatusEntry e : entries) {
            graphics.drawString(font, e.text, screenW - xOffset - font.width(e.text), y, e.color, false);
            y += LINE_HEIGHT;
        }
    }

    private static final int PURPLE = 0xFFFF55FF;

    private static List<StatusEntry> collectStatus(LocalPlayer player) {
        List<StatusEntry> entries = new ArrayList<>();
        long now = player.level().getGameTime();
        boolean creative = player.getAbilities().instabuild;

        AttachmentUtil.forEach(player, (match, attachment) -> {
            if (!(attachment instanceof AttachmentItem att)) return;
            ItemStack attached = match.stack();

            IAttachment.ControlPattern pattern = att.getControlPattern();
            if (pattern == IAttachment.ControlPattern.PASSIVE
                    || pattern == IAttachment.ControlPattern.ALWAYS_ON) return;

            int state = att.getState(attached);
            String name = attached.getHoverName().getString().replaceAll("§.", "");
            String text;
            int color;
            long sortKey;

            switch (state) {
                case IAttachment.STATE_READY -> {
                    if (creative) {
                        text = name + " §d●";
                        color = PURPLE;
                    } else {
                        text = name + " §a●";
                        color = 0xFF55FF55;
                    }
                    sortKey = 0;
                }
                case IAttachment.STATE_ACTIVE -> {
                    long timer = att.getTimer(attached);
                    if (timer > 0) {
                        long remaining = timer - now;
                        if (remaining <= 0) return;
                        text = name + (creative ? " §d⚡" : " §b⚡") + formatTime(remaining);
                        color = creative ? PURPLE : 0xFF55FFFF;
                        sortKey = remaining;
                    } else {
                        text = name + " §d⚡";
                        color = PURPLE;
                        sortKey = 0;
                    }
                }
                case IAttachment.STATE_COOLING -> {
                    long cdTimer = att.getTimer(attached);
                    if (cdTimer > 0) {
                        long remaining = cdTimer - now;
                        if (remaining <= 0) return;
                        text = name + (creative ? " §d⌛" : " §7⌛") + formatTime(remaining);
                        long cdDur = att.getCooldownDuration();
                        if (creative) {
                            color = PURPLE;
                        } else {
                            float pct = cdDur > 0 ? (float) remaining / (float) cdDur : 0;
                            if (pct > 0.5f) color = 0xFFAAAAAA;
                            else if (pct > 0.25f) color = 0xFFFFAA00;
                            else color = 0xFFFF5555;
                        }
                        sortKey = remaining;
                    } else {
                        return;
                    }
                }
                default -> { // DISABLED
                    if (pattern == IAttachment.ControlPattern.FREE_TOGGLE) {
                        text = name + (creative ? " §d○" : " §8○");
                        color = creative ? PURPLE : 0xFF888888;
                        sortKey = Long.MAX_VALUE;
                    } else {
                        return;
                    }
                }
            }

            entries.add(new StatusEntry(text, color, sortKey));
        });

        entries.sort(Comparator.comparingLong(e -> e.sortKey));
        return entries;
    }

    private static String formatTime(long ticks) {
        if (ticks <= 0) return "0s";
        if (ticks > 72000) return ">1h"; // 上限防溢出
        long seconds = ticks / 20;
        if (seconds >= 60) return (seconds / 60) + "m" + (seconds % 60) + "s";
        return seconds + "s";
    }

    private record StatusEntry(String text, int color, long sortKey) {}
}
