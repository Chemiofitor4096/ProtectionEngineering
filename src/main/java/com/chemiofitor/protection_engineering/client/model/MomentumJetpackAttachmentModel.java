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

public class MomentumJetpackAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "momentum_jetpack"), "main");
    private final ModelPart MachineBackpack;

    public MomentumJetpackAttachmentModel(ModelPart root) {
        this.MachineBackpack = root.getChild("MachineBackpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MachineBackpack = partdefinition.addOrReplaceChild("MachineBackpack",
                CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition MachineBackpackMain = MachineBackpack.addOrReplaceChild("MachineBackpackMain",
                CubeListBuilder.create()
                        .texOffs(0, 9).addBox(-3.0F, -10.0F, 2.0F, 6.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 37).addBox(-2.0F, -5.0F, 6.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(38, 16).addBox(-2.0F, -9.0F, 6.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 9).addBox(-3.5F, -5.0F, 3.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(38, 20).addBox(-1.5F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r1", CubeListBuilder.create()
                .texOffs(0, 24).addBox(0.5F, -5.0F, -2.506F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-5.933F, -5.9199F, 4.506F, 0.0F, 0.0F, 0.5236F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r2", CubeListBuilder.create()
                .texOffs(22, 16).addBox(-3.5F, -5.0F, -2.502F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(5.933F, -5.9199F, 4.502F, 0.0F, 0.0F, -0.5236F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r3", CubeListBuilder.create()
                .texOffs(34, 44).addBox(-1.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 43).addBox(-1.5F, -2.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.3066F, -5.5F, 7.2346F, 0.0F, -1.1781F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r4", CubeListBuilder.create()
                .texOffs(28, 44).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 43).addBox(-1.5F, -2.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -5.5F, 8.0F, 0.0F, -0.3927F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r5", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 24).addBox(-0.5F, 1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -7.5F, 8.0F, 0.0F, 0.3927F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r6", CubeListBuilder.create()
                .texOffs(42, 12).addBox(-0.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 35).addBox(-0.5F, 1.498F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.3066F, -7.5F, 7.2346F, 0.0F, 1.1781F, 0.0F));

        MachineBackpack.addOrReplaceChild("MachineElytra", CubeListBuilder.create()
                .texOffs(16, 37).addBox(3.5F, 0.0F, 2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-7.5F, 0.0F, 2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(10, 40).addBox(-7.5F, -5.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 0).addBox(6.5F, -5.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 9).addBox(-6.5F, 2.002F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(42, 6).addBox(4.5F, 2.002F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 26).addBox(-7.0F, -5.0F, 3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 26).addBox(4.0F, -5.0F, 3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 30).addBox(-5.0F, -4.0F, 3.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 25).addBox(2.0F, -4.0F, 3.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.5F, 0.0F));

        PartDefinition Wings = MachineBackpack.addOrReplaceChild("Wings", CubeListBuilder.create()
                .texOffs(0, 40).addBox(1.0F, -13.0F, 4.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(0, 40).mirror().addBox(-5.0F, -13.0F, 4.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        Wings.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(0, 0).mirror().addBox(-10.0F, -1.0F, 0.5F, 20.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-13.5595F, -6.9686F, 3.5F, 0.0F, 0.0F, -0.2618F));

        Wings.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(0, 5).mirror().addBox(-10.0F, -1.0F, 0.5F, 20.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)).mirror(false),
                PartPose.offsetAndRotation(-13.8561F, -8.2493F, 3.5F, 0.0F, 0.0F, -0.3927F));

        Wings.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-10.0F, -1.0F, 0.5F, 20.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(13.5595F, -6.9686F, 3.5F, 0.0F, 0.0F, 0.2618F));

        Wings.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                .texOffs(0, 5).addBox(-10.0F, -1.0F, 0.5F, 20.0F, 3.0F, 1.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(13.8561F, -8.2493F, 3.5F, 0.0F, 0.0F, 0.3927F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        MachineBackpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
