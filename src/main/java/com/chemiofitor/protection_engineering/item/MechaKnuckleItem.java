package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * 机械拳套 —— 手臂附件，近战伤害 +20%。
 */
public class MechaKnuckleItem extends AttachmentItem {

    public MechaKnuckleItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(IAttachment.bonus("mecha_knuckle_damage", Attributes.ATTACK_DAMAGE, 0.2,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }
}
