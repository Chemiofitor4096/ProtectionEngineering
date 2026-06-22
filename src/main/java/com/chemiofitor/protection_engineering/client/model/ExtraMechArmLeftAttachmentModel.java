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

public class ExtraMechArmLeftAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "extra_mech_arm_left"), "main");
    private final ModelPart LeftArmArmorered;

    public ExtraMechArmLeftAttachmentModel(ModelPart root) {
        this.LeftArmArmorered = root.getChild("LeftArmArmorered");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition LeftArmArmorered = partdefinition.addOrReplaceChild("LeftArmArmorered", CubeListBuilder.create()
                .texOffs(34, 10).addBox(4.7412F, 0.0341F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(2.7412F, 0.0341F, -3.5F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(16, 7).addBox(0.7412F, 0.0341F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.7528F, 1.0654F, 0.0F, 0.0F, 0.0F, -0.0873F));

        LeftArmArmorered.addOrReplaceChild("r1", CubeListBuilder.create().texOffs(36, 32)
                .addBox(8.2412F, -23.4659F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(15.7996F, 24.2473F, 0.0F, 0.0F, 0.0F, -0.7854F));
        LeftArmArmorered.addOrReplaceChild("r2", CubeListBuilder.create().texOffs(36, 26)
                .addBox(12.2412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(32.0962F, 12.3862F, 0.0F, 0.0F, 0.0F, -1.5708F));
        LeftArmArmorered.addOrReplaceChild("r3", CubeListBuilder.create().texOffs(0, 0)
                .addBox(6.2412F, -23.4659F, -2.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(34.5378F, 7.1979F, 0.0F, 0.0F, 0.0F, -1.789F));
        LeftArmArmorered.addOrReplaceChild("r4", CubeListBuilder.create()
                .texOffs(34, 20).addBox(4.7412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 19).addBox(3.2412F, -25.9659F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(3.7412F, -24.4659F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(26.8478F, 23.3019F, 0.0F, 0.0F, 0.0F, -1.0036F));
        LeftArmArmorered.addOrReplaceChild("r5", CubeListBuilder.create().texOffs(0, 37)
                .addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(11.5971F, 9.1503F, 0.0F, 0.0F, 0.0F, -0.829F));
        LeftArmArmorered.addOrReplaceChild("r6", CubeListBuilder.create().texOffs(36, 36)
                .addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(8.2235F, 7.0011F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition LeftGear = LeftArmArmorered.addOrReplaceChild("LeftArmArmoreredGear",
                CubeListBuilder.create().texOffs(16, 17).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(4.2412F, 2.5341F, 0.0F));
        LeftGear.addOrReplaceChild("g1", CubeListBuilder.create().texOffs(34, 0)
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
        LeftGear.addOrReplaceChild("g2", CubeListBuilder.create().texOffs(18, 27)
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        LeftGear.addOrReplaceChild("g3", CubeListBuilder.create().texOffs(0, 27)
                .addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        LeftArmArmorered.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
