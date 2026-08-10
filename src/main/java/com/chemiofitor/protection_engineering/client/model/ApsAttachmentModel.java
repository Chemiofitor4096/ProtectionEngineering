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

public class ApsAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "aps"), "main");
    private final ModelPart APS;

    public ApsAttachmentModel(ModelPart root) {
        this.APS = root.getChild("APS");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition APS = partdefinition.addOrReplaceChild("APS", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        APS.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(0, 8).addBox(-4.0F, -2.5F, -2.0F, 7.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-8.0F, -26.5F, 1.0F, -0.4363F, 0.0F, -0.2618F));

        APS.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -2.5F, -2.0F, 7.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(8.0F, -26.5F, 1.0F, -0.4363F, 0.0F, 0.2618F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        APS.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
