package com.chemiofitor.protection_engineering.menu;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 改装台附件槽位 — 动态类型、分页感知。
 */
public class AttachmentGUISlot extends Slot {

    private final WorkbenchMenu menu;
    private final int slotIndex; // 0-3, 在当前页内的索引

    public AttachmentGUISlot(WorkbenchMenu menu, SimpleContainer container,
                              int slotIndex, int x, int y) {
        super(container, 1 + slotIndex, x, y);
        this.menu = menu;
        this.slotIndex = slotIndex;
    }

    /**
     * 当前页内的槽位类型，页超出范围或无此索引时返回 null。
     */
    private SlotType getCurrentType() {
        List<SlotType> slots = menu.getActiveSlots();
        return slotIndex < slots.size() ? slots.get(slotIndex) : null;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        SlotType type = getCurrentType();
        return type != null
                && stack.getItem() instanceof IAttachment att
                && att.compatibleSlots().contains(type);
    }

    @Override
    public int getMaxStackSize() { return 1; }

    @Override
    public boolean isActive() { return getCurrentType() != null; }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.onTake(player, stack);
        menu.saveAndSync();
        menu.playAttachSound();
    }

    @Override
    public void set(ItemStack stack) {
        boolean wasEmpty = getItem().isEmpty();
        super.set(stack);
        if (!wasEmpty || !stack.isEmpty()) {
            menu.saveAndSync();
            if (wasEmpty != stack.isEmpty()) menu.playAttachSound();
        }
    }
}
