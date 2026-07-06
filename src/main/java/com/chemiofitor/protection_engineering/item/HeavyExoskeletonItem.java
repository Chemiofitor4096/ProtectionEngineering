package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 重型外骨骼辅助设备 —— 腿部附件。
 * 护甲值 +2，可以走上 1 格高的方块，降低 20% 摔落伤害，免疫缓慢效果。
 */
public class HeavyExoskeletonItem extends AttachmentItem {

    private static final ResourceLocation ARMOR_ID =
            ProtectionEngineering.asResource("heavy_exo_armor");
    private static final ResourceLocation STEP_ID =
            ProtectionEngineering.asResource("heavy_exo_step");

    public HeavyExoskeletonItem(Properties properties) {
        super(properties, Set.of(MobEffects.MOVEMENT_SLOWDOWN), SlotTypes.LEG);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.heavy_exoskeleton";
    }

    /** 摔落伤害减免 20% */
    @Override
    public float getFallDamageReduction() { return 0.20f; }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ARMOR,
                new AttributeModifier(ARMOR_ID, 2.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.LEGS);
        event.replaceModifier(Attributes.STEP_HEIGHT,
                new AttributeModifier(STEP_ID, 0.4, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.LEGS);
    }
}
