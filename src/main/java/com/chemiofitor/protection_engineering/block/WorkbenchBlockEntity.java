package com.chemiofitor.protection_engineering.block;

import com.chemiofitor.protection_engineering.menu.WorkbenchMenu;
import com.chemiofitor.protection_engineering.registry.PEWorkbench;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 改装台方块实体 — 仅负责创建菜单，不存储物品（同原版工作台）。
 */
public class WorkbenchBlockEntity extends BlockEntity {

    public WorkbenchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public WorkbenchBlockEntity(BlockPos pos, BlockState state) {
        this(PEWorkbench.WORKBENCH_BE.get(), pos, state);
    }

    public Component getDisplayName() {
        return Component.translatable("block.protectionengineering.workbench");
    }

    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv) {
        return new WorkbenchMenu(windowId, playerInv, this);
    }
}
