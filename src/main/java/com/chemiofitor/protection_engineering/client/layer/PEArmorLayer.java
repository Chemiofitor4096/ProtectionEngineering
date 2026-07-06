package com.chemiofitor.protection_engineering.client.layer;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.client.PEAttachmentModelRegistry;
import com.chemiofitor.protection_engineering.client.model.*;
import com.chemiofitor.protection_engineering.item.AttachmentHostArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

/**
 * 工程护甲 & 附件渲染层 — 同时支持玩家和盔甲架。
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

    private final EntityModelSet models;

    public PEArmorLayer(LivingEntityRenderer<? extends LivingEntity, ?> renderer, EntityModelSet models) {
        super((LivingEntityRenderer<LivingEntity, HumanoidModel<LivingEntity>>) (Object) renderer);
        this.models = models;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buf, int light,
                       LivingEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(getParentModel() instanceof HumanoidModel humanoidModel)) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = entity.getItemBySlot(slot);
            if (!(armor.getItem() instanceof AttachmentHostArmorItem)) continue;

            switch (slot) {
                case HEAD -> renderHelmet(pose, buf, light, humanoidModel, armor);
                case CHEST -> renderChestplate(pose, buf, light, humanoidModel, armor);
                case LEGS -> renderLeggings(pose, buf, light, humanoidModel, armor);
                case FEET -> renderBoots(pose, buf, light, humanoidModel, armor);
            }
        }
    }

    // ── 渲染辅助 ──────────────────────────────────────────────

    private static void rotateAtPart(PoseStack pose, ModelPart part) {
        float px = part.x / 16f, py = part.y / 16f, pz = part.z / 16f;
        pose.translate(px, py, pz);
        if (part.zRot != 0) pose.mulPose(Axis.ZP.rotation(part.zRot));
        if (part.yRot != 0) pose.mulPose(Axis.YP.rotation(part.yRot));
        if (part.xRot != 0) pose.mulPose(Axis.XP.rotation(part.xRot));
        pose.translate(-px, -py, -pz);
    }

    private void renderModel(PoseStack pose, MultiBufferSource buf, int light,
                             EntityModel<?> model, ResourceLocation tex, ModelPart part) {
        pose.pushPose();
        rotateAtPart(pose, part);
        model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                light, OverlayTexture.NO_OVERLAY, -1);
        pose.popPose();
    }

    // ── 各部位 ────────────────────────────────────────────────

    private void renderHelmet(PoseStack pose, MultiBufferSource buf, int light,
                              HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerHoodModel<>(models.bakeLayer(EngineerHoodModel.LAYER_LOCATION)),
                HELMET_TEX, m.head);
        renderAttachments(armor, pose, buf, light, m.head);
    }

    private void renderChestplate(PoseStack pose, MultiBufferSource buf, int light,
                                  HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerChestplateModel<>(models.bakeLayer(EngineerChestplateModel.LAYER_LOCATION)),
                CHEST_TEX, m.body);
        renderModel(pose, buf, light,
                new EngineerChestplateRightArmModel<>(models.bakeLayer(EngineerChestplateRightArmModel.LAYER_LOCATION)),
                ARMR_TEX, m.rightArm);
        renderModel(pose, buf, light,
                new EngineerChestplateLeftArmModel<>(models.bakeLayer(EngineerChestplateLeftArmModel.LAYER_LOCATION)),
                ARML_TEX, m.leftArm);
        renderAttachments(armor, pose, buf, light, m.body);
        renderArmAttachments(armor, pose, buf, light, m.rightArm, m.leftArm);
    }

    private void renderLeggings(PoseStack pose, MultiBufferSource buf, int light,
                                HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerLeggingsRightModel<>(models.bakeLayer(EngineerLeggingsRightModel.LAYER_LOCATION)),
                LEGS_TEX, m.rightLeg);
        renderModel(pose, buf, light,
                new EngineerLeggingsLeftModel<>(models.bakeLayer(EngineerLeggingsLeftModel.LAYER_LOCATION)),
                LEGS_TEX, m.leftLeg);
        renderAttachments(armor, pose, buf, light, m.rightLeg);
        renderLegAttachments(armor, pose, buf, light, m.rightLeg, m.leftLeg);
    }

    private void renderBoots(PoseStack pose, MultiBufferSource buf, int light,
                             HumanoidModel<?> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerBootsRightModel<>(models.bakeLayer(EngineerBootsRightModel.LAYER_LOCATION)),
                BOOTS_TEX, m.rightLeg);
        renderModel(pose, buf, light,
                new EngineerBootsLeftModel<>(models.bakeLayer(EngineerBootsLeftModel.LAYER_LOCATION)),
                BOOTS_TEX, m.leftLeg);
        renderAttachments(armor, pose, buf, light, m.rightLeg);
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
                rotateAtPart(pose, leftArm);
                leftModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                        light, OverlayTexture.NO_OVERLAY, -1);
                pose.popPose();
            }

            EntityModel<?> rightModel = PEAttachmentModelRegistry.createRightArmModel(attachment, models);
            if (rightModel != null) {
                pose.pushPose();
                rotateAtPart(pose, rightArm);
                rightModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(tex)),
                        light, OverlayTexture.NO_OVERLAY, -1);
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
            rotateAtPart(pose, leftLeg);
            leftModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();

            // 右腿 — X 轴镜像（使用左腿模型或独立右腿模型）
            EntityModel<?> rightModel = PEAttachmentModelRegistry.createRightLegModel(attachment, models);
            if (rightModel == null) rightModel = leftModel;
            pose.pushPose();
            rotateAtPart(pose, rightLeg);
            if (rightModel == leftModel) pose.scale(-1, 1, 1);
            rightModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();
        }
    }

    // ── 附件渲染（通用路径）──────────────────────────────────

    private void renderAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                   int light, ModelPart part) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            EntityModel<?> model = PEAttachmentModelRegistry.createMainModel(attachment, models);
            ResourceLocation texture = PEAttachmentModelRegistry.getMainTexture(attachment);
            if (model == null || texture == null) continue;

            pose.pushPose();
            rotateAtPart(pose, part);
            model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(texture)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();
        }
    }
}
