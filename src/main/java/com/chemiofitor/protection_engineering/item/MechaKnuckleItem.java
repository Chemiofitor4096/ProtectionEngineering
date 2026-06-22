package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.MechaKnuckleLeftAttachmentModel;
import com.chemiofitor.protection_engineering.client.model.MechaKnuckleRightAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
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
    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/mecha_knuckle.png");

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

    // ── 3D 渲染（双拳套）──────────────────────────────────

    @Override
    public EntityModel<?> createLeftArmModel(EntityModelSet modelSet) {
        return new MechaKnuckleLeftAttachmentModel<>(
                modelSet.bakeLayer(MechaKnuckleLeftAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getLeftArmTexture() { return TEX; }

    @Override
    public EntityModel<?> createRightArmModel(EntityModelSet modelSet) {
        return new MechaKnuckleRightAttachmentModel<>(
                modelSet.bakeLayer(MechaKnuckleRightAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getRightArmTexture() { return TEX; }
}
