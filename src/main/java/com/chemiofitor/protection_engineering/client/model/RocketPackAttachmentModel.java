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

public class RocketPackAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "rocket_pack"), "main");
    private final ModelPart MachineBackpack;

    public RocketPackAttachmentModel(ModelPart root) {
        this.MachineBackpack = root.getChild("MachineBackpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MachineBackpack = partdefinition.addOrReplaceChild("MachineBackpack",
                CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition MachineBackpackMain = MachineBackpack.addOrReplaceChild("MachineBackpackMain",
                CubeListBuilder.create()
                .texOffs(10, 38).addBox(-4.0F, -5.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 40).addBox(3.0F, -5.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-3.0F, -10.0F, 2.0F, 6.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(16, 30).addBox(-2.0F, -5.0F, 6.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 8).addBox(-2.0F, -9.0F, 6.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 13).addBox(-3.5F, -5.0F, 3.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-1.5F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r1", CubeListBuilder.create()
                .texOffs(0, 28).addBox(0.5F, -5.0F, -2.506F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-5.933F, -5.9199F, 4.506F, 0.0F, 0.0F, 0.5236F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r2", CubeListBuilder.create()
                .texOffs(22, 20).addBox(-3.5F, -5.0F, -2.502F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(5.933F, -5.9199F, 4.502F, 0.0F, 0.0F, -0.5236F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r3", CubeListBuilder.create()
                .texOffs(42, 12).addBox(-1.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 40).addBox(-1.5F, -2.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.3066F, -5.5F, 7.2346F, 0.0F, -1.1781F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r4", CubeListBuilder.create()
                .texOffs(40, 40).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 40).addBox(-1.5F, -2.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -5.5F, 8.0F, 0.0F, -0.3927F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r5", CubeListBuilder.create()
                .texOffs(22, 40).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 28).addBox(-0.5F, 1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -7.5F, 8.0F, 0.0F, 0.3927F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r6", CubeListBuilder.create()
                .texOffs(38, 28).addBox(-0.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 26).addBox(-0.5F, 1.498F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.3066F, -7.5F, 7.2346F, 0.0F, 1.1781F, 0.0F));

        PartDefinition Rocket = MachineBackpack.addOrReplaceChild("Rocket", CubeListBuilder.create()
                .texOffs(0, 0).addBox(7.2418F, -18.862F, 1.0F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.001F))
                .texOffs(0, 0).mirror().addBox(-12.2418F, -18.862F, 1.0F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Left side rockets
        Rocket.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(30, 37).mirror().addBox(-2.0F, -1.0F, 0.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false)
                .texOffs(16, 37).mirror().addBox(-2.0F, -1.0F, -1.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offsetAndRotation(-6.4142F, -9.8284F, 4.5F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(30, 0).mirror().addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offsetAndRotation(-7.8284F, -11.2426F, 4.5F, 0.0F, 0.0F, 1.5708F));
        Rocket.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                .texOffs(38, 20).mirror().addBox(-5.0F, -1.0F, 0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false)
                .texOffs(38, 23).mirror().addBox(-5.0F, -1.0F, -1.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offsetAndRotation(-7.8284F, -11.2426F, 4.5F, 0.0F, 0.0F, 1.1781F));
        Rocket.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                .texOffs(30, 30).mirror().addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.001F)).mirror(false),
                PartPose.offsetAndRotation(-9.7418F, -15.862F, 4.5F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                .texOffs(42, 14).mirror().addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false),
                PartPose.offsetAndRotation(-8.7418F, -17.362F, 1.25F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                .texOffs(42, 14).mirror().addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false),
                PartPose.offsetAndRotation(-9.7418F, -17.362F, 1.25F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r7", CubeListBuilder.create()
                .texOffs(42, 14).mirror().addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false),
                PartPose.offsetAndRotation(-10.7418F, -17.362F, 1.25F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r8", CubeListBuilder.create()
                .texOffs(42, 14).mirror().addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false),
                PartPose.offsetAndRotation(-10.2418F, -16.612F, 1.25F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r9", CubeListBuilder.create()
                .texOffs(42, 14).mirror().addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false),
                PartPose.offsetAndRotation(-9.2418F, -16.612F, 1.25F, 0.0F, 0.0F, -0.7854F));

        // Right side rockets
        Rocket.addOrReplaceChild("cube_r10", CubeListBuilder.create()
                .texOffs(30, 37).addBox(-4.0F, -1.0F, 0.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(16, 37).addBox(-4.0F, -1.0F, -1.5F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(6.4142F, -9.8284F, 4.5F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r11", CubeListBuilder.create()
                .texOffs(30, 0).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(7.8284F, -11.2426F, 4.5F, 0.0F, 0.0F, -1.5708F));
        Rocket.addOrReplaceChild("cube_r12", CubeListBuilder.create()
                .texOffs(38, 20).addBox(0.0F, -1.0F, 0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(38, 23).addBox(0.0F, -1.0F, -1.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(7.8284F, -11.2426F, 4.5F, 0.0F, 0.0F, -1.1781F));
        Rocket.addOrReplaceChild("cube_r13", CubeListBuilder.create()
                .texOffs(30, 30).addBox(-1.0F, -1.0F, -2.5F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(9.7418F, -15.862F, 4.5F, 0.0F, 0.0F, -0.7854F));
        Rocket.addOrReplaceChild("cube_r14", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(8.7418F, -17.362F, 1.25F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r15", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(9.7418F, -17.362F, 1.25F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r16", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(10.7418F, -17.362F, 1.25F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r17", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(10.2418F, -16.612F, 1.25F, 0.0F, 0.0F, 0.7854F));
        Rocket.addOrReplaceChild("cube_r18", CubeListBuilder.create()
                .texOffs(42, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)),
                PartPose.offsetAndRotation(9.2418F, -16.612F, 1.25F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        MachineBackpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
