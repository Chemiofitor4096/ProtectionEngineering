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



public class EngineerChestplateLeftArmModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("protectionengineering", "engineer_chestplate_leftarm"), "main");
	private final ModelPart LeftArm;

	public EngineerChestplateLeftArmModel(ModelPart root) {
		this.LeftArm = root.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 46).addBox(-3.4579F, 2.3755F, -6.7188F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(7.049F, 1.7648F, 4.5F));

		PartDefinition LeftArmCloth_r1 = LeftArm.addOrReplaceChild("LeftArmCloth_r1", CubeListBuilder.create().texOffs(50, 33).addBox(-1.4375F, -0.4375F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-1.4148F, -1.5958F, -4.498F, 0.0F, 0.0F, 0.2618F));

		PartDefinition LeftArmCloth_r2 = LeftArm.addOrReplaceChild("LeftArmCloth_r2", CubeListBuilder.create().texOffs(32, 37).addBox(-2.5F, -1.5F, -6.5F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition LeftArmCloth_r3 = LeftArm.addOrReplaceChild("LeftArmCloth_r3", CubeListBuilder.create().texOffs(24, 10).addBox(-2.25F, -1.4375F, -7.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition LeftForeArmCloth_r1 = LeftArm.addOrReplaceChild("LeftForeArmCloth_r1", CubeListBuilder.create().texOffs(40, 45).addBox(-1.25F, -4.0F, -2.0625F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.1671F, 6.3755F, -4.7188F, 0.0F, 0.0F, -0.0873F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
	}
}