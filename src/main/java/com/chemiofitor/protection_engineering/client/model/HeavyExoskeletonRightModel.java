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

public class HeavyExoskeletonRightModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "heavy_exoskeleton_right"), "main");
    private final ModelPart MachineLeg;
    private final ModelPart LeftMachineLeg;
    private final ModelPart LeftMachineUpperLeg;
    private final ModelPart LeftMachineLowerLeg;
    private final ModelPart LeftMachineLowestLeg;
    private final ModelPart LeftMachineFoot;
    private final ModelPart LeftMachineLowestLegRes;
    private final ModelPart LeftMachineLowerLegPush;

    public HeavyExoskeletonRightModel(ModelPart root) {
        this.MachineLeg = root.getChild("MachineLeg");
        this.LeftMachineLeg = this.MachineLeg.getChild("LeftMachineLeg");
        this.LeftMachineUpperLeg = this.LeftMachineLeg.getChild("LeftMachineUpperLeg");
        this.LeftMachineLowerLeg = this.LeftMachineUpperLeg.getChild("LeftMachineLowerLeg");
        this.LeftMachineLowestLeg = this.LeftMachineLowerLeg.getChild("LeftMachineLowestLeg");
        this.LeftMachineFoot = this.LeftMachineLowestLeg.getChild("LeftMachineFoot");
        this.LeftMachineLowestLegRes = this.LeftMachineLowestLeg.getChild("LeftMachineLowestLegRes");
        this.LeftMachineLowerLegPush = this.LeftMachineLowerLeg.getChild("LeftMachineLowerLegPush");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MachineLeg = partdefinition.addOrReplaceChild("MachineLeg", CubeListBuilder.create(),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition LeftMachineLeg = MachineLeg.addOrReplaceChild("LeftMachineLeg", CubeListBuilder.create(),
                PartPose.offset(4.5F, 0.25F, 0.75F));

        LeftMachineLeg.addOrReplaceChild("LeftMachineLeg_r1", CubeListBuilder.create().texOffs(0, 21)
                .addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 5.0F, -0.5F, -0.5236F, 0.0F, 0.0F));

        PartDefinition LeftMachineUpperLeg = LeftMachineLeg.addOrReplaceChild("LeftMachineUpperLeg", CubeListBuilder.create()
                .texOffs(12, 33).addBox(-1.5F, 4.3301F, 1.5F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 33).addBox(0.5F, 4.3301F, 1.5F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 0.0F, -0.5F, -0.5236F, 0.0F, 0.0F));

        LeftMachineUpperLeg.addOrReplaceChild("LeftMachineUpperLeg_r1", CubeListBuilder.create()
                .texOffs(24, 36).addBox(11.25F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(34, 6).addBox(13.75F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F)),
                PartPose.offsetAndRotation(-13.0F, 9.3301F, 2.5F, 0.7854F, 0.0F, 0.0F));

        PartDefinition LeftMachineLowerLeg = LeftMachineUpperLeg.addOrReplaceChild("LeftMachineLowerLeg", CubeListBuilder.create()
                .texOffs(0, 9).addBox(-0.5F, 2.2835F, -0.942F, 1.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 6.7165F, -0.058F, 0.3927F, 0.0F, 0.0F));

        PartDefinition LeftMachineLowestLeg = LeftMachineLowerLeg.addOrReplaceChild("LeftMachineLowestLeg", CubeListBuilder.create()
                .texOffs(0, 33).addBox(0.5F, 3.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 36).addBox(0.5F, 7.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 33).addBox(-1.5F, 3.0F, -1.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 38).addBox(-1.5F, 7.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, -0.1309F, 0.0F, 0.0F));

        LeftMachineLowestLeg.addOrReplaceChild("LeftMachineLowestLeg_r1", CubeListBuilder.create()
                .texOffs(34, 3).addBox(-1.498F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F))
                .texOffs(34, 0).addBox(0.502F, -0.5F, -0.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-0.002F, 8.0F, -1.2929F, -0.7854F, 0.0F, 0.0F));

        LeftMachineLowestLeg.addOrReplaceChild("LeftMachineLowestLeg_r2", CubeListBuilder.create()
                .texOffs(24, 29).addBox(-0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(-0.25F))
                .texOffs(24, 38).addBox(-0.5F, -0.5F, -3.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(38, 6).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 7.5F, -1.5F, -0.7854F, 0.0F, 0.0F));

        LeftMachineLowestLeg.addOrReplaceChild("LeftMachineLowestLeg_r3", CubeListBuilder.create()
                .texOffs(32, 36).addBox(11.25F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F))
                .texOffs(28, 36).addBox(13.75F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.125F)),
                PartPose.offsetAndRotation(-13.0F, 4.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition LeftMachineFoot = LeftMachineLowestLeg.addOrReplaceChild("LeftMachineFoot", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-6.5F, 1.0F, -5.0F, 9.0F, 1.0F, 8.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        LeftMachineFoot.addOrReplaceChild("LeftMachineFoot_r1", CubeListBuilder.create()
                .texOffs(0, 29).addBox(-1.998F, -0.5F, 2.0F, 9.0F, 1.0F, 3.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(-4.502F, -1.8895F, -8.183F, -0.7854F, 0.0F, 0.0F));

        LeftMachineLowestLeg.addOrReplaceChild("LeftMachineLowestLegRes", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 4.5F, -1.5F, -0.3927F, 0.0F, 0.0F));

        LeftMachineLowerLeg.addOrReplaceChild("LeftMachineLowerLegPush", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 1.1005F, -1.509F, 1.0472F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        MachineLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
