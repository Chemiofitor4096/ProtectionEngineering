package com.chemiofitor.protection_engineering.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * 单兵制导导弹 —— 消耗品，供便携式导弹背包发射。
 * <p>
 * 激活导弹背包时从物品栏中消耗一枚。
 */
public class MissileItem extends Item {

    public MissileItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.protectionengineering.missile_item")
                .withStyle(ChatFormatting.GRAY));
    }
}
