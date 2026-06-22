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



public class EngineerLeggingsRightModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("protectionengineering", "engineer_leggings_right"), "main");
	private final ModelPart RightLeg;

	public EngineerLeggingsRightModel(ModelPart root) {
		this.RightLeg = root.getChild("RightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-9.9602F, 10.9899F, -1.9766F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)).mirror(false)
		.texOffs(28, 8).mirror().addBox(-10.6875F, 8.8125F, -2.4766F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.9602F, 7.0101F, -0.0234F));

		PartDefinition RightLegProtect_r1 = RightLeg.addOrReplaceChild("RightLegProtect_r1", CubeListBuilder.create().texOffs(48, 8).mirror().addBox(-1.5F, -2.0F, -0.5F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.1875F, 9.7907F, -2.626F, -0.0873F, 0.0F, 3.1416F));

		PartDefinition RightLegCloth_r1 = RightLeg.addOrReplaceChild("RightLegCloth_r1", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(-1.0F, -3.0F, -2.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false), PartPose.offsetAndRotation(-9.1183F, 8.9173F, 0.0234F, 0.0F, 0.0F, 0.0436F));

		PartDefinition RightLegCloth_r2 = RightLeg.addOrReplaceChild("RightLegCloth_r2", CubeListBuilder.create().texOffs(12, 8).mirror().addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-8.1086F, 8.8377F, 0.0234F, 0.0F, 0.0F, -0.0262F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
	}
}