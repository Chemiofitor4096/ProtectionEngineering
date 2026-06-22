package com.chemiofitor.protection_engineering.client.renderer;

import com.chemiofitor.protection_engineering.entity.RocketProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * 火箭射弹渲染器 —— 渲染存储的烟花火箭 {@link net.minecraft.world.item.ItemStack}。
 */
public class RocketProjectileRenderer extends EntityRenderer<RocketProjectile> {

    private static final ResourceLocation TEXTURE = TextureAtlas.LOCATION_BLOCKS;

    public RocketProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(RocketProjectile entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 居中于实体位置
        poseStack.translate(0, 0.15, 0);

        // 对齐运动方向
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot + 90));

        // 缩放使烟花火箭看起来像射弹
        poseStack.scale(0.8f, 0.8f, 0.8f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                entity.getRenderItem(),
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                0);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketProjectile entity) {
        return TEXTURE; // 烟花火箭贴图从方块 atlas 来
    }
}
