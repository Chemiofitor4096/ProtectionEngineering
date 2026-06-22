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

public class ExtraMechArmRightAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "extra_mech_arm_right"), "main");
    private final ModelPart RightArmArmorered;

    public ExtraMechArmRightAttachmentModel(ModelPart root) {
        this.RightArmArmorered = root.getChild("RightArmArmorered");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightArmArmorered = partdefinition.addOrReplaceChild("RightArmArmorered", CubeListBuilder.create()
                .texOffs(34, 10).mirror().addBox(-6.7412F, 0.0341F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 7).mirror().addBox(-3.7412F, 0.0341F, -3.5F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 7).mirror().addBox(-2.7412F, 0.0341F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-4.7528F, 1.0654F, 0.0F, 0.0F, 0.0F, 0.0873F));

        RightArmArmorered.addOrReplaceChild("r1", CubeListBuilder.create().texOffs(36, 32).mirror()
                .addBox(-13.2412F, -23.4659F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-15.7996F, 24.2473F, 0.0F, 0.0F, 0.0F, 0.7854F));
        RightArmArmorered.addOrReplaceChild("r2", CubeListBuilder.create().texOffs(36, 26).mirror()
                .addBox(-15.2412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-32.0962F, 12.3862F, 0.0F, 0.0F, 0.0F, 1.5708F));
        RightArmArmorered.addOrReplaceChild("r3", CubeListBuilder.create().texOffs(0, 0).mirror()
                .addBox(-14.2412F, -23.4659F, -2.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-34.5378F, 7.1979F, 0.0F, 0.0F, 0.0F, 1.789F));
        RightArmArmorered.addOrReplaceChild("r4", CubeListBuilder.create()
                .texOffs(34, 20).mirror().addBox(-7.7412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(10, 19).mirror().addBox(-4.2412F, -25.9659F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 19).mirror().addBox(-4.7412F, -24.4659F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-26.8478F, 23.3019F, 0.0F, 0.0F, 0.0F, 1.0036F));
        RightArmArmorered.addOrReplaceChild("r5", CubeListBuilder.create().texOffs(0, 37).mirror()
                .addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-11.5971F, 9.1503F, 0.0F, 0.0F, 0.0F, 0.829F));
        RightArmArmorered.addOrReplaceChild("r6", CubeListBuilder.create().texOffs(36, 36).mirror()
                .addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-8.2235F, 7.0011F, 0.0F, 0.0F, 0.0F, 1.1781F));

        PartDefinition RightGear = RightArmArmorered.addOrReplaceChild("RightArmArmoreredGear",
                CubeListBuilder.create().texOffs(16, 17).mirror().addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(-4.2412F, 2.5341F, 0.0F));
        RightGear.addOrReplaceChild("g1", CubeListBuilder.create().texOffs(34, 0).mirror()
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
        RightGear.addOrReplaceChild("g2", CubeListBuilder.create().texOffs(18, 27).mirror()
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        RightGear.addOrReplaceChild("g3", CubeListBuilder.create().texOffs(0, 27).mirror()
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        RightArmArmorered.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
