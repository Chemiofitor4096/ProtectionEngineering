package com.chemiofitor.protection_engineering.data;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.registry.PEItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class PEMechanicalCraftingRecipeGen extends MechanicalCraftingRecipeGen {

    public PEMechanicalCraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ProtectionEngineering.MODID);

        // ── 空气过滤器 ───────────────────────────────────────
        create(PEItems.AIR_FILTER::get)
                .recipe(b -> b
                        .patternLine(" BSB ")
                        .patternLine("SCOCS")
                        .patternLine("SBSBS")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('C', Items.CHARCOAL)
                        .key('O', AllItems.POWDERED_OBSIDIAN.get())
                );

        // ── 工程师眼镜 ───────────────────────────────────────
        create(PEItems.ENGINEER_GOGGLES::get)
                .recipe(b -> b
                        .patternLine("  BS ")
                        .patternLine("  BS ")
                        .patternLine("  SG ")
                        .patternLine("  BS ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('G', AllItems.GOGGLES.get())
                );

        // ── 夜视眼镜 ─────────────────────────────────────────
        create(PEItems.NIGHT_VISION_GOGGLES::get)
                .recipe(b -> b
                        .patternLine("  BS ")
                        .patternLine("  BZ ")
                        .patternLine("  GA ")
                        .patternLine("  BS ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('Z', AllItems.ZINC_INGOT.get())
                        .key('G', Items.GLOWSTONE_DUST)
                        .key('A', Items.AMETHYST_SHARD)
                );

        // ── 潜水设备 ─────────────────────────────────────────
        create(PEItems.DIVING_DEVICE::get)
                .recipe(b -> b
                        .patternLine(" S   ")
                        .patternLine(" SGGG")
                        .patternLine(" SCS ")
                        .patternLine(" SCS ")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('G', Items.GLASS_PANE)
                        .key('C', AllItems.COPPER_SHEET.get())
                );

        // ── 轻型外骨骼 ───────────────────────────────────────
        create(PEItems.LIGHT_EXOSKELETON::get)
                .recipe(b -> b
                        .patternLine("BSPSB")
                        .patternLine("BB BB")
                        .patternLine(" S S ")
                        .patternLine(" S S ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                );

        // ── 喷气背包 ─────────────────────────────────────────
        create(PEItems.JETPACK::get)
                .recipe(b -> b
                        .patternLine(" SSS ")
                        .patternLine("ISNSI")
                        .patternLine("ISSSI")
                        .patternLine("IF FI")
                        .patternLine(" W W ")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('I', AllItems.IRON_SHEET.get())
                        .key('N', Items.NETHER_STAR)
                        .key('F', AllBlocks.ENCASED_FAN.get())
                        .key('W', AllBlocks.STEAM_WHISTLE.get())
                );

        // ── 应激反馈背包 ─────────────────────────────────────
        create(PEItems.DODGE_JETPACK::get)
                .recipe(b -> b
                        .patternLine("ISCSI")
                        .patternLine(" IPI ")
                        .patternLine(" ITI ")
                        .key('I', AllItems.IRON_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('C', AllBlocks.REDSTONE_CONTACT.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                        .key('T', AllItems.ELECTRON_TUBE.get())
                );

        // ── 坚固防护板 ───────────────────────────────────────
        create(PEItems.STURDY_PLATE::get)
                .recipe(b -> b
                        .patternLine(" BBB ")
                        .patternLine(" SSS ")
                        .patternLine(" SSS ")
                        .patternLine(" BBB ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                );

        // ── 火箭发射器 ───────────────────────────────────────
        create(PEItems.ROCKET_LAUNCHER::get)
                .recipe(b -> b
                        .patternLine("SS SS")
                        .patternLine("ST TS")
                        .patternLine(" BDB ")
                        .patternLine(" BPB ")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('T', Items.TNT)
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('D', Items.DIAMOND)
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                );

        // ── 主动防御系统 ─────────────────────────────────────
        create(PEItems.APS::get)
                .recipe(b -> b
                        .patternLine("BB BB")
                        .patternLine("BTCTB")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('T', Items.TNT)
                        .key('C', AllBlocks.REDSTONE_CONTACT.get())
                );

        // ── 高级主动防御系统 ─────────────────────────────────
        create(PEItems.ADVANCED_APS::get)
                .recipe(b -> b
                        .patternLine("SSPSS")
                        .patternLine("ITATI")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                        .key('I', AllItems.IRON_SHEET.get())
                        .key('T', AllItems.ELECTRON_TUBE.get())
                        .key('A', PEItems.APS.get())
                );

        // ── 导弹背包 ─────────────────────────────────────────
        create(PEItems.MISSILE_PACK::get)
                .recipe(b -> b
                        .patternLine("SS SS")
                        .patternLine("S D S")
                        .patternLine("S P S")
                        .patternLine("SC CS")
                        .patternLine("BB BB")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('D', Items.DIAMOND)
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                        .key('C', AllBlocks.REDSTONE_CONTACT.get())
                        .key('B', AllItems.BRASS_SHEET.get())
                );

        // ── 缓冲护膝 ─────────────────────────────────────────
        create(PEItems.CUSHIONED_KNEECAP::get)
                .recipe(b -> b
                        .patternLine(" S S ")
                        .patternLine("WM MW")
                        .patternLine(" S S ")
                        .key('W', ItemTags.WOOL)
                        .key('M', Items.WHITE_WOOL)
                        .key('S', AllItems.STURDY_SHEET.get())
                );

        // ── 弹跳助力膝 ─────────────────────────────────────────
        create(PEItems.SPRINGY_KNEECAP::get)
                .recipe(b -> b
                        .patternLine(" S S ")
                        .patternLine("WM MW")
                        .patternLine(" S S ")
                        .key('W', ItemTags.WOOL)
                        .key('M', Items.SLIME_BLOCK)
                        .key('S', AllItems.STURDY_SHEET.get())
                );


        // ── 单筒望远镜 ───────────────────────────────────────
        create(PEItems.SPYGLASS::get)
                .recipe(b -> b
                        .patternLine(" BSB ")
                        .patternLine(" SGS ")
                        .patternLine(" BAB ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('G', Items.GLASS_PANE)
                        .key('A', Items.AMETHYST_SHARD)
                );

        // ── 刺激性治疗针 ─────────────────────────────────────
        create(PEItems.HORMONE_INJECTOR::get)
                .recipe(b -> b
                        .patternLine(" FBF ")
                        .patternLine(" GBG ")
                        .patternLine(" LBL ")
                        .patternLine(" SPS ")
                        .key('F', Items.BLAZE_POWDER)
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('G', Items.GLISTERING_MELON_SLICE)
                        .key('L', Items.GLOWSTONE_DUST)
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                );

        // ── 机械拳套 ─────────────────────────────────────────
        create(PEItems.MECHANICAL_GAUNTLET::get)
                .recipe(b -> b
                        .patternLine(" W W ")
                        .patternLine(" I I ")
                        .patternLine(" S S ")
                        .key('W', Items.RED_WOOL)
                        .key('I', AllItems.IRON_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                );

        // ── 动量背包 ─────────────────────────────────────────
        create(PEItems.MOMENTUM_JETPACK::get)
                .recipe(b -> b
                        .patternLine(" SES ")
                        .patternLine("SBJBS")
                        .patternLine(" B B ")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('E', Items.END_CRYSTAL)
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('J', PEItems.JETPACK.get())
                );

        // ── 重型外骨骼 ───────────────────────────────────────
        create(PEItems.HEAVY_EXOSKELETON::get)
                .recipe(b -> b
                        .patternLine("BSPSB")
                        .patternLine("BB BB")
                        .patternLine(" R R ")
                        .patternLine(" S S ")
                        .patternLine(" S S ")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                        .key('R', AllBlocks.SHAFT.get())
                );

        // ── 额外机械臂 ───────────────────────────────────────
        create(PEItems.EXTRA_MECHANICAL_ARM::get)
                .recipe(b -> b
                        .patternLine(" S S ")
                        .patternLine("ASPSA")
                        .patternLine(" S S ")
                        .key('A', AllBlocks.MECHANICAL_ARM.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('P', AllItems.PRECISION_MECHANISM.get())
                );

        // ── 改良鞋底 ─────────────────────────────────────────
        create(PEItems.IMPROVED_SOLES::get)
                .recipe(b -> b
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine(" B B ")
                        .patternLine("SB BS")
                        .key('B', AllItems.BRASS_SHEET.get())
                        .key('S', AllItems.STURDY_SHEET.get())
                );

        // ── 缓冲鞋底 ─────────────────────────────────────────
        create(PEItems.CUSHIONED_SOLES::get)
                .recipe(b -> b
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine(" S S ")
                        .patternLine("SM MS")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('M', Items.SLIME_BLOCK)
                );

        // ── 隔热鞋底 ─────────────────────────────────────────
        create(PEItems.INSULATED_SOLES::get)
                .recipe(b -> b
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine(" S S ")
                        .patternLine("SM MS")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('M', Items.MAGMA_CREAM)
                );

        // ── 静音鞋底 ─────────────────────────────────────────
        create(PEItems.SILENT_SOLES::get)
                .recipe(b -> b
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine(" S S ")
                        .patternLine("SC CS")
                        .patternLine("WW WW")
                        .key('W', Items.CYAN_WOOL)
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('C', AllItems.CARDBOARD.get())
                );

        // ── 冰霜鞋底 ─────────────────────────────────────────
        create(PEItems.FROST_SOLES::get)
                .recipe(b -> b
                        .patternLine("     ")
                        .patternLine("     ")
                        .patternLine(" S S ")
                        .patternLine("SC CS")
                        .patternLine("II II")
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('C', AllItems.COPPER_SHEET.get())
                        .key('I', Blocks.ICE )
                );

        // ── 防爆内衬 ─────────────────────────────────────────
        create(PEItems.BLAST_LINING::get)
                .recipe(b -> b
                        .patternLine("  O  ")
                        .patternLine(" WWW ")
                        .patternLine(" BSB ")
                        .key('O', Items.OBSIDIAN)
                        .key('W', Items.RED_WOOL)
                        .key('S', AllItems.STURDY_SHEET.get())
                        .key('B', AllItems.BRASS_SHEET.get())
                );
    }
}
