package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IGradedRepair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

/**
 * 铁砧分级修补 — 接管 {@link IGradedRepair} 物品的铁砧结果计算。
 * <p>
 * 原版对任何合法修补材料都固定恢复 25% 耐久；此处按材料等级计算恢复量，
 * 并保留原版的「消耗多个材料直至修满」「重命名 +1 级」行为。
 * <p>
 * 未登记的材料组合不写入 output，交回原版处理（附魔书、同类合并等）。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID)
public class PEAnvilEvents {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!(left.getItem() instanceof IGradedRepair graded)) return;
        if (right.isEmpty() || !left.isDamageableItem()) return;

        int perItem = graded.getRepairAmount(left, right);
        if (perItem <= 0) return;                 // 该材料不参与分级修补
        if (left.getDamageValue() <= 0) return;   // 未损坏 → 交回原版（仅重命名）

        ItemStack output = left.copy();
        int consumed = 0;
        int levels = 0;

        while (output.getDamageValue() > 0 && consumed < right.getCount()) {
            output.setDamageValue(Math.max(0, output.getDamageValue() - perItem));
            levels += graded.getRepairLevelCost(left, right);
            consumed++;
        }
        if (consumed == 0) return;

        // 重命名（与原版一致：+1 级）
        String name = event.getName();
        if (name != null && !StringUtil.isBlank(name)) {
            if (!name.equals(left.getHoverName().getString())) {
                output.set(DataComponents.CUSTOM_NAME, Component.literal(name));
                levels++;
            }
        } else if (left.has(DataComponents.CUSTOM_NAME)) {
            output.remove(DataComponents.CUSTOM_NAME);
            levels++;
        }

        int priorCost = Math.max(left.getOrDefault(DataComponents.REPAIR_COST, 0),
                right.getOrDefault(DataComponents.REPAIR_COST, 0));

        // 前置工作惩罚：可附魔装备（链锯剑）跟随原版递增，
        // 不可附魔装备（护甲/盾牌）保持不变，否则修几次后会彻底无法修复
        if (graded.increasesRepairCost(left)) {
            output.set(DataComponents.REPAIR_COST, AnvilMenu.calculateIncreasedRepairCost(priorCost));
        }

        // 基础前置消耗 + 本次操作等级消耗（至少 1 级，否则结果不可取出）
        long cost = event.getCost()
                + left.getOrDefault(DataComponents.REPAIR_COST, 0)
                + right.getOrDefault(DataComponents.REPAIR_COST, 0)
                + levels;

        event.setOutput(output);
        event.setMaterialCost(consumed);
        event.setCost(Math.max(1L, cost));
    }
}
