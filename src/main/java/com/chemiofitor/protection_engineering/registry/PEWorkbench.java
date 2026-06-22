package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.block.WorkbenchBlock;
import com.chemiofitor.protection_engineering.block.WorkbenchBlockEntity;
import com.chemiofitor.protection_engineering.menu.WorkbenchMenu;
import com.chemiofitor.protection_engineering.menu.WorkbenchScreen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import net.minecraft.world.level.block.SoundType;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.REGISTRATE;

public class PEWorkbench {

    public static final BlockEntry<WorkbenchBlock> WORKBENCH_BLOCK = REGISTRATE
            .block("workbench", WorkbenchBlock::new)
            .properties(p -> p.strength(2.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL))
            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(),
                    prov.models().getExistingFile(ctx.getId())))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<WorkbenchBlockEntity> WORKBENCH_BE =
            REGISTRATE.<WorkbenchBlockEntity>blockEntity("workbench", WorkbenchBlockEntity::new)
                    .validBlock(WORKBENCH_BLOCK)
                    .register();

    public static final MenuEntry<WorkbenchMenu> WORKBENCH_MENU = REGISTRATE
            .menu("workbench", (type, windowId, inv) -> new WorkbenchMenu(windowId, inv),
                    () -> WorkbenchScreen::new)
            .register();

    public static void init() {}
}
