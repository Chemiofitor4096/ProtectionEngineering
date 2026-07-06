package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;

/**
 * 下界合金防护板 —— 胸甲板附件，盔甲值 +4，减伤 15%。
 */
public class NetheritePlateItem extends AttachmentItem {

    private static final ResourceLocation ARMOR_ID =
            ProtectionEngineering.asResource("netherite_plate_armor");

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
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ARMOR,
                new AttributeModifier(ARMOR_ID, 4.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.CHEST);
    }
}
