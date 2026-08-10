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

public class MissilePackAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "missile_pack"), "main");
    private final ModelPart MachineBackpack;

    public MissilePackAttachmentModel(ModelPart root) {
        this.MachineBackpack = root.getChild("MachineBackpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition MachineBackpack = partdefinition.addOrReplaceChild("MachineBackpack",
                CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition MachineBackpackMain = MachineBackpack.addOrReplaceChild("MachineBackpackMain",
                CubeListBuilder.create()
                .texOffs(0, 23).addBox(-3.0F, -10.0F, 2.0F, 6.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(32, 10).addBox(-2.0F, -5.0F, 6.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 17).addBox(-2.0F, -9.0F, 6.5F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 23).addBox(-3.5F, -5.0F, 3.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 38).addBox(-1.5F, -8.0F, 7.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(3.0F, -12.0F, 4.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-7.0F, -12.0F, 4.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 37).addBox(4.0F, -13.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 34).addBox(-6.0F, -13.0F, 5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 30).addBox(3.5F, -12.002F, 4.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-6.5F, -12.002F, 4.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r1", CubeListBuilder.create()
                .texOffs(32, 0).addBox(0.5F, -5.0F, -2.506F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(-5.933F, -5.9199F, 4.506F, 0.0F, 0.0F, 0.5236F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r2", CubeListBuilder.create()
                .texOffs(22, 30).addBox(-3.5F, -5.0F, -2.502F, 3.0F, 5.0F, 5.0F, new CubeDeformation(-0.001F)),
                PartPose.offsetAndRotation(5.933F, -5.9199F, 4.502F, 0.0F, 0.0F, -0.5236F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r3", CubeListBuilder.create()
                .texOffs(6, 42).addBox(-1.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 40).addBox(-1.5F, -2.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.3066F, -5.5F, 7.2346F, 0.0F, -1.1781F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r4", CubeListBuilder.create()
                .texOffs(0, 42).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 40).addBox(-1.5F, -2.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, -5.5F, 8.0F, 0.0F, -0.3927F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r5", CubeListBuilder.create()
                .texOffs(28, 40).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 21).addBox(-0.5F, 1.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.0F, -7.5F, 8.0F, 0.0F, 0.3927F, 0.0F));
        MachineBackpackMain.addOrReplaceChild("MachineBackpackMain_r6", CubeListBuilder.create()
                .texOffs(22, 40).addBox(-0.5F, -0.502F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 21).addBox(-0.5F, 1.498F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.3066F, -7.5F, 7.2346F, 0.0F, 1.1781F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        MachineBackpack.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
