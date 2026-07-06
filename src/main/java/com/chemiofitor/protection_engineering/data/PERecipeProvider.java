package com.chemiofitor.protection_engineering.data;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.registry.PEItems;
import com.chemiofitor.protection_engineering.registry.PEWorkbench;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class PERecipeProvider extends RecipeProvider {

    public PERecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildWorkbench(output);
        buildEngineerArmor(output);
        buildAttachments(output);
    }

    private static void buildAttachments(RecipeOutput output) {
        // 下界合金防护板：锻造台升级
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(PEItems.STURDY_PLATE.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.COMBAT,
                        PEItems.NETHERITE_PLATE.get())
                .unlocks("has_netherite_ingot",
                        InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
                .save(output, rl("netherite_plate_smithing"));

        // 制导导弹 ×4
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.MISSILE.get(), 4)
                .pattern(" O ")
                .pattern("BGB")
                .pattern("S S")
                .define('O', Items.OBSERVER)
                .define('B', AllItems.BRASS_SHEET.get())
                .define('G', Items.GUNPOWDER)
                .define('S', AllItems.STURDY_SHEET.get())
                .unlockedBy("has_gunpowder",
                        InventoryChangeTrigger.TriggerInstance.hasItems(Items.GUNPOWDER))
                .save(output, rl("missile"));

        // ── 工程师链锯剑 ─────────────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_SAW_SWORD.get())
                .pattern(" SI")
                .pattern("SA ")
                .pattern("TB ")
                .define('S', AllItems.IRON_SHEET.get())
                .define('I', AllItems.STURDY_SHEET.get())
                .define('A', AllItems.PRECISION_MECHANISM)
                .define('B', AllItems.BRASS_SHEET.get())
                .define('T', Items.STICK)
                .unlockedBy("has_sturdy_sheet",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.STURDY_SHEET.get()))
                .save(output, rl("engineer_saw_sword"));

        // ── 工程师盾牌 ───────────────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_SHIELD.get())
                .pattern(" G ")
                .pattern("BSB")
                .pattern("I I")
                .define('S', Items.SHIELD)
                .define('B', AllItems.BRASS_SHEET.get())
                .define('I', AllItems.STURDY_SHEET.get())
                .define('G', AllPaletteBlocks.FRAMED_GLASS_PANE.asItem())
                .unlockedBy("has_sturdy_sheet",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.STURDY_SHEET.get()))
                .save(output, rl("engineer_shield"));

        // ── 纯洁印记 ───────────────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.PURITY_MARK.get())
                .pattern(" R ")
                .pattern("RSW")
                .pattern(" W ")
                .define('R', Items.RED_WOOL)
                .define('W', Items.WHITE_WOOL)
                .define('S', Items.LILY_OF_THE_VALLEY)
                .unlockedBy("has_lily_of_the_valley",
                        InventoryChangeTrigger.TriggerInstance.hasItems(Items.LILY_OF_THE_VALLEY))
                .save(output, rl("purity_mark"));
    }

    private static void buildWorkbench(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PEWorkbench.WORKBENCH_BLOCK.get())
                .pattern(" D ")
                .pattern("MSM")
                .pattern("BPB")
                .define('D', AllBlocks.DISPLAY_BOARD.get())
                .define('M', AllBlocks.MECHANICAL_ARM.get())
                .define('S', AllBlocks.DEPOT.get())
                .define('B', AllBlocks.BRASS_CASING.get())
                .define('P', AllItems.PRECISION_MECHANISM.get())
                .unlockedBy("has_brass_casing",
                        InventoryChangeTrigger.TriggerInstance.hasItems(AllBlocks.BRASS_CASING.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(ProtectionEngineering.MODID, "workbench"));
    }

    // ── 工程师护甲四件套 ─────────────────────────────────────

    private static void buildEngineerArmor(RecipeOutput output) {
        var S = AllItems.STURDY_SHEET.get();
        var P = AllItems.PRECISION_MECHANISM.get();
        var W = Items.RED_WOOL;
        var B = AllItems.BRASS_SHEET.get();
        var I = AllItems.IRON_SHEET.get();

        // 兜帽
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_HOOD.get())
                .pattern("SPS")
                .pattern("WHW")
                .define('S', S).define('P', P).define('W', W)
                .define('H', Items.DIAMOND_HELMET)
                .unlockedBy("has_sturdy_sheet", InventoryChangeTrigger.TriggerInstance.hasItems(S))
                .save(output, rl("engineer_hood"));

        // 胸甲
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_CHESTPLATE.get())
                .pattern("B B")
                .pattern("SCS")
                .pattern("WPW")
                .define('B', B).define('S', S).define('C', Items.DIAMOND_CHESTPLATE)
                .define('W', W).define('P', P)
                .unlockedBy("has_sturdy_sheet", InventoryChangeTrigger.TriggerInstance.hasItems(S))
                .save(output, rl("engineer_chestplate"));

        // 护腿
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_LEGGINGS.get())
                .pattern("SPS")
                .pattern("SLS")
                .pattern("I I")
                .define('S', S).define('P', P).define('L', Items.DIAMOND_LEGGINGS)
                .define('I', I)
                .unlockedBy("has_sturdy_sheet", InventoryChangeTrigger.TriggerInstance.hasItems(S))
                .save(output, rl("engineer_leggings"));

        // 靴子
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.ENGINEER_BOOTS.get())
                .pattern("SPS")
                .pattern("WBW")
                .define('S', S).define('P', P).define('W', W)
                .define('B', Items.DIAMOND_BOOTS)
                .unlockedBy("has_sturdy_sheet", InventoryChangeTrigger.TriggerInstance.hasItems(S))
                .save(output, rl("engineer_boots"));
    }

    private static ResourceLocation rl(String name) {
        return ResourceLocation.fromNamespaceAndPath(ProtectionEngineering.MODID, name);
    }
}
