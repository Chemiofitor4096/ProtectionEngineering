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

    private static final ResourceLocation TEXTURE = ProtectionEngineering.asResource("textures/gui/workbench.png");

    // 附件槽 (x, y) 来自改装台.md，与 WorkbenchMenu.ATTACH_POS 一致
    private static final int[][] ATTACH_POS = {{88, 29}, {88, 47}, {106, 29}, {106, 47}};

    private static ResourceLocation slotTexture(SlotType slot) {
        return ResourceLocation.fromNamespaceAndPath(slot.id().getNamespace(),
                "textures/gui/slot/slot_" + slot.id().getPath() + ".png");
    }

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
        for (int i = 0; i < activeSlots.size() && i < ATTACH_POS.length; i++) {
            if (menu.slots.get(1 + i).hasItem()) continue;
            SlotType slot = activeSlots.get(i);
            int x = leftPos + ATTACH_POS[i][0];
            int y = topPos + ATTACH_POS[i][1];
            g.blit(slotTexture(slot), x, y, 0, 0, 16, 16, 16, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);

        // 空附件槽位悬停时显示槽位名
        List<SlotType> activeSlots = menu.getActiveSlots();
        for (int i = 0; i < activeSlots.size() && i < ATTACH_POS.length; i++) {
            int x = leftPos + ATTACH_POS[i][0];
            int y = topPos + ATTACH_POS[i][1];
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                if (!menu.slots.get(1 + i).hasItem()) {
                    g.renderTooltip(font, Component.translatable(activeSlots.get(i).getTranslationKey()), mouseX, mouseY);
                }
            }
        }
    }
}
