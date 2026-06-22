package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.ExtraMechArmLeftAttachmentModel;
import com.chemiofitor.protection_engineering.client.model.ExtraMechArmRightAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import javax.annotation.Nullable;

import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * 额外机械臂 —— 手臂附件，触及范围 +2。
 */
public class ExtraMechanicalArmItem extends AttachmentItem {

    private static final ResourceLocation REACH_ID =
            ProtectionEngineering.asResource("extra_mechanical_arm_reach");
    private static final ResourceLocation ARM_TEX =
            ProtectionEngineering.asResource("textures/models/armor/extra_mechanical_arm.png");

    public ExtraMechanicalArmItem(Properties properties) {
        super(properties, SlotTypes.ARM);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.extra_mechanical_arm";
    }

    // ── 3D 渲染（双机械臂）──────────────────────────────────

    @Override
    public EntityModel<?> createLeftArmModel(EntityModelSet modelSet) {
        return new ExtraMechArmLeftAttachmentModel<>(
                modelSet.bakeLayer(ExtraMechArmLeftAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getLeftArmTexture() { return ARM_TEX; }

    @Override
    public EntityModel<?> createRightArmModel(EntityModelSet modelSet) {
        return new ExtraMechArmRightAttachmentModel<>(
                modelSet.bakeLayer(ExtraMechArmRightAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getRightArmTexture() { return ARM_TEX; }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        var modifier = new AttributeModifier(REACH_ID, 2.0, AttributeModifier.Operation.ADD_VALUE);
        event.replaceModifier(Attributes.BLOCK_INTERACTION_RANGE, modifier,
                EquipmentSlotGroup.CHEST);
        event.replaceModifier(Attributes.ENTITY_INTERACTION_RANGE, modifier,
                EquipmentSlotGroup.CHEST);
    }
}
