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



public class EngineerBootsLeftModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("protectionengineering", "engineer_boots_left"), "main");
	private final ModelPart LeftFoot;

	public EngineerBootsLeftModel(ModelPart root) {
		this.LeftFoot = root.getChild("LeftFoot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftFoot = partdefinition.addOrReplaceChild("LeftFoot", CubeListBuilder.create().texOffs(30, 0).addBox(7.5F, 15.75F, -4.4734F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 7.5F, 0.0F));

		PartDefinition RightFootCloth_r1 = LeftFoot.addOrReplaceChild("RightFootCloth_r1", CubeListBuilder.create().texOffs(24, 20).addBox(14.925F, 2.7125F, -0.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(18.425F, 2.7125F, -0.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.175F, 13.0286F, -1.6166F, 0.2313F, 0.0F, 0.0F));

		PartDefinition LeftFootCloth_r1 = LeftFoot.addOrReplaceChild("LeftFootCloth_r1", CubeListBuilder.create().texOffs(0, 0).addBox(1.075F, -1.725F, -3.625F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(6.925F, 15.1006F, -0.8719F, 0.2313F, 0.0F, 0.0F));

		PartDefinition LeftFootCloth_r2 = LeftFoot.addOrReplaceChild("LeftFootCloth_r2", CubeListBuilder.create().texOffs(20, 0).addBox(16.9375F, -2.1351F, 2.5F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(-8.9375F, 14.6351F, -1.2234F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		LeftFoot.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}