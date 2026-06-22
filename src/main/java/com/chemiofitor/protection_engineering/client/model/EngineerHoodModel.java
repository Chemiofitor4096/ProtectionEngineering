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
import net.minecraft.world.entity.LivingEntity;



public class EngineerHoodModel<T extends LivingEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("protectionengineering", "engineer_hood"), "main");
	private final ModelPart Hood;

	public EngineerHoodModel(ModelPart root) {
		this.Hood = root.getChild("Hood");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Hood = partdefinition.addOrReplaceChild("Hood", CubeListBuilder.create().texOffs(0, 10).addBox(-4.0F, -32.0F, 3.75F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = Hood.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(34, 9).addBox(-3.0F, 1.0F, -0.5F, 7.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -34.4749F, 1.818F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Hood.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 9).addBox(-1.25F, -2.0F, -1.25F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(-2.25F, -2.0F, 0.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 51).addBox(-3.25F, -2.0F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 36).addBox(0.75F, -2.0F, 3.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3097F, -29.9666F, 4.2103F, -0.8414F, -0.1555F, 0.8795F));

		PartDefinition cube_r3 = Hood.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(34, 0).addBox(-0.5F, -1.5F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.1043F, -23.6827F, -1.0212F, 3.134F, 0.043F, -1.0474F));

		PartDefinition cube_r4 = Hood.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 25).addBox(-3.9162F, -0.3635F, -2.498F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -32.6988F, -4.1254F, 0.3463F, -0.0447F, 0.1231F));

		PartDefinition cube_r5 = Hood.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 24).addBox(-2.0F, -0.5F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2248F, -31.3511F, -3.8086F, 0.0144F, 0.2358F, -1.1664F));

		PartDefinition cube_r6 = Hood.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 41).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.8417F, -26.9429F, -3.0551F, 2.9233F, 0.0433F, 3.1359F));

		PartDefinition cube_r7 = Hood.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 19).addBox(-0.25F, -0.125F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9005F, -33.0618F, -4.2227F, 0.0144F, -0.2358F, 1.1664F));

		PartDefinition cube_r8 = Hood.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(18, 18).addBox(-0.0838F, -0.3635F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -32.6988F, -4.1254F, 0.3463F, 0.0447F, -0.1231F));

		PartDefinition cube_r9 = Hood.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(22, 39).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8417F, -26.9429F, -3.0551F, 2.9233F, -0.0433F, -3.1359F));

		PartDefinition cube_r10 = Hood.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(44, 29).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9333F, -33.6217F, 0.0F, -3.1416F, 0.0F, -1.6581F));

		PartDefinition cube_r11 = Hood.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(52, 43).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.8817F, -33.6223F, 3.2161F, 3.0289F, -0.1468F, -1.7456F));

		PartDefinition cube_r12 = Hood.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 37).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8817F, -33.6223F, 3.2161F, 3.0289F, 0.1468F, 1.7456F));

		PartDefinition cube_r13 = Hood.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 42).addBox(-1.504F, 0.0F, 0.0F, 3.004F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.002F, -29.5796F, 8.7201F, -1.0908F, 0.0F, 0.0F));

		PartDefinition cube_r14 = Hood.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(12, 38).addBox(1.25F, -2.0F, 0.75F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 51).addBox(1.25F, -2.0F, -1.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 0).addBox(-1.75F, -2.0F, -1.25F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(46, 21).addBox(-1.75F, -2.0F, 3.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3097F, -29.9666F, 4.2103F, -0.8414F, 0.1555F, -0.8795F));

		PartDefinition cube_r15 = Hood.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-1.508F, 0.0F, 0.0F, 3.008F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.004F, -34.1759F, 4.8634F, -0.8727F, 0.0F, 0.0F));

		PartDefinition cube_r16 = Hood.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(36, 23).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -33.4455F, 2.75F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r17 = Hood.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(42, 41).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9333F, -33.6217F, 0.002F, 3.1416F, 0.0F, 1.6581F));

		PartDefinition cube_r18 = Hood.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 50).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8779F, -31.7416F, 3.3799F, 3.1262F, -0.1739F, -2.6603F));

		PartDefinition cube_r19 = Hood.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(42, 49).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8779F, -31.7416F, 3.3799F, 3.1262F, 0.1739F, 2.6603F));

		PartDefinition cube_r20 = Hood.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(48, 0).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2727F, -31.9064F, -0.002F, -3.1416F, 0.0F, -2.7489F));

		PartDefinition cube_r21 = Hood.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(46, 13).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2727F, -31.9064F, -0.002F, 3.1416F, 0.0F, 2.7489F));

		PartDefinition cube_r22 = Hood.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(50, 49).addBox(-1.25F, 0.0F, -2.625F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2664F, -23.4653F, 4.8489F, -0.7655F, -0.5115F, 1.0864F));

		PartDefinition cube_r23 = Hood.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(50, 21).addBox(0.25F, 0.0F, -2.625F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2664F, -23.4653F, 4.8489F, -0.7655F, 0.5115F, -1.0864F));

		PartDefinition cube_r24 = Hood.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(44, 37).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.8425F, 3.8247F, -0.829F, 0.0F, 0.0F));

		PartDefinition cube_r25 = Hood.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(32, 30).addBox(-0.5F, -3.0F, -2.625F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0164F, -26.6312F, 4.8836F, -2.8026F, -1.0215F, 2.8494F));

		PartDefinition cube_r26 = Hood.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 31).addBox(-0.5F, -3.0F, -2.625F, 1.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0164F, -26.6312F, 4.8836F, -2.8026F, 1.0215F, -2.8494F));

		PartDefinition cube_r27 = Hood.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(18, 49).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5139F, -27.0565F, 2.8352F, -2.9588F, -0.3006F, 3.0869F));

		PartDefinition cube_r28 = Hood.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(10, 49).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5139F, -27.0565F, 2.8352F, -2.9588F, 0.3006F, -3.0869F));

		PartDefinition cube_r29 = Hood.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(18, 30).addBox(-0.5F, -1.5F, -3.0F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.1043F, -23.6827F, -1.0212F, 3.134F, -0.043F, 1.0474F));

		PartDefinition cube_r30 = Hood.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(50, 8).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.434F, -23.2066F, 2.3778F, 0.3167F, -0.2257F, 2.3058F));

		PartDefinition cube_r31 = Hood.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(12, 31).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.434F, -23.2066F, 2.3778F, 0.3167F, 0.2257F, -2.3058F));

		PartDefinition cube_r32 = Hood.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(12, 39).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -27.25F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition cube_r33 = Hood.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(36, 13).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -27.25F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Hood.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
	}
}