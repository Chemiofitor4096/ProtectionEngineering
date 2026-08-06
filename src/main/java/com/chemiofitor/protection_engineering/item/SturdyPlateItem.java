package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 坚固防护板 —— 胸甲板附件，盔甲值 +2。
 */
public class SturdyPlateItem extends AttachmentItem {

    public SturdyPlateItem(Properties properties) {
        super(properties, SlotTypes.CHESTPLATE);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.sturdy_plate";
    }

    @Override
    public float getDamageReduction() { return 0.10f; }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(IAttachment.bonus("sturdy_plate_armor", Attributes.ARMOR, 2.0,
                AttributeModifier.Operation.ADD_VALUE));
    }
}
