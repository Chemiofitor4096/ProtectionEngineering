package com.chemiofitor.protection_engineering.event;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IGradedRepair;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 铁砧分级修补 — 接管 {@link IGradedRepair} 物品的铁砧结果计算。
 * <p>
 * 原版对任何合法修补材料都固定恢复 25% 耐久；此处按材料等级计算恢复量，
 * 并保留原版的「消耗多个材料直至修满」「重命名 +1 级」行为。
 * <p>
 * 未登记的材料组合不写入 output，交回原版处理（附魔书、同类合并等）。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID)
public class PEAnvilEvents {

    /** 1.20.1 的 REPAIR_COST 存于 NBT 的 RepairCost 键 */
    private static int repairCost(ItemStack stack) {
        return stack.getTag() != null ? stack.getTag().getInt("RepairCost") : 0;
    }

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
        if (name != null && !StringUtil.isNullOrEmpty(name)) {
            if (!name.equals(left.getHoverName().getString())) {
                output.setHoverName(Component.literal(name));
                levels++;
            }
        } else if (left.hasCustomHoverName()) {
            output.resetHoverName();
            levels++;
        }

        int priorCost = Math.max(repairCost(left), repairCost(right));

        // 前置工作惩罚：可附魔装备（链锯剑）跟随原版递增，
        // 不可附魔装备（护甲/盾牌）保持不变，否则修几次后会彻底无法修复
        if (graded.increasesRepairCost(left)) {
            output.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(priorCost));
        }

        // 基础前置消耗 + 本次操作等级消耗（至少 1 级，否则结果不可取出）
        int cost = event.getCost()
                + repairCost(left)
                + repairCost(right)
                + levels;

        event.setOutput(output);
        event.setMaterialCost(consumed);
        event.setCost(Math.max(1, cost));
    }
}
