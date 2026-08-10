package com.chemiofitor.protection_engineering.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Map;

/**
 * PE 护甲的客户端扩展：让原版 HumanoidArmorLayer 渲染一个全隐藏的模型（不显示贴片盔甲），
 * 实际 3D 模型由 {@link com.chemiofitor.protection_engineering.client.layer.PEArmorLayer} 负责。
 */
public enum PEClientExtensions implements IClientItemExtensions {
    INSTANCE;

    private HumanoidModel<?> emptyModel;

    private static final ModelPart EMPTY_PART = new ModelPart(List.of(), Map.of());

    private HumanoidModel<?> emptyModel() {
        if (emptyModel == null) {
            var root = createEmptyRoot();
            emptyModel = new HumanoidModel<>(root);
            emptyModel.head.visible = false;
            emptyModel.hat.visible = false;
            emptyModel.body.visible = false;
            emptyModel.rightArm.visible = false;
            emptyModel.leftArm.visible = false;
            emptyModel.rightLeg.visible = false;
            emptyModel.leftLeg.visible = false;
        }
        return emptyModel;
    }

    private static ModelPart createEmptyRoot() {
        return new ModelPart(List.of(), Map.of(
                "head",      EMPTY_PART,
                "hat",       EMPTY_PART,
                "body",      EMPTY_PART,
                "right_arm", EMPTY_PART,
                "left_arm",  EMPTY_PART,
                "right_leg", EMPTY_PART,
                "left_leg",  EMPTY_PART
        ));
    }

    @Override
    public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack,
                                                   EquipmentSlot slot, HumanoidModel<?> original) {
        return emptyModel();
    }
}
