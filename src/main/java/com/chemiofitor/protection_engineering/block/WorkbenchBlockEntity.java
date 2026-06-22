package com.chemiofitor.protection_engineering.block;

import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.menu.WorkbenchMenu;
import com.chemiofitor.protection_engineering.registry.PEWorkbench;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class WorkbenchBlockEntity extends BaseContainerBlockEntity {

    public static final int SLOT_ARMOR  = 0;
    public static final int SLOT_ATTACH = 4;
    public static final int SLOT_COUNT  = 1 + SLOT_ATTACH;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public WorkbenchBlockEntity(BlockEntityType<WorkbenchBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public WorkbenchBlockEntity(BlockPos pos, BlockState state) {
        this(PEWorkbench.WORKBENCH_BE.get(), pos, state);
    }

    // ── BaseContainerBlockEntity ─────────────────────────────────

    @Override protected NonNullList<ItemStack> getItems() { return items; }
    @Override protected void setItems(NonNullList<ItemStack> list) { this.items = list; }
    @Override public int getContainerSize() { return SLOT_COUNT; }
    @Override protected Component getDefaultName() { return Component.translatable("block.protectionengineering.workbench"); }

    /** 改装台不持久化物品（关闭 UI 物品掉落） */
    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
    }

    // ── Menu ─────────────────────────────────────────────────────

    @Override
    protected AbstractContainerMenu createMenu(int windowId, Inventory playerInv) {
        return new WorkbenchMenu(windowId, playerInv, this);
    }

    // ── Helpers ──────────────────────────────────────────────────

    public ItemStack getArmor() { return items.get(SLOT_ARMOR); }

    public List<SlotType> getActiveSlots() {
        ItemStack armor = getArmor();
        if (armor.isEmpty() || !(armor.getItem() instanceof IAttachmentHost host))
            return List.of();
        return List.copyOf(host.supportedSlots());
    }

    public void loadAttachments() {
        for (int i = 1; i < SLOT_COUNT; i++) items.set(i, ItemStack.EMPTY);

        ItemStack armor = getArmor();
        if (armor.isEmpty() || !(armor.getItem() instanceof IAttachmentHost host)) return;

        AttachmentsData data = host.getAttachments(armor);
        List<SlotType> active = getActiveSlots();
        for (int i = 0; i < active.size(); i++)
            items.set(1 + i, data.get(active.get(i)).copy());
    }

    public void clearAttachments() {
        for (int i = 1; i < SLOT_COUNT; i++) items.set(i, ItemStack.EMPTY);
    }

    /** 将附件槽内容写入护甲 DataComponent，然后在菜单关闭/掉落前调用 */
    public void applyAttachments() {
        ItemStack armor = getArmor();
        if (armor.isEmpty() || !(armor.getItem() instanceof IAttachmentHost host)) return;

        AttachmentsData data = host.getAttachments(armor);
        List<SlotType> active = getActiveSlots();
        for (int i = 0; i < active.size(); i++)
            data = data.with(active.get(i), items.get(1 + i));
        host.setAttachments(armor, data);
    }
}
