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

/**
 * 纯洁印记 — 装饰附件 3D 模型。Blockbench 导出。
 */
public class PurityMarkAttachmentModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath("protectionengineering", "purity_mark"), "main");

    private final ModelPart bb_main;

    public PurityMarkAttachmentModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 3.0F, -3.0F, (float) (-Math.PI / 36), 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                .texOffs(5, 7).addBox(-0.0005F, -0.25F, -0.5F, 0.001F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.3107F, -0.1245F, -1.309F, 1.5621F, -1.5708F));

        bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-0.0005F, -1.0F, -1.5F, 0.001F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.3107F, -0.1245F, -1.7017F, 1.5272F, -1.5708F));

        bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                .texOffs(5, 0).addBox(-0.875F, -2.25F, -0.75F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.0607F, -0.875F, 0.0F, 1.5708F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer,
                               int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }
}
