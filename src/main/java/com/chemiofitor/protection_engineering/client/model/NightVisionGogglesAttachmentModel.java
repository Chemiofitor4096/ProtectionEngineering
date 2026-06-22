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

public class NightVisionGogglesAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "night_vision_goggles"), "main");
    private final ModelPart AttachGlass;

    public NightVisionGogglesAttachmentModel(ModelPart root) {
        this.AttachGlass = root.getChild("AttachGlass");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition AttachGlass = partdefinition.addOrReplaceChild("AttachGlass", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-4.0F, -32.0F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(24, 24).addBox(-4.25F, -27.75F, -4.25F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-2.25F, -32.25F, -4.25F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-4.25F, -32.25F, -2.25F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        AttachGlass.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(32, 4).addBox(0.0F, 0.125F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(32, 2).addBox(-1.125F, -1.0F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(32, 0).addBox(0.0F, -1.0F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 28).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.75F, -27.25F, -4.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        AttachGlass.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
