// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart AttachGlass;

	public unknown(ModelPart root) {
		this.AttachGlass = root.getChild("AttachGlass");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition AttachGlass = partdefinition.addOrReplaceChild("AttachGlass", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -32.0F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
		.texOffs(24, 24).addBox(-4.25F, -27.75F, -4.25F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(24, 16).addBox(-2.25F, -32.25F, -4.25F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(24, 28).addBox(-4.25F, -32.25F, -2.25F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = AttachGlass.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 31).addBox(0.0F, -1.0F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 28).addBox(-0.5F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -26.5F, -4.0F, 0.0F, 0.0F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		AttachGlass.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}