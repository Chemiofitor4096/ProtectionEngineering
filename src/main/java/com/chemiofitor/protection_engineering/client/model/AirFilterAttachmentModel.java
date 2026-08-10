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

public class AirFilterAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "air_filter"), "main");
    private final ModelPart AirFilter;

    public AirFilterAttachmentModel(ModelPart root) {
        this.AirFilter = root.getChild("AirFilter");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition AirFilter = partdefinition.addOrReplaceChild("AirFilter", CubeListBuilder.create(),
                PartPose.offset(0.0F, -1.875F, 0.25F));

        AirFilter.addOrReplaceChild("AirFilter_r1", CubeListBuilder.create().texOffs(0, 4)
                .addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.7173F, -4.0691F, -0.3054F, 0.0F, 0.0F));

        AirFilter.addOrReplaceChild("AirFilter_r2", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-2.0F, -1.5F, -1.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 2.5F, -3.5F, 0.48F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        AirFilter.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
