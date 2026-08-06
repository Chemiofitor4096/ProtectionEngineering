package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * 额外机械臂 —— 手臂附件，触及范围 +2。
 */
public class ExtraMechanicalArmItem extends AttachmentItem {

    public ExtraMechanicalArmItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(
                IAttachment.bonus("extra_mechanical_arm_reach", Attributes.BLOCK_INTERACTION_RANGE, 2.0,
                        AttributeModifier.Operation.ADD_VALUE),
                IAttachment.bonus("extra_mechanical_arm_reach", Attributes.ENTITY_INTERACTION_RANGE, 2.0,
                        AttributeModifier.Operation.ADD_VALUE));
    }
}
