package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.LightExoskeletonLeftModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 轻型外骨骼辅助设备 —— 腿部附件。
 * 移动速度 +10%，跳跃高度 +0.5 格，可以走上 1 格高的方块，免疫缓慢效果。
 */
public class LightExoskeletonItem extends AttachmentItem {

    private static final ResourceLocation SPEED_ID =
            ProtectionEngineering.asResource("light_exo_speed");
    private static final ResourceLocation JUMP_ID =
            ProtectionEngineering.asResource("light_exo_jump");
    private static final ResourceLocation STEP_ID =
            ProtectionEngineering.asResource("light_exo_step");
    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/light_exoskeleton.png");

    public LightExoskeletonItem(Properties properties) {
        super(properties, Set.of(MobEffects.MOVEMENT_SLOWDOWN), SlotTypes.LEG);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.light_exoskeleton";
    }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(SPEED_ID, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                EquipmentSlotGroup.LEGS);
        event.replaceModifier(Attributes.JUMP_STRENGTH,
                new AttributeModifier(JUMP_ID, 0.12, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.LEGS);
        event.replaceModifier(Attributes.STEP_HEIGHT,
                new AttributeModifier(STEP_ID, 0.4, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.LEGS);
    }

    // ── 3D 渲染（共用左腿模型，右腿由渲染层 X 轴镜像）───────

    @Override
    public EntityModel<?> createLeftLegModel(EntityModelSet modelSet) {
        return new LightExoskeletonLeftModel<>(
                modelSet.bakeLayer(LightExoskeletonLeftModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getLeftLegTexture() { return TEX; }
}
