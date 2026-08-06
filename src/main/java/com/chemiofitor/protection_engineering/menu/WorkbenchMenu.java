package com.chemiofitor.protection_engineering.menu;

import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.block.WorkbenchBlockEntity;
import com.chemiofitor.protection_engineering.registry.PESounds;
import com.chemiofitor.protection_engineering.registry.PEWorkbench;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class WorkbenchMenu extends AbstractContainerMenu {

    public static final int SLOT_ARMOR = 0;
    public static final int SLOT_ATTACH = 4;
    public static final int SLOT_COUNT = 1 + SLOT_ATTACH;

    /** 附件槽位在 GUI 中的位置（供 Screen 复用） */
    public static final int[][] ATTACH_POS = {{88, 29}, {88, 47}, {106, 29}, {106, 47}};

    private final SimpleContainer container = new SimpleContainer(SLOT_COUNT);
    @Nullable
    private final WorkbenchBlockEntity be;
    private boolean syncing;
    private int page;

    // ── 客户端构造器 ───────────────────────────────────────────

    public WorkbenchMenu(int windowId, Inventory playerInv) {
        this(windowId, playerInv, null);
    }

    // ── 服务端构造器 ───────────────────────────────────────────

    public WorkbenchMenu(int windowId, Inventory playerInv, @Nullable WorkbenchBlockEntity be) {
        super(PEWorkbench.WORKBENCH_MENU.get(), windowId);
        this.be = be;

        addDataSlot(new DataSlot() {
            @Override public int get() { return page; }
            @Override public void set(int value) { page = value; }
        });

        // 护甲槽
        addSlot(new Slot(container, 0, 44, 39) {
            @Override public boolean mayPlace(ItemStack s) { return s.getItem() instanceof IAttachmentHost; }
            @Override public int getMaxStackSize() { return 1; }
            @Override
            public void set(ItemStack s) {
                ItemStack old = getItem().copy();
                super.set(s);
                if (!syncing && old.getItem() != s.getItem()) {
                    page = 0; // 换不同护甲时重置页码
                    loadFromArmor();
                }
            }
        });

        // 附件槽
        for (int i = 0; i < SLOT_ATTACH; i++)
            addSlot(new AttachmentGUISlot(this, container, i, ATTACH_POS[i][0], ATTACH_POS[i][1]));

        // 玩家背包
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
    }

    // ── 护甲 ⇄ 附件 读写 ──────────────────────────────────────

    private ItemStack getArmorStack() { return container.getItem(SLOT_ARMOR); }

    private List<SlotType> getAllSlots() {
        ItemStack armor = getArmorStack();
        if (armor.isEmpty() || !(armor.getItem() instanceof IAttachmentHost host)) return List.of();
        return List.copyOf(host.supportedSlots());
    }

    private void loadFromArmor() {
        syncing = true;
        for (int i = 1; i < SLOT_COUNT; i++) container.setItem(i, ItemStack.EMPTY);

        ItemStack armor = getArmorStack();
        if (armor.getItem() instanceof IAttachmentHost host) {
            AttachmentsData data = host.getAttachments(armor);
            var all = getAllSlots();
            int start = page * SLOT_ATTACH;
            for (int i = 0; start + i < all.size() && i < SLOT_ATTACH; i++)
                container.setItem(1 + i, data.get(all.get(start + i)).copy());
        }
        syncing = false;
    }

    private void applyToArmor() {
        if (syncing) return;
        syncing = true;
        ItemStack armor = getArmorStack();
        if (armor.getItem() instanceof IAttachmentHost host) {
            AttachmentsData data = host.getAttachments(armor);
            var all = getAllSlots();
            int start = page * SLOT_ATTACH;
            for (int i = 0; start + i < all.size() && i < SLOT_ATTACH; i++) {
                ItemStack containerItem = container.getItem(1 + i);
                if (!ItemStack.matches(data.get(all.get(start + i)), containerItem))
                    data = data.with(all.get(start + i), containerItem);
            }
            host.setAttachments(armor, data);
        }
        syncing = false;
    }

    void saveAndSync() {
        if (be == null) return; // 客户端不写盔甲数据
        applyToArmor();
    }

    void playAttachSound() {
        if (be == null || be.getLevel() == null) return;
        var sound = RandomSource.create().nextBoolean()
                ? PESounds.ATTACH_1.get() : PESounds.ATTACH_2.get();
        be.getLevel().playSound(null, be.getBlockPos(), sound, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    // ── 翻页 ──────────────────────────────────────────────────

    public int getPage() { return page; }

    public int getTotalPages() {
        int size = getAllSlots().size();
        return Math.max(1, (size + SLOT_ATTACH - 1) / SLOT_ATTACH);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id != 0 && id != 1) return false;
        applyToArmor();
        if (id == 0) page = Math.max(0, page - 1);
        else page = Math.min(getTotalPages() - 1, page + 1);
        loadFromArmor();
        broadcastChanges();
        return true;
    }

    public List<SlotType> getActiveSlots() {
        var all = getAllSlots();
        int start = page * SLOT_ATTACH;
        if (start >= all.size()) return List.of();
        int end = Math.min(start + SLOT_ATTACH, all.size());
        return all.subList(start, end);
    }

    // ── 关闭 ──────────────────────────────────────────────────

    @Override
    public void removed(Player player) {
        if (player instanceof ServerPlayer) {
            applyToArmor();
        }
        // 清空附件槽（已写入护甲），避免掉落时重复返还
        for (int i = 1; i < SLOT_COUNT; i++) container.setItem(i, ItemStack.EMPTY);
        clearContainer(player, container);
        super.removed(player);
    }

    // ── shift-click ───────────────────────────────────────────

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack src = slot.getItem();
        ItemStack copy = src.copy();

        if (index < SLOT_COUNT) {
            if (!moveItemStackTo(src, SLOT_COUNT, slots.size(), true))
                return ItemStack.EMPTY;
        } else {
            boolean isHost = src.getItem() instanceof IAttachmentHost;
            if (isHost && !moveItemStackTo(src, 0, 1, false)) return ItemStack.EMPTY;
            if (!isHost && !moveItemStackTo(src, 1, SLOT_COUNT, false))
                return ItemStack.EMPTY;
        }
        if (src.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, src);
        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public SimpleContainer getContainer() { return container; }
}
