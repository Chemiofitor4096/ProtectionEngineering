package com.chemiofitor.protection_engineering.menu;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class WorkbenchScreen extends AbstractContainerScreen<WorkbenchMenu> {

    private static final ResourceLocation TEXTURE =
            ProtectionEngineering.asResource("textures/gui/workbench.png");

    // 翻页按钮位置 (贴图自带，始终显示)
    private static final int BTN_PREV_X = 126, BTN_NEXT_X = 134, BTN_Y = 40;
    private static final int BTN_W = 6, BTN_H = 12;

    public WorkbenchScreen(WorkbenchMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 空附件槽位渲染专用 UI
        List<SlotType> activeSlots = menu.getActiveSlots();
        for (int i = 0; i < activeSlots.size() && i < WorkbenchMenu.ATTACH_POS.length; i++) {
            if (menu.slots.get(1 + i).hasItem()) continue;
            SlotType slot = activeSlots.get(i);
            int x = leftPos + WorkbenchMenu.ATTACH_POS[i][0];
            int y = topPos + WorkbenchMenu.ATTACH_POS[i][1];
            g.blit(slotTexture(slot), x, y, 0, 0, 16, 16, 16, 16);
        }

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);

        // 附件槽位悬停 tooltip
        List<SlotType> activeSlots = menu.getActiveSlots();
        for (int i = 0; i < activeSlots.size() && i < WorkbenchMenu.ATTACH_POS.length; i++) {
            int x = leftPos + WorkbenchMenu.ATTACH_POS[i][0];
            int y = topPos + WorkbenchMenu.ATTACH_POS[i][1];
            if (inRect(mouseX, mouseY, x, y, 16, 16)) {
                if (!menu.slots.get(1 + i).hasItem()) {
                    g.renderTooltip(font, Component.translatable(activeSlots.get(i).getTranslationKey()), mouseX, mouseY);
                }
            }
        }

        // 翻页按钮 tooltip
        int totalPages = menu.getTotalPages();
        if (totalPages > 1) {
            int page = menu.getPage();
            if (page > 0 && inRect(mouseX, mouseY, leftPos + BTN_PREV_X, topPos + BTN_Y, BTN_W, BTN_H)) {
                g.renderTooltip(font, Component.translatable("tooltip.protectionengineering.prev_page"), mouseX, mouseY);
            }
            if (page < totalPages - 1 && inRect(mouseX, mouseY, leftPos + BTN_NEXT_X, topPos + BTN_Y, BTN_W, BTN_H)) {
                g.renderTooltip(font, Component.translatable("tooltip.protectionengineering.next_page"), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int totalPages = menu.getTotalPages();
        if (totalPages > 1) {
            int page = menu.getPage();
            int bx = leftPos + BTN_PREV_X, by = topPos + BTN_Y;
            int nx = leftPos + BTN_NEXT_X, ny = topPos + BTN_Y;
            if (page > 0 && inBtn(mouseX, mouseY, bx, by)) {
                this.minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 0);
                return true;
            }
            if (page < totalPages - 1 && inBtn(mouseX, mouseY, nx, ny)) {
                this.minecraft.gameMode.handleInventoryButtonClick(menu.containerId, 1);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private static boolean inBtn(double mx, double my, int x, int y) {
        return inRect(mx, my, x, y, BTN_W, BTN_H);
    }

    private static boolean inRect(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static ResourceLocation slotTexture(SlotType slot) {
        return new ResourceLocation(slot.id().getNamespace(),
                "textures/gui/slot/slot_" + slot.id().getPath() + ".png");
    }
}
