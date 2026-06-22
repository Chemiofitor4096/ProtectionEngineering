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



public class EngineerBootsRightModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("protectionengineering", "engineer_boots_right"), "main");
	private final ModelPart RightFoot;

	public EngineerBootsRightModel(ModelPart root) {
		this.RightFoot = root.getChild("RightFoot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightFoot = partdefinition.addOrReplaceChild("RightFoot", CubeListBuilder.create().texOffs(30, 0).mirror().addBox(-12.5F, 15.75F, -4.4734F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(8.0F, 7.5F, 0.0F));

		PartDefinition LeftFootCloth_r1 = RightFoot.addOrReplaceChild("LeftFootCloth_r1", CubeListBuilder.create().texOffs(24, 20).mirror().addBox(-15.925F, 2.7125F, -0.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 20).mirror().addBox(-19.425F, 2.7125F, -0.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.175F, 13.0286F, -1.6166F, 0.2313F, 0.0F, 0.0F));

		PartDefinition RightFootCloth_r1 = RightFoot.addOrReplaceChild("RightFootCloth_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.075F, -1.725F, -3.625F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-6.925F, 15.1006F, -0.8719F, 0.2313F, 0.0F, 0.0F));

		PartDefinition RightFootCloth_r2 = RightFoot.addOrReplaceChild("RightFootCloth_r2", CubeListBuilder.create().texOffs(20, 0).mirror().addBox(-20.9375F, -2.1351F, 2.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(8.9375F, 14.6351F, -1.2234F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		RightFoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
	}
}