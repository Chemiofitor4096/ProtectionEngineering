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

public class JetpackAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "jetpack"), "main");
    private final ModelPart MachineBackpack;

    public JetpackAttachmentModel(ModelPart root) {
        this.MachineBackpack = root.getChild("MachineBackpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MachineBackpack = partdefinition.addOrReplaceChild("MachineBackpack",
                CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition MachineBackpackMain = MachineBackpack.addOrReplaceChild("MachineBackpackMain",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-3.0F, -10.0F, 2.0F, 6.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 32).addBox(-2.0F, -5.0F, 6.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(22, 11).addBox(-2.0F, -9.0F, 6.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 15).addBox(-3.5F, -5.0F, 3.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(34, 0).addBox(-1.5F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r1", CubeListBuilder.create()
                .texOffs(0, 22).addBox(0.5F, -5.0F, -2.506F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-5.933F, -5.9199F, 4.506F, 0.0F, 0.0F, 0.5236F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r2", CubeListBuilder.create()
                .texOffs(20, 15).addBox(-3.5F, -5.0F, -2.502F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(5.933F, -5.9199F, 4.502F, 0.0F, 0.0F, -0.5236F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r3", CubeListBuilder.create()
                .texOffs(38, 39).addBox(-1.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 39).addBox(-1.5F, -2.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.3066F, -5.5F, 7.2346F, 0.0F, -1.1781F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r4", CubeListBuilder.create()
                .texOffs(32, 39).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 39).addBox(-1.5F, -2.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -5.5F, 8.0F, 0.0F, -0.3927F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r5", CubeListBuilder.create()
                .texOffs(6, 39).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 37).addBox(-0.5F, 1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -7.5F, 8.0F, 0.0F, 0.3927F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r6", CubeListBuilder.create()
                .texOffs(0, 39).addBox(-0.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 37).addBox(-0.5F, 1.498F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.3066F, -7.5F, 7.2346F, 0.0F, 1.1781F, 0.0F));

        PartDefinition MachineElytra = MachineBackpack.addOrReplaceChild("MachineElytra",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.0F));

        MachineElytra.addOrReplaceChild("MachineElytraMain", CubeListBuilder.create()
                .texOffs(34, 5).addBox(2.0F, -5.0F, 3.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 10).addBox(-5.0F, -5.0F, 3.5F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(4.0F, -6.0F, 3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 25).addBox(-7.0F, -6.0F, 3.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 36).addBox(4.5F, 1.002F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 21).addBox(-6.5F, 1.002F, 3.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 36).addBox(6.5F, -6.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 15).addBox(-7.5F, -6.5F, 3.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 25).addBox(-7.5F, -1.0F, 2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(3.5F, -1.0F, 2.5F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        MachineBackpack.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
