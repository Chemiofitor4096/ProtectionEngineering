package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 下界合金防护板 —— 胸甲板附件，盔甲值 +4，减伤 15%。
 */
public class NetheritePlateItem extends AttachmentItem {

    public NetheritePlateItem(Properties properties) {
        super(properties, SlotTypes.CHESTPLATE);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.netherite_plate";
    }

    @Override
    public float getDamageReduction() { return 0.15f; }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(IAttachment.bonus("netherite_plate_armor", Attributes.ARMOR, 4.0,
                AttributeModifier.Operation.ADDITION));
    }
}
