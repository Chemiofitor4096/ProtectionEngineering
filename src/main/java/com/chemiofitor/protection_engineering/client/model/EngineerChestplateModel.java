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



public class EngineerChestplateModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("protectionengineering", "engineer_chestplate"), "main");
	private final ModelPart Chestplate;
	private final ModelPart BodyCloth;
	private final ModelPart Medal;

	public EngineerChestplateModel(ModelPart root) {
		this.Chestplate = root.getChild("Chestplate");
		this.BodyCloth = this.Chestplate.getChild("BodyCloth");
		this.Medal = this.Chestplate.getChild("Medal");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Chestplate = partdefinition.addOrReplaceChild("Chestplate", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition BodyCloth = Chestplate.addOrReplaceChild("BodyCloth", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1341F, -2.75F, -3.0091F, 6.0F, 8.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.8659F, -24.1775F, 1.0091F));

		PartDefinition BodyCloth_r1 = BodyCloth.addOrReplaceChild("BodyCloth_r1", CubeListBuilder.create().texOffs(40, 0).mirror().addBox(-1.5F, -3.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0825F, 6.9635F, -0.0991F, 3.0543F, 0.0F, -3.0543F));

		PartDefinition BodyCloth_r2 = BodyCloth.addOrReplaceChild("BodyCloth_r2", CubeListBuilder.create().texOffs(40, 0).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.6492F, 6.9635F, -0.0991F, 3.0543F, 0.0F, 3.0543F));

		PartDefinition BodyCloth_r3 = BodyCloth.addOrReplaceChild("BodyCloth_r3", CubeListBuilder.create().texOffs(38, 19).mirror().addBox(-1.5F, -3.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.02F, 7.3385F, -1.919F, -3.0543F, 0.0F, -3.0543F));

		PartDefinition BodyCloth_r4 = BodyCloth.addOrReplaceChild("BodyCloth_r4", CubeListBuilder.create().texOffs(38, 19).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7117F, 7.3385F, -1.919F, -3.0543F, 0.0F, 3.0543F));

		PartDefinition BodyCloth_r5 = BodyCloth.addOrReplaceChild("BodyCloth_r5", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, -0.75F, 0.252F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3659F, 5.5F, 0.0444F, 0.0873F, 0.0F, 0.0F));

		PartDefinition BodyCloth_r6 = BodyCloth.addOrReplaceChild("BodyCloth_r6", CubeListBuilder.create().texOffs(50, 26).addBox(-3.0F, -0.75F, -1.252F, 5.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3659F, 5.5F, -2.0625F, -0.0873F, 0.0F, 0.0F));

		PartDefinition BodyCloth_r7 = BodyCloth.addOrReplaceChild("BodyCloth_r7", CubeListBuilder.create().texOffs(32, 30).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7409F, 4.75F, -2.375F, 0.0F, 0.0F, 0.1309F));

		PartDefinition BodyCloth_r8 = BodyCloth.addOrReplaceChild("BodyCloth_r8", CubeListBuilder.create().texOffs(14, 30).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9909F, 4.75F, -2.375F, 0.0F, 0.0F, -0.1309F));

		PartDefinition BodyCloth_r9 = BodyCloth.addOrReplaceChild("BodyCloth_r9", CubeListBuilder.create().texOffs(52, 7).addBox(-2.0F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3659F, 3.5F, -3.25F, 0.7854F, 0.0F, 0.0F));

		PartDefinition BodyCloth_r10 = BodyCloth.addOrReplaceChild("BodyCloth_r10", CubeListBuilder.create().texOffs(16, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8659F, 4.75F, -2.875F, 0.0F, 0.0F, 0.7854F));

		PartDefinition BodyCloth_r11 = BodyCloth.addOrReplaceChild("BodyCloth_r11", CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-1.0F, -2.0F, -3.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.7317F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition BodyCloth_r12 = BodyCloth.addOrReplaceChild("BodyCloth_r12", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, -2.0F, -3.5F, 2.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r1 = BodyCloth.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -1.0F, -2.5F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-0.1341F, -1.8225F, -1.0091F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r2 = BodyCloth.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 19).addBox(-3.0F, -1.0F, -2.5F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.8659F, -1.8225F, -1.0091F, 0.0F, 0.0F, -0.2182F));

		PartDefinition Medal = Chestplate.addOrReplaceChild("Medal", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = Medal.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Chestplate.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}