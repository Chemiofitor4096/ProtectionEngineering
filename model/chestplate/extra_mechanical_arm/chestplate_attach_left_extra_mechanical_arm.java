// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart LeftArmArmorered;
	private final ModelPart LeftArmArmoreredGear;

	public unknown(ModelPart root) {
		this.LeftArmArmorered = root.getChild("LeftArmArmorered");
		this.LeftArmArmoreredGear = this.LeftArmArmorered.getChild("LeftArmArmoreredGear");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftArmArmorered = partdefinition.addOrReplaceChild("LeftArmArmorered", CubeListBuilder.create().texOffs(34, 10).addBox(4.7412F, 0.0341F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 7).addBox(2.7412F, 0.0341F, -3.5F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(16, 7).addBox(0.7412F, 0.0341F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7528F, 1.0654F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition LeftArmArmorered_r1 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r1", CubeListBuilder.create().texOffs(36, 32).addBox(8.2412F, -23.4659F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.7996F, 24.2473F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition LeftArmArmorered_r2 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r2", CubeListBuilder.create().texOffs(36, 26).addBox(12.2412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(32.0962F, 12.3862F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition LeftArmArmorered_r3 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r3", CubeListBuilder.create().texOffs(0, 0).addBox(6.2412F, -23.4659F, -2.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(34.5378F, 7.1979F, 0.0F, 0.0F, 0.0F, -1.789F));

		PartDefinition LeftArmArmorered_r4 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r4", CubeListBuilder.create().texOffs(34, 20).addBox(4.7412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 19).addBox(3.2412F, -25.9659F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(3.7412F, -24.4659F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.8478F, 23.3019F, 0.0F, 0.0F, 0.0F, -1.0036F));

		PartDefinition LeftArmArmorered_r5 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r5", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5971F, 9.1503F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition LeftArmArmorered_r6 = LeftArmArmorered.addOrReplaceChild("LeftArmArmorered_r6", CubeListBuilder.create().texOffs(36, 36).addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.2235F, 7.0011F, 0.0F, 0.0F, 0.0F, -1.1781F));

		PartDefinition LeftArmArmoreredGear = LeftArmArmorered.addOrReplaceChild("LeftArmArmoreredGear", CubeListBuilder.create().texOffs(16, 17).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.2412F, 2.5341F, 0.0F));

		PartDefinition LeftArmArmoreredGear_r1 = LeftArmArmoreredGear.addOrReplaceChild("LeftArmArmoreredGear_r1", CubeListBuilder.create().texOffs(34, 0).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition LeftArmArmoreredGear_r2 = LeftArmArmoreredGear.addOrReplaceChild("LeftArmArmoreredGear_r2", CubeListBuilder.create().texOffs(18, 27).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmArmoreredGear_r3 = LeftArmArmoreredGear.addOrReplaceChild("LeftArmArmoreredGear_r3", CubeListBuilder.create().texOffs(0, 27).addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		LeftArmArmorered.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}