package com.chemiofitor.protection_engineering.client.layer;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
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
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PEArmorLayer extends RenderLayer {

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

    public PEArmorLayer(PlayerRenderer renderer, EntityModelSet models) {
        super(renderer);
        this.models = models;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buf, int light,
                       net.minecraft.world.entity.Entity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(entity instanceof Player player)) return;
        if (!(getParentModel() instanceof HumanoidModel playerModel)) return;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof AttachmentHostArmorItem)) continue;

            switch (slot) {
                case HEAD -> renderHelmet(pose, buf, light, playerModel, armor);
                case CHEST -> renderChestplate(pose, buf, light, playerModel, armor);
                case LEGS -> renderLeggings(pose, buf, light, playerModel, armor);
                case FEET -> renderBoots(pose, buf, light, playerModel, armor);
            }
        }
    }

    // ── 渲染辅助 ──────────────────────────────────────────────

    /** 在 body part 锚点处施加旋转，然后平移回原点。
     *  Blockbench 模型自带 PartPose.offset，设计在实体原点渲染。 */
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
                              HumanoidModel<Player> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerHoodModel<>(models.bakeLayer(EngineerHoodModel.LAYER_LOCATION)),
                HELMET_TEX, m.head);
        renderAttachments(armor, pose, buf, light, m.head);
    }

    private void renderChestplate(PoseStack pose, MultiBufferSource buf, int light,
                                  HumanoidModel<Player> m, ItemStack armor) {
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
                                HumanoidModel<Player> m, ItemStack armor) {
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
                             HumanoidModel<Player> m, ItemStack armor) {
        renderModel(pose, buf, light,
                new EngineerBootsRightModel<>(models.bakeLayer(EngineerBootsRightModel.LAYER_LOCATION)),
                BOOTS_TEX, m.rightLeg);
        renderModel(pose, buf, light,
                new EngineerBootsLeftModel<>(models.bakeLayer(EngineerBootsLeftModel.LAYER_LOCATION)),
                BOOTS_TEX, m.leftLeg);
        renderAttachments(armor, pose, buf, light, m.rightLeg);
    }

    /** 渲染左右臂附件模型（如额外机械臂） */
    private void renderArmAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                      int light, ModelPart rightArm, ModelPart leftArm) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            EntityModel<?> leftModel = attachment.createLeftArmModel(models);
            ResourceLocation leftTex = attachment.getLeftArmTexture();
            if (leftModel != null && leftTex != null) {
                pose.pushPose();
                rotateAtPart(pose, leftArm);
                leftModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(leftTex)),
                        light, OverlayTexture.NO_OVERLAY, -1);
                pose.popPose();
            }

            EntityModel<?> rightModel = attachment.createRightArmModel(models);
            ResourceLocation rightTex = attachment.getRightArmTexture();
            if (rightModel != null && rightTex != null) {
                pose.pushPose();
                rotateAtPart(pose, rightArm);
                rightModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(rightTex)),
                        light, OverlayTexture.NO_OVERLAY, -1);
                pose.popPose();
            }
        }
    }

    /** 渲染左右腿附件模型（如外骨骼辅助设备）。右腿通过 X 轴镜像复用左腿模型。 */
    private void renderLegAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                      int light, ModelPart rightLeg, ModelPart leftLeg) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            EntityModel<?> legModel = attachment.createLeftLegModel(models);
            ResourceLocation legTex = attachment.getLeftLegTexture();
            if (legModel == null || legTex == null) continue;

            // 左腿
            pose.pushPose();
            rotateAtPart(pose, leftLeg);
            legModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();

            // 右腿 — X 轴镜像
            pose.pushPose();
            rotateAtPart(pose, rightLeg);
            pose.scale(-1, 1, 1);
            legModel.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(legTex)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();
        }
    }

    // ── 附件渲染（通用路径）──────────────────────────────────

    /** 遍历已安装附件，调用各附件自己的模型和贴图接口进行渲染 */
    private void renderAttachments(ItemStack armor, PoseStack pose, MultiBufferSource buf,
                                   int light, ModelPart part) {
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        AttachmentsData data = host.getAttachments(armor);

        for (var entry : data.slots().entrySet()) {
            ItemStack attachmentStack = entry.getValue();
            if (!(attachmentStack.getItem() instanceof IAttachment attachment)) continue;

            EntityModel<?> model = attachment.createAttachmentModel(models);
            ResourceLocation texture = attachment.getAttachmentTexture();
            if (model == null || texture == null) continue;

            pose.pushPose();
            rotateAtPart(pose, part);
            model.renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(texture)),
                    light, OverlayTexture.NO_OVERLAY, -1);
            pose.popPose();
        }
    }
}
