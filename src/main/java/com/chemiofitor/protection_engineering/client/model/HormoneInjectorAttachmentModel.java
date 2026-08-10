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

public class HormoneInjectorAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "hormone_injector"), "main");
    private final ModelPart AttachGlass;

    public HormoneInjectorAttachmentModel(ModelPart root) {
        this.AttachGlass = root.getChild("AttachGlass");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition AttachGlass = partdefinition.addOrReplaceChild("AttachGlass", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(8, 16).addBox(-4.25F, -27.75F, -4.25F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-2.25F, -32.25F, -4.25F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(8, 20).addBox(-4.25F, -32.25F, -2.25F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 16).addBox(-3.0F, -31.5F, -5.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        AttachGlass.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 20)
                .addBox(-0.5F, -2.0F, -1.9142F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -26.5F, -1.75F, 0.0F, 1.5708F, -0.7854F));

        AttachGlass.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 16)
                .addBox(-1.0F, -5.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -26.5F, -1.75F, 0.0F, 1.5708F, 0.0F));

        AttachGlass.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 20)
                .addBox(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, -25.5F, -3.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        AttachGlass.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
