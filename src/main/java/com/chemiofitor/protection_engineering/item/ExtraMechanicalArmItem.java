package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

import java.util.List;

/**
 * 额外机械臂 —— 手臂附件，触及范围 +2。
 * <p>
 * 1.20.1 无 1.21 的 BLOCK/ENTITY_INTERACTION_RANGE 属性，
 * 等价物为 ForgeMod 的 BLOCK_REACH（方块交互距离）与 ENTITY_REACH（实体攻击距离），
 * 均已注册进 Player 的 AttributeSupplier，由 IForgePlayer#canReach / 横扫判定消费。
 */
public class ExtraMechanicalArmItem extends AttachmentItem {

    public ExtraMechanicalArmItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    public List<AttributeBonus> getAttributeBonuses() {
        return List.of(
                IAttachment.bonus("extra_mechanical_arm_reach", ForgeMod.BLOCK_REACH.get(), 2.0,
                        AttributeModifier.Operation.ADDITION),
                IAttachment.bonus("extra_mechanical_arm_reach", ForgeMod.ENTITY_REACH.get(), 2.0,
                        AttributeModifier.Operation.ADDITION));
    }
}
