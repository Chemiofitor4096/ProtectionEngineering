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

public class LightExoskeletonLeftModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "light_exoskeleton_left"), "main");
    private final ModelPart LeftMachineLeg;
    private final ModelPart LeftMachineUpperLeg;

    public LightExoskeletonLeftModel(ModelPart root) {
        this.LeftMachineLeg = root.getChild("LeftMachineLeg");
        this.LeftMachineUpperLeg = this.LeftMachineLeg.getChild("LeftMachineUpperLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition LeftMachineLeg = partdefinition.addOrReplaceChild("LeftMachineLeg", CubeListBuilder.create(),
                PartPose.offset(4.5F, 8.25F, 0.75F));

        LeftMachineLeg.addOrReplaceChild("LeftMachineLeg_r1", CubeListBuilder.create().texOffs(0, 9)
                .addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 5.0F, -0.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition LeftMachineUpperLeg = LeftMachineLeg.addOrReplaceChild("LeftMachineUpperLeg", CubeListBuilder.create()
                .texOffs(0, 21).addBox(-1.5F, 4.3301F, 1.5F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 21).addBox(0.5F, 4.3301F, 1.5F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 0.0F, -0.5F, -0.5236F, 0.0F, 0.0F));

        LeftMachineUpperLeg.addOrReplaceChild("LeftMachineFoot_r1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(0.0F, -0.0335F, 1.192F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-6.5F, 16.1388F, 2.7101F, 0.5236F, 0.0F, 0.0F));

        LeftMachineUpperLeg.addOrReplaceChild("LeftMachineFoot_r2", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-1.998F, -0.5F, 2.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-4.502F, 14.603F, -0.4756F, -0.2618F, 0.0F, 0.0F));

        LeftMachineUpperLeg.addOrReplaceChild("LeftMachineUpperLeg_r1", CubeListBuilder.create()
                .texOffs(18, 9).addBox(-0.5F, -1.0F, 1.5F, 1.0F, 2.0F, 4.0F, new CubeDeformation(-0.25F))
                .texOffs(20, 21).addBox(-0.5F, -1.0F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 21).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 9.3301F, 2.5F, -0.6545F, 0.0F, 0.0F));

        LeftMachineUpperLeg.addOrReplaceChild("LeftMachineUpperLeg_r2", CubeListBuilder.create()
                .texOffs(22, 15).addBox(11.25F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(18, 15).addBox(13.75F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F)),
                PartPose.offsetAndRotation(-13.0F, 9.3301F, 2.5F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        LeftMachineLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
