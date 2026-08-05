package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * 额外机械臂 —— 手臂附件，触及范围 +2。
 */
public class ExtraMechanicalArmItem extends AttachmentItem {

    private static final ResourceLocation REACH_ID =
            ProtectionEngineering.asResource("extra_mechanical_arm_reach");

    public ExtraMechanicalArmItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(
                new IAttachment.AttributeBonus(REACH_ID, Attributes.BLOCK_INTERACTION_RANGE, 2.0,
                        AttributeModifier.Operation.ADD_VALUE),
                new IAttachment.AttributeBonus(REACH_ID, Attributes.ENTITY_INTERACTION_RANGE, 2.0,
                        AttributeModifier.Operation.ADD_VALUE));
    }
}
