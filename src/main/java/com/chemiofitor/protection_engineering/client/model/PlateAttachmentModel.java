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

public class PlateAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "sturdy_plate"), "main");
    private final ModelPart Plate;

    public PlateAttachmentModel(ModelPart root) {
        this.Plate = root.getChild("Plate");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Plate = partdefinition.addOrReplaceChild("Plate", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        Plate.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -3.5F, -0.5F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.5F, -21.5F, -3.0F, -0.0436F, -0.0019F, -0.0436F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Plate.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
