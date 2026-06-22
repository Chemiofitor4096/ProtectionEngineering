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

public class DodgeJetpackAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "dodge_jetpack"), "main");
    private final ModelPart DodgePack;
    private final ModelPart LeftWing;
    private final ModelPart LeftOuterWing;
    private final ModelPart RightWing;
    private final ModelPart RightOuterWing;
    private final ModelPart LeftLowerWing;
    private final ModelPart RightLowerWing;
    private final ModelPart MainPack;

    public DodgeJetpackAttachmentModel(ModelPart root) {
        this.DodgePack = root.getChild("DodgePack");
        this.LeftWing = this.DodgePack.getChild("LeftWing");
        this.LeftOuterWing = this.LeftWing.getChild("LeftOuterWing");
        this.RightWing = this.DodgePack.getChild("RightWing");
        this.RightOuterWing = this.RightWing.getChild("RightOuterWing");
        this.LeftLowerWing = this.DodgePack.getChild("LeftLowerWing");
        this.RightLowerWing = this.DodgePack.getChild("RightLowerWing");
        this.MainPack = this.DodgePack.getChild("MainPack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition DodgePack = partdefinition.addOrReplaceChild("DodgePack", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition LeftWing = DodgePack.addOrReplaceChild("LeftWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.0F, -21.5F, 4.0F, 0.0F, -0.3491F, -0.0873F));

        LeftWing.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 25)
                .addBox(-5.0F, -1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.75F, -0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition LeftOuterWing = LeftWing.addOrReplaceChild("LeftOuterWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.75F, -0.25F, -0.5F, -0.0305F, -0.2193F, -0.0754F));

        LeftOuterWing.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 4)
                .addBox(0.5F, 0.0F, 0.0F, 13.0F, 1.0F, 1.0F, new CubeDeformation(-0.002F))
                .texOffs(0, 2).addBox(0.5F, -1.0F, 0.0F, 18.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.5F, -2.0F, 0.0F, 19.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-0.5F, -3.0F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.1745F));

        LeftOuterWing.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 26)
                .addBox(0.5F, 0.25F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(22, 10).addBox(0.5F, -0.75F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 1.3526F));

        LeftOuterWing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 19)
                .addBox(0.5F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(18, 12).addBox(0.5F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.7418F));

        LeftOuterWing.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 10)
                .addBox(0.5F, 0.25F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 6).addBox(0.5F, -0.75F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1745F));

        LeftOuterWing.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 21)
                .addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.6109F));

        PartDefinition RightWing = DodgePack.addOrReplaceChild("RightWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.0F, -21.5F, 4.0F, 0.0F, 0.3491F, 0.0873F));

        RightWing.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 25)
                .addBox(1.0F, -1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.75F, -0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition RightOuterWing = RightWing.addOrReplaceChild("RightOuterWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.75F, -0.25F, -0.5F, -0.0305F, 0.2193F, 0.0754F));

        RightOuterWing.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 4)
                .addBox(-13.5F, 0.0F, 0.0F, 13.0F, 1.0F, 1.0F, new CubeDeformation(-0.002F))
                .texOffs(0, 2).addBox(-18.5F, -1.0F, 0.0F, 18.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-19.5F, -2.0F, 0.0F, 19.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-5.5F, -3.0F, -1.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1745F));

        RightOuterWing.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 26)
                .addBox(-3.5F, 0.25F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(22, 10).addBox(-5.5F, -0.75F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -1.3526F));

        RightOuterWing.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(12, 19)
                .addBox(-6.5F, 0.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(18, 12).addBox(-8.5F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.7418F));

        RightOuterWing.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 10)
                .addBox(-10.5F, 0.25F, 0.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 6).addBox(-12.5F, -0.75F, 0.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.1745F));

        RightOuterWing.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(12, 21)
                .addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.6109F));

        PartDefinition LeftLowerWing = DodgePack.addOrReplaceChild("LeftLowerWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.8707F, -16.8009F, 3.6473F, -0.044F, -0.1308F, 0.0057F));

        LeftLowerWing.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(20, 16)
                .addBox(-0.5F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 14).addBox(-0.5F, -1.0F, -0.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1104F, -0.1886F, 1.3194F));

        LeftLowerWing.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-0.5F, -0.25F, -0.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 8).addBox(-0.5F, -1.25F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2182F, 0.7854F));

        LeftLowerWing.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(22, 21)
                .addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0846F, -0.2013F, 1.1867F));

        PartDefinition RightLowerWing = DodgePack.addOrReplaceChild("RightLowerWing", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.8707F, -16.8009F, 3.6473F, -0.044F, 0.1308F, -0.0057F));

        RightLowerWing.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(20, 16)
                .addBox(-5.5F, 0.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 14).addBox(-7.5F, -1.0F, -0.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1104F, 0.1886F, -1.3194F));

        RightLowerWing.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 17)
                .addBox(-8.5F, -0.25F, -0.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(-0.001F))
                .texOffs(0, 8).addBox(-10.5F, -1.25F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2182F, -0.7854F));

        RightLowerWing.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(22, 21)
                .addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0846F, 0.2013F, -1.1867F));

        PartDefinition MainPack = DodgePack.addOrReplaceChild("MainPack", CubeListBuilder.create(),
                PartPose.offset(3.75F, -22.0F, 3.0F));

        MainPack.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(26, 6)
                .addBox(-3.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.5496F, 3.1281F, -0.002F, 0.0F, 0.2182F, -0.7854F));

        MainPack.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(16, 28)
                .addBox(-1.0F, -1.0F, -0.502F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-6.5496F, 3.1281F, 0.0F, 0.0F, 0.0F, -0.7854F));

        MainPack.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 28)
                .addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-7.3201F, -1.8491F, -0.0176F, 0.0152F, 0.0859F, 0.1752F));

        MainPack.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(24, 26)
                .addBox(-0.5F, -3.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.5436F, 0.0F, 0.0019F, 0.0F, 0.0873F, 0.0F));

        MainPack.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 19)
                .addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.5F, 0.0F, 0.5F, -0.0618F, 0.0617F, -0.7873F));

        MainPack.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(10, 26)
                .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-7.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        MainPack.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(26, 6)
                .addBox(0.0F, -1.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.9504F, 3.1281F, -0.002F, 0.0F, -0.2182F, 0.7854F));

        MainPack.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(16, 28)
                .addBox(-1.0F, -1.0F, -0.502F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.9504F, 3.1281F, 0.0F, 0.0F, 0.0F, 0.7854F));

        MainPack.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 28)
                .addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-0.1799F, -1.8491F, -0.0176F, 0.0152F, -0.0859F, -0.1752F));

        MainPack.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(24, 26)
                .addBox(-1.5F, -3.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0436F, 0.0F, 0.0019F, 0.0F, -0.0873F, 0.0F));

        MainPack.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 19)
                .addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -0.0618F, -0.0617F, 0.7873F));

        MainPack.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(10, 26)
                .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        DodgePack.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
