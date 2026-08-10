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

public class MechaKnuckleRightAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation("protectionengineering", "mecha_knuckle_right"), "main");
    private final ModelPart GauntletRight;

    public MechaKnuckleRightAttachmentModel(ModelPart root) {
        this.GauntletRight = root.getChild("GauntletRight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition GauntletRight = partdefinition.addOrReplaceChild("GauntletRight",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-8.0F, -18.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        GauntletRight.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
