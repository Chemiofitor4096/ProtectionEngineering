package com.chemiofitor.protection_engineering.menu;

import com.chemiofitor.protection_engineering.api.IAttachment;
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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.asResource;

public class WorkbenchMenu extends AbstractContainerMenu {

    // 附件槽像素位置 (来自改装台.md)
    private static final int[][] ATTACH_POS = {{88, 29}, {88, 47}, {106, 29}, {106, 47}};

    private final SimpleContainer container;
    @Nullable
    private final WorkbenchBlockEntity be;
    private boolean syncing; // 防递归标志

    // ── 客户端 ────────────────────────────────────────────────

    public WorkbenchMenu(int windowId, Inventory playerInv) {
        this(windowId, playerInv, new SimpleContainer(WorkbenchBlockEntity.SLOT_COUNT), null);
    }

    // ── 服务端 ────────────────────────────────────────────────

    public WorkbenchMenu(int windowId, Inventory playerInv, WorkbenchBlockEntity be) {
        this(windowId, playerInv, new SimpleContainer(WorkbenchBlockEntity.SLOT_COUNT), be);
        for (int i = 0; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
            container.setItem(i, be.getItem(i).copy());
    }

    private WorkbenchMenu(int windowId, Inventory playerInv, SimpleContainer container,
                          @Nullable WorkbenchBlockEntity be) {
        super(PEWorkbench.WORKBENCH_MENU.get(), windowId);
        this.container = container;
        this.be = be;

        // 护甲槽
        addSlot(new Slot(container, 0, 44, 39) {
            @Override public boolean mayPlace(ItemStack s) { return s.getItem() instanceof IAttachmentHost; }
            @Override public int getMaxStackSize() { return 1; }
            @Override
            public void set(ItemStack s) {
                ItemStack old = getItem().copy();
                super.set(s);
                if (!syncing && !ItemStack.matches(old, s)) {
                    loadFromArmor();
                }
            }
        });

        // 附件槽 — 4 固定位置，active/type 动态查询（不能构造时捕获）
        for (int i = 0; i < WorkbenchBlockEntity.SLOT_ATTACH; i++) {
            final int slotIndex = i;

            addSlot(new Slot(container, 1 + i, ATTACH_POS[i][0], ATTACH_POS[i][1]) {
                /** 每次都从容器读当前护甲支持的槽位列表 */
                private SlotType getCurrentType() {
                    var slots = getActiveSlots();
                    return slotIndex < slots.size() ? slots.get(slotIndex) : null;
                }

                @Override
                public boolean mayPlace(ItemStack s) {
                    SlotType t = getCurrentType();
                    if (t == null) return false;
                    return s.getItem() instanceof IAttachment att && att.compatibleSlots().contains(t);
                }
                @Override public int getMaxStackSize() { return 1; }
                @Override public boolean isActive() { return getCurrentType() != null; }
                @Override
                public void onTake(Player p, ItemStack s) {
                    super.onTake(p, s);
                    applyToArmor();
                    playAttachSound();
                }
                @Override
                public void set(ItemStack s) {
                    boolean wasEmpty = getItem().isEmpty();
                    super.set(s);
                    if (be != null && (wasEmpty || !s.isEmpty())) {
                        applyToArmor();
                        if (wasEmpty != s.isEmpty()) playAttachSound();
                    }
                }
            });
        }

        // 玩家背包
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
    }

    // ── BE 同步 ────────────────────────────────────────────────

    /** 护甲放入 → 从护甲 DataComponent 加载附件到工作台槽位 */
    private void loadFromArmor() {
        if (be == null || syncing) return;
        syncing = true;
        for (int i = 0; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
            be.setItem(i, container.getItem(i).copy());
        be.loadAttachments();
        for (int i = 0; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
            container.setItem(i, be.getItem(i).copy());
        broadcastChanges();
        syncing = false;
    }

    private void playAttachSound() {
        if (be == null || be.getLevel() == null) return;
        var sound = RandomSource.create().nextBoolean()
                ? PESounds.ATTACH_1.get() : PESounds.ATTACH_2.get();
        be.getLevel().playSound(null, be.getBlockPos(), sound, SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    /** 附件槽变化 → 即时写入护甲 DataComponent 并刷新护甲槽显示 */
    private void applyToArmor() {
        if (be == null || syncing) return;
        syncing = true;
        for (int i = 0; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
            be.setItem(i, container.getItem(i).copy());
        be.applyAttachments();
        ItemStack updatedArmor = be.getItem(WorkbenchBlockEntity.SLOT_ARMOR).copy();
        container.setItem(WorkbenchBlockEntity.SLOT_ARMOR, updatedArmor);
        syncing = false;
    }

    // ── Screen ─────────────────────────────────────────────────

    public List<SlotType> getActiveSlots() {
        ItemStack armor = container.getItem(0);
        if (armor.isEmpty() || !(armor.getItem() instanceof IAttachmentHost host))
            return List.of();
        return List.copyOf(host.supportedSlots());
    }

    public SimpleContainer getContainer() { return container; }

    // ── shift-click ────────────────────────────────────────────

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack src = slot.getItem();
        ItemStack copy = src.copy();

        int workbenchSize = WorkbenchBlockEntity.SLOT_COUNT;
        if (index < workbenchSize) {
            if (!moveItemStackTo(src, workbenchSize, slots.size(), true))
                return ItemStack.EMPTY;
        } else {
            boolean isHost = src.getItem() instanceof IAttachmentHost;
            if (isHost && !moveItemStackTo(src, 0, 1, false)) return ItemStack.EMPTY;
            if (!isHost && !moveItemStackTo(src, 1, workbenchSize, false))
                return ItemStack.EMPTY;
        }
        if (src.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        slot.onTake(player, src);
        return copy;
    }

    @Override
    public void removed(Player player) {
        if (be != null && player instanceof ServerPlayer) {
            // 附件写入护甲
            for (int i = 0; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
                be.setItem(i, container.getItem(i).copy());
            be.applyAttachments();

            // 清空附件槽（已写回护甲），避免 clearContainer 时重复返还
            for (int i = 1; i < WorkbenchBlockEntity.SLOT_COUNT; i++)
                container.setItem(i, ItemStack.EMPTY);

            // 仅返还护甲槽和背包物品
            clearContainer(player, container);
            be.clearContent();
        }
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
