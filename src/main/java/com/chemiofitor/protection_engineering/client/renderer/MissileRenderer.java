package com.chemiofitor.protection_engineering.client.renderer;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.model.MissileModel;
import com.chemiofitor.protection_engineering.entity.MissileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 制导导弹渲染器。模型 nose 在 -Y，旋转对齐速度方向：β=-yRot, α=xRot-90。
 */
public class MissileRenderer extends EntityRenderer<MissileEntity> {

    private static final ResourceLocation TEXTURE =
            ProtectionEngineering.asResource("textures/entity/missile.png");

    private final MissileModel<MissileEntity> model;

    public MissileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new MissileModel<>(context.bakeLayer(MissileModel.LAYER_LOCATION));
    }

    @Override
    public void render(MissileEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 补偿模型 Y=24 偏移
        poseStack.translate(0, -0.5, 0);

        // 模型 nose 在 -Y → 映射到速度方向: YP(-yRot) × XP(xRot-90)
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot - 90));

        VertexConsumer vertexConsumer = buffer.getBuffer(
                RenderType.entityCutoutNoCull(TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight,
                OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MissileEntity entity) {
        return TEXTURE;
    }
}
