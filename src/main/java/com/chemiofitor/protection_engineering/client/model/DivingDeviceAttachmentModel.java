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

public class DivingDeviceAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "diving_device"), "main");
    private final ModelPart Device;

    public DivingDeviceAttachmentModel(ModelPart root) {
        this.Device = root.getChild("Device");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Device = partdefinition.addOrReplaceChild("Device", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -31.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(16, 13).addBox(-6.0F, -37.0F, -4.5F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        Device.addOrReplaceChild("AirFilter_r1", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-1.75F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(-5.0F, -23.5876F, -3.9244F, 0.48F, 0.0F, 0.0F));

        Device.addOrReplaceChild("AirFilter_r2", CubeListBuilder.create().texOffs(0, 13)
                .addBox(-2.0F, -1.5F, -1.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -23.375F, -3.25F, 0.48F, 0.0F, 0.0F));

        Device.addOrReplaceChild("AirFilter_r3", CubeListBuilder.create().texOffs(20, 13)
                .addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -25.1577F, -3.8191F, -0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Device.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
