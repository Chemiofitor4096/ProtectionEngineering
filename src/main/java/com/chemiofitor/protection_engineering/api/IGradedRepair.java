package com.chemiofitor.protection_engineering.api;

import com.chemiofitor.protection_engineering.registry.PERepairMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 分级修补 — 让同一件物品的不同修补材料拥有不同的修补强度。
 * <p>
 * 原版铁砧对任何合法修补材料都固定恢复 25% 耐久，无法区分材料等级。
 * 实现该接口的物品由 {@code PEAnvilEvents} 接管铁砧结果计算。
 * <p>
 * 强度以「单位」计量：{@link #FULL_REPAIR_UNITS} 个单位 = 满耐久。
 * 因此原版铁锭的等效强度为 5 单位（25%）。
 *
 * @see com.chemiofitor.protection_engineering.registry.PERepairMaterials
 */
public interface IGradedRepair {

    /** 修满耐久所需的单位数。1 单位 = 5% 最大耐久 */
    int FULL_REPAIR_UNITS = 20;

    /**
     * 返回指定材料对该物品的修补强度（单位）。
     *
     * @param toRepair 待修补物品
     * @param material 修补材料
     * @return 修补单位数，{@code <= 0} 表示该材料不可用于分级修补（回退原版逻辑）
     */
    int getRepairUnits(ItemStack toRepair, ItemStack material);

    /**
     * 单个材料恢复的耐久点数。
     *
     * @return 耐久点数，{@code <= 0} 表示不可修补
     */
    default int getRepairAmount(ItemStack toRepair, ItemStack material) {
        int units = getRepairUnits(toRepair, material);
        if (units <= 0) return 0;
        return Math.max(1, toRepair.getMaxDamage() * units / FULL_REPAIR_UNITS);
    }

    /** 每消耗一个材料的等级消耗。默认与原版一致（1 级/个） */
    default int getRepairLevelCost(ItemStack toRepair, ItemStack material) {
        return 1;
    }

    /**
     * 是否递增 {@code REPAIR_COST}（原版的「前置工作惩罚」）。
     * <p>
     * 原版靠它防止无限附魔叠加；对不可附魔的装备而言，它只会让装备修若干次后
     * 彻底无法修复，因此默认跟随可附魔性。
     */
    default boolean increasesRepairCost(ItemStack toRepair) {
        // 注意用 Item#isEnchantable 而非 ItemStack#isEnchantable：
        // 后者要求附魔列表为空，会让已附魔的装备反而免除惩罚
        return toRepair.getItem().isEnchantable(toRepair);
    }

    /**
     * 向 tooltip 追加「修补材料」清单，每种材料独占一行：
     * <pre>
     * 修补：
     *   黄铜板 15%
     *   坚固板 50%
     * </pre>
     * 缩进与附件清单一致（两个空格）。
     * <p>
     * 供实现类在 {@code appendHoverText} 中调用。配置未就绪、
     * 或该物品不接受任何已登记材料时，不输出任何行（含表头）。
     *
     * @param stack   被查看的物品
     * @param tooltip 目标 tooltip 列表
     */
    default void appendRepairTooltip(ItemStack stack, List<Component> tooltip) {
        List<PERepairMaterials.Entry> entries = PERepairMaterials.listEntries();
        if (entries.isEmpty()) return;

        List<Component> lines = new ArrayList<>();
        for (PERepairMaterials.Entry entry : entries) {
            if (getRepairUnits(stack, new ItemStack(entry.material())) <= 0) continue;

            MutableComponent line = Component.literal("  ");
            line.append(Component.translatable(entry.material().getDescriptionId())
                    .withStyle(ChatFormatting.GRAY));
            line.append(Component.literal(" " + Math.round(entry.ratio() * 100) + "%")
                    .withStyle(ChatFormatting.GREEN));
            lines.add(line);
        }
        if (lines.isEmpty()) return;   // 该物品不接受任何已登记材料，表头也不显示

        tooltip.add(Component.translatable("tooltip.protectionengineering.repair_materials")
                .withStyle(ChatFormatting.GOLD));
        tooltip.addAll(lines);
    }
}
