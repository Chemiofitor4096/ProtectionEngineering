package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IGradedRepair;
import com.chemiofitor.protection_engineering.registry.PERepairMaterials;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.List;

/**
 * 工程师盾牌 — 3×耐久，斧头破防禁用减半，防护范围 +20%。
 * <p>
 * 修补：仅分级材料（黄铜板/坚固板），见 {@link PERepairMaterials}。
 */
public class EngineerShieldItem extends Item implements Equipable, IGradedRepair {

    public static final int COOLDOWN_TICKS = 50; // 原版 100 的一半

    public EngineerShieldItem(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    // ── 分级修补 ────────────────────────────────────────────────

    @Override
    public int getRepairUnits(ItemStack toRepair, ItemStack material) {
        return PERepairMaterials.getUnits(material);
    }

    /** 仅接受分级材料 — 不再沿用原版盾牌的木板修补 */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return PERepairMaterials.contains(repair);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        appendRepairTooltip(stack, tooltip);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SHIELD_ACTIONS.contains(itemAbility);
    }
}
