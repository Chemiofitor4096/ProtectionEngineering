package com.chemiofitor.protection_engineering.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Optional;

/**
 * 工程师链锯剑 — 等同钻石剑伤害/攻速，下界合金耐久。
 * 攻击冷却未满也能横扫，可砍树（斧头工具），兼容剑+斧附魔。
 */
public class EngineerSawSwordItem extends SwordItem {

    /** 与钻石剑相同的属性，耐久用下界合金 */
    public static final Tier TIER = new Tier() {
        @Override public int getUses() { return 2031; }            // 下界合金耐久
        @Override public float getSpeed() { return 8.0F; }         // 钻石工具挖掘速度
        @Override public float getAttackDamageBonus() { return 3.0F; } // 钻石剑 +3
        @Override public int getEnchantmentValue() { return 15; }  // 钻石附魔能力
        @Override public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
            return net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }
        @Override public net.minecraft.world.item.crafting.Ingredient getRepairIngredient() {
            return net.minecraft.world.item.crafting.Ingredient.of(Items.DIAMOND);
        }
    };

    public EngineerSawSwordItem(Properties properties) {
        super(TIER, properties.attributes(SwordItem.createAttributes(TIER, 3.0F, -2.4F)));
    }

    // ── 视为斧头（砍树） ──────────────────────────────────────

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE)
                || super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE)) {
            return TIER.getSpeed();
        }
        return super.getDestroySpeed(stack, state);
    }

    // ── 斧头动作：useOn 剥皮/刮铜/去蜡 ────────────────────────

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        Optional<BlockState> stripped = Optional.ofNullable(
                state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false));
        if (stripped.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            stripped = Optional.ofNullable(
                    state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false));
            if (stripped.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0);
            } else {
                stripped = Optional.ofNullable(
                        state.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false));
                if (stripped.isPresent()) {
                    level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3004, pos, 0);
                }
            }
        }

        if (stripped.isPresent()) {
            if (player instanceof ServerPlayer sp) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(sp, pos, context.getItemInHand());
            }
            level.setBlock(pos, stripped.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, stripped.get()));
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player,
                        LivingEntity.getSlotForHand(context.getHand()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(ability)
                || ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(ability);
    }

    // ── 兼容剑+斧附魔 ─────────────────────────────────────────

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

}
