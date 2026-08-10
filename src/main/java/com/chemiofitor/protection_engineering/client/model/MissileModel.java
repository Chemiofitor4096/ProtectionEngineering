package com.chemiofitor.protection_engineering.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * 单兵制导导弹 3D 模型。
 * Blockbench 4.12.5 导出，适配 NeoForge 1.21.1。
 */
public class MissileModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "missile"), "main");

    private final ModelPart bb_main;

    public MissileModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
                .texOffs(12, 13).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 0).addBox(0.0F, -4.0F, -3.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -0.7383F, -1.5F, 3.0F, 15.0F, 3.0F, new CubeDeformation(-0.75F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(12, 0).addBox(0.0F, -3.5F, -3.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
