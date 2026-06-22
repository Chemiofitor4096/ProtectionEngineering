// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart RightArmArmorered;
	private final ModelPart RightArmArmoreredGear;

	public unknown(ModelPart root) {
		this.RightArmArmorered = root.getChild("RightArmArmorered");
		this.RightArmArmoreredGear = this.RightArmArmorered.getChild("RightArmArmoreredGear");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightArmArmorered = partdefinition.addOrReplaceChild("RightArmArmorered", CubeListBuilder.create().texOffs(34, 10).mirror().addBox(-6.7412F, 0.0341F, -2.5F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 7).mirror().addBox(-3.7412F, 0.0341F, -3.5F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 7).mirror().addBox(-2.7412F, 0.0341F, -3.5F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.7528F, 1.0654F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition RightArmArmorered_r1 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r1", CubeListBuilder.create().texOffs(36, 32).mirror().addBox(-13.2412F, -23.4659F, -1.0F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-15.7996F, 24.2473F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition RightArmArmorered_r2 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r2", CubeListBuilder.create().texOffs(36, 26).mirror().addBox(-15.2412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-32.0962F, 12.3862F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition RightArmArmorered_r3 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-14.2412F, -23.4659F, -2.5F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-34.5378F, 7.1979F, 0.0F, 0.0F, 0.0F, 1.789F));

		PartDefinition RightArmArmorered_r4 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r4", CubeListBuilder.create().texOffs(34, 20).mirror().addBox(-7.7412F, -23.9659F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(10, 19).mirror().addBox(-4.2412F, -25.9659F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 19).mirror().addBox(-4.7412F, -24.4659F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-26.8478F, 23.3019F, 0.0F, 0.0F, 0.0F, 1.0036F));

		PartDefinition RightArmArmorered_r5 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r5", CubeListBuilder.create().texOffs(0, 37).mirror().addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.5971F, 9.1503F, 0.0F, 0.0F, 0.0F, 0.829F));

		PartDefinition RightArmArmorered_r6 = RightArmArmorered.addOrReplaceChild("RightArmArmorered_r6", CubeListBuilder.create().texOffs(36, 36).mirror().addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.2235F, 7.0011F, 0.0F, 0.0F, 0.0F, 1.1781F));

		PartDefinition RightArmArmoreredGear = RightArmArmorered.addOrReplaceChild("RightArmArmoreredGear", CubeListBuilder.create().texOffs(16, 17).mirror().addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.2412F, 2.5341F, 0.0F));

		PartDefinition RightArmArmoreredGear_r1 = RightArmArmoreredGear.addOrReplaceChild("RightArmArmoreredGear_r1", CubeListBuilder.create().texOffs(34, 0).mirror().addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition RightArmArmoreredGear_r2 = RightArmArmoreredGear.addOrReplaceChild("RightArmArmoreredGear_r2", CubeListBuilder.create().texOffs(18, 27).mirror().addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmArmoreredGear_r3 = RightArmArmoreredGear.addOrReplaceChild("RightArmArmoreredGear_r3", CubeListBuilder.create().texOffs(0, 27).mirror().addBox(-0.5F, -1.0F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightArmArmorered.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}