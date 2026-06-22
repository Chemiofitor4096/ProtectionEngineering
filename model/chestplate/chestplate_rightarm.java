// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart RightArm;

	public unknown(ModelPart root) {
		this.RightArm = root.getChild("RightArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 46).mirror().addBox(1.4579F, 2.3755F, -6.7188F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(-7.049F, 1.7648F, 4.5F));

		PartDefinition RightArmCloth_r1 = RightArm.addOrReplaceChild("RightArmCloth_r1", CubeListBuilder.create().texOffs(50, 33).mirror().addBox(-1.5625F, -0.4375F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(1.4148F, -1.5958F, -4.498F, 0.0F, 0.0F, -0.2618F));

		PartDefinition RightArmCloth_r2 = RightArm.addOrReplaceChild("RightArmCloth_r2", CubeListBuilder.create().texOffs(32, 37).mirror().addBox(-0.5F, -1.5F, -6.5F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.75F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition RightArmCloth_r3 = RightArm.addOrReplaceChild("RightArmCloth_r3", CubeListBuilder.create().texOffs(24, 10).mirror().addBox(-0.75F, -1.4375F, -7.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition RightForeArmCloth_r1 = RightArm.addOrReplaceChild("RightForeArmCloth_r1", CubeListBuilder.create().texOffs(40, 45).mirror().addBox(-0.75F, -4.0F, -2.0625F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.1671F, 6.3755F, -4.7188F, 0.0F, 0.0F, 0.0873F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}