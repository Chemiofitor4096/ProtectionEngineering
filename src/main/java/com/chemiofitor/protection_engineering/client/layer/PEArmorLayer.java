package com.chemiofitor.protection_engineering.client.layer;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.client.PEAttachmentModelRegistry;
import com.chemiofitor.protection_engineering.client.model.*;
import com.chemiofitor.protection_engineering.compat.PlayerAnimCompat;
import com.chemiofitor.protection_engineering.item.AttachmentHostArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

/**
 * 工程护甲 & 附件渲染层 — 同时支持玩家和盔甲架。
 * <p>
 * {@link #rotateAtPart} 的旋转锚点用玩家 HumanoidModel 部件的<strong>原始 pivot</strong>
 * （Blockbench 护甲模型基于它设计），但平移跟随<strong>动画后的 pivot</strong>
 * （PlayerAnimator 位置关键帧会移动 part.x/y/z）。由此护甲渲染位置 =
 * 新pivot + R·(cube_abs − 原始pivot) = 玩家部件位置，动画（含 pivot 移动）下精确贴合。
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PEArmorLayer extends RenderLayer<LivingEntity, HumanoidModel<LivingEntity>> {

    private static final ResourceLocation HELMET_TEX = tex("engineer_hood.png");
    private static final ResourceLocation CHEST_TEX  = tex("engineer_chestplate.png");
    private static final ResourceLocation LEGS_TEX   = tex("engineer_leggings.png");
    private static final ResourceLocation BOOTS_TEX  = tex("engineer_boots.png");
    private static final ResourceLocation ARMR_TEX   = tex("engineer_chestplate_rightarm.png");
    private static final ResourceLocation ARML_TEX   = tex("engineer_chestplate_leftarm.png");

    private static ResourceLocation tex(String name) {
        return ProtectionEngineering.asResource("textures/models/armor/" + name);
    }

    // 玩家 HumanoidModel 部件的原始 pivot（护甲模型基于它设计；动画会修改 part.x/y/z）
    private static final float HEAD_PX = 0f, HEAD_PY = 0f, HEAD_PZ = 0f;
    private static final float BODY_PX = 0f, BODY_PY = 0f, BODY_PZ = 0f;
    private static final float R_ARM_PX = -5f, R_ARM_PY = 2f, R_ARM_PZ = 0f;
    private static final float L_ARM_PX = 5f, L_ARM_PY = 2f, L_ARM_PZ = 0f;
    private static final float R_LEG_PX = -1.9f, R_LEG_PY = 12f, R_LEG_PZ = 0f;
    private static final float L_LEG_PX = 1.9f, L_LEG_PY = 12f, L_LEG_PZ = 0f;

    private final EntityModelSet models;

    public PEArmorLayer(LivingEntityRenderer<? extends LivingEntity, ?> renderer, EntityModelSet models) {
        super((LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) (Object) renderer);
        this.models = models;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buf, int light,
                       LivingEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        HumanoidModel<?> humanoidModel = getParentModel();
        if (humanoidModel == null) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            ItemStack armor = entity.getItemBySlot(slot);
            if (!(armor.getItem() instanceof AttachmentHostArmorItem)) continue;

            switch (slot) {
                // 上半身（head/body/arm）需跟随 PlayerAnimator 的 body 弯曲动画
                case HEAD -> {
                    pose.pushPose();
                    applyPlayerAnimBend(pose, entity);
                    renderHelmet(pose, buf, light, humanoidModel, armor);
                    pose.popPose();
                }
                case CHEST -> {
                    pose.pushPose();
                    applyPlayerAnimBend(pose, entity);
                    renderChestplate(pose, buf, light, humanoidModel, armor);
                    pose.popPose();
                }
                // 下半身（腿/靴）走部件旋转即可，不应用 body 弯曲
                case LEGS -> renderLeggings(pose, buf, light, humanoidModel, armor);
                case FEET -> renderBoots(pose, buf, light, humanoidModel, armor);
            }
        }
    }

    // ── 渲染辅助 ──────────────────────────────────────────────

    /** PlayerAnimator 弯曲兼容：动画活跃时对上半身应用 body 弯曲（playeranimator 缺席时安全跳过） */
    private static void applyPlayerAnimBend(PoseStack pose, LivingEntity entity) {
        if (ModList.get().isLoaded("playeranimator")) {
            PlayerAnimCompat.applyBodyBend(pose, entity);
        }
    }

    /**
     * 把 PoseStack 对齐到玩家部件：平移到<strong>动画后</strong>的 pivot（跟随 PlayerAnimator
     * 位置关键帧），应用部件旋转，再减去<strong>原始</strong> pivot —— 使绝对坐标护甲模型
     * 在 pivot 移动 + 旋转的动画下精确跟随玩家部件。
     */
    private static void rotateAtPart(PoseStack pose, ModelPart part,
                                     float origPx, float origPy, float origPz) {
        float px = part.x / 16f, py = part.y / 16f, pz = part.z / 16f;
        pose.translate(px, py, pz);
        if (part.zRot != 0) pose.mulPose(Axis.ZP.rotation(part.zRot));
        if (part.yRot != 0) pose.mulPose(Axis.YP.rotation(part.yRot));
        if (part.xRot != 0) pose.mulPose(Axis.XP.rotation(part.xRot));
        pose.translate(-origPx / 16f, -origPy / 16f, -origPz / 16f);
    }

    private void renderModel(PoseStack pose, MultiBufferSource buf, int light,
                             EntityModel<?> model, ResourceLocation tex, ModelPart part,
                             float origPx, float origPy, float origPz) {
        pose.pushPose();
        rotateAtPart(pose, part, origPx, origPy, origPz);
        model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        pose.popPose();
    }

    // ── 各部位 ────────────────────────────────────────────────

    private void renderHelmet(PoseStack pose, MultiBufferSource buf, int light,
                              HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerHoodModel<>(models.bakeLayer(EngineerHoodModel.LAYER_LOCATION)),
                HELMET_TEX, m.head, HEAD_PX, HEAD_PY, HEAD_PZ);
        renderAttachments(armor, pose, buf, light, m.head, HEAD_PX, HEAD_PY, HEAD_PZ);
    }

    private void renderChestplate(PoseStack pose, MultiBufferSource buf, int light,
                                  HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerChestplateModel<>(models.bakeLayer(EngineerChestplateModel.LAYER_LOCATION)),
                CHEST_TEX, m.body, BODY_PX, BODY_PY, BODY_PZ);
        renderModel(pose, buf, light,
                new EngineerChestplateRightArmModel<>(models.bakeLayer(EngineerChestplateRightArmModel.LAYER_LOCATION)),
                ARMR_TEX, m.rightArm, R_ARM_PX, R_ARM_PY, R_ARM_PZ);
        renderModel(pose, buf, light,
                new EngineerChestplateLeftArmModel<>(models.bakeLayer(EngineerChestplateLeftArmModel.LAYER_LOCATION)),
                ARML_TEX, m.leftArm, L_ARM_PX, L_ARM_PY, L_ARM_PZ);
        renderAttachments(armor, pose, buf, light, m.body, BODY_PX, BODY_PY, BODY_PZ);
        renderArmAttachments(armor, pose, buf, light, m.rightArm, m.leftArm);
    }

    private void renderLeggings(PoseStack pose, MultiBufferSource buf, int light,
                                HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerLeggingsRightModel<>(models.bakeLayer(EngineerLeggingsRightModel.LAYER_LOCATION)),
                LEGS_TEX, m.rightLeg, R_LEG_PX, R_LEG_PY, R_LEG_PZ);
        renderModel(pose, buf, light,
                new EngineerLeggingsLeftModel<>(models.bakeLayer(EngineerLeggingsLeftModel.LAYER_LOCATION)),
                LEGS_TEX, m.leftLeg, L_LEG_PX, L_LEG_PY, L_LEG_PZ);
        renderAttachments(armor, pose, buf, light, m.rightLeg, R_LEG_PX, R_LEG_PY, R_LEG_PZ);
        renderLegAttachments(armor, pose, buf, light, m.rightLeg, m.leftLeg);
    }

    private void renderBoots(PoseStack pose, MultiBufferSource buf, int light,
                             HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerBootsRightModel<>(models.bakeLayer(EngineerBootsRightModel.LAYER_LOCATION)),
                BOOTS_TEX, m.rightLeg, R_LEG_PX, R_LEG_PY, R_LEG_PZ);
        renderModel(pose, buf, light,
                new EngineerBootsLeftModel<>(models.bakeLayer(EngineerBootsLeftModel.LAYER_LOCATION)),
                BOOTS_TEX, m.leftLeg, L_LEG_PX, L_LEG_PY, L_LEG_PZ);
        renderAttachments(armor, pose, buf, light, m.rightLeg, R_LEG_PX, R_LEG_PY, R_LEG_PZ);
    }

    private void renderArmAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                      int light, ModelPart rightArm, ModelPart leftArm) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            ResourceLocation tex = PEAttachmentModelRegistry.getArmTexture(attachment);
            if (tex == null) continue;

            EntityModel<?> leftModel = PEAttachmentModelRegistry.createLeftArmModel(attachment, models);
            if (leftModel != null) {
                pose.pushPose();
                rotateAtPart(pose, leftArm, L_ARM_PX, L_ARM_PY, L_ARM_PZ);
                leftModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                        light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
                pose.popPose();
            }

            EntityModel<?> rightModel = PEAttachmentModelRegistry.createRightArmModel(attachment, models);
            if (rightModel != null) {
                pose.pushPose();
                rotateAtPart(pose, rightArm, R_ARM_PX, R_ARM_PY, R_ARM_PZ);
                rightModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                        light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
                pose.popPose();
            }
        }
    }

    private void renderLegAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                      int light, ModelPart rightLeg, ModelPart leftLeg) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            ResourceLocation legTex = PEAttachmentModelRegistry.getLegTexture(attachment);
            if (legTex == null) continue;

            EntityModel<?> leftModel = PEAttachmentModelRegistry.createLeftLegModel(attachment, models);
            if (leftModel == null) continue;

            // 左腿
            pose.pushPose();
            rotateAtPart(pose, leftLeg, L_LEG_PX, L_LEG_PY, L_LEG_PZ);
            leftModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            pose.popPose();

            // 右腿 — X 轴镜像（使用左腿模型或独立右腿模型）
            EntityModel<?> rightModel = PEAttachmentModelRegistry.createRightLegModel(attachment, models);
            if (rightModel == null) rightModel = leftModel;
            pose.pushPose();
            rotateAtPart(pose, rightLeg, R_LEG_PX, R_LEG_PY, R_LEG_PZ);
            if (rightModel == leftModel) pose.scale(-1, 1, 1);
            rightModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            pose.popPose();
        }
    }

    // ── 附件渲染（通用路径）──────────────────────────────────

    private void renderAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                   int light, ModelPart part,
                                   float origPx, float origPy, float origPz) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            EntityModel<?> model = PEAttachmentModelRegistry.createMainModel(attachment, models);
            ResourceLocation texture = PEAttachmentModelRegistry.getMainTexture(attachment);
            if (model == null || texture == null) continue;

            pose.pushPose();
            rotateAtPart(pose, part, origPx, origPy, origPz);
            model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(texture)),
                    light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
            pose.popPose();
        }
    }
}
