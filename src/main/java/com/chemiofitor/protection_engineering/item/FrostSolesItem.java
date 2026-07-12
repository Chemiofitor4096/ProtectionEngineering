package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;

/**
 * 冰霜鞋底 — 脚部附件，水上行走时将脚下水面冻结为霜冰。
 */
public class FrostSolesItem extends AttachmentItem {

    private static final int RANGE = 3;

    public FrostSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.frost_soles";
    }

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        if (!(entity.level() instanceof ServerLevel level)) return;
        if (!entity.onGround()) return;

        BlockPos center = entity.blockPosition();
        for (int dx = -RANGE; dx <= RANGE; dx++) {
            for (int dz = -RANGE; dz <= RANGE; dz++) {
                if (dx * dx + dz * dz > RANGE * RANGE) continue;
                BlockPos pos = center.offset(dx, -1, dz);
                if (level.getBlockState(pos).is(Blocks.WATER)
                        && level.getBlockState(pos).getFluidState().isSource()) {
                    level.setBlock(pos, Blocks.FROSTED_ICE.defaultBlockState()
                            .setValue(FrostedIceBlock.AGE, 0), 3);
                }
            }
        }
    }
}
