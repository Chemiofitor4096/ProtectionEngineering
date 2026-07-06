package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import javax.annotation.Nullable;

import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * 机械拳套 —— 手臂附件，近战伤害 +20%。
 */
public class MechaKnuckleItem extends AttachmentItem {

    private static final ResourceLocation DAMAGE_ID =
            ProtectionEngineering.asResource("mecha_knuckle_damage");

    public MechaKnuckleItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.mecha_knuckle";
    }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(DAMAGE_ID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                EquipmentSlotGroup.CHEST);
    }
}
