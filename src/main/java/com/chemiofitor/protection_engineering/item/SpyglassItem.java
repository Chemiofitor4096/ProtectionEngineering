package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.SpyglassAttachmentModel;
import com.chemiofitor.protection_engineering.registry.PEDataComponents;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 单筒望远镜 —— 眼部附件，可按键开关。
 * 激活时缩放视野（同原版望远镜）。
 */
public class SpyglassItem extends AttachmentItem {

    private static final ResourceLocation TEXTURE =
            ProtectionEngineering.asResource("textures/models/armor/spyglass.png");

    public SpyglassItem(Properties properties) {
        super(properties, SlotTypes.EYES);
    }

    /** 默认关闭，需手动开启 */
    @Override
    public void onEquip(ItemStack attachment, ItemStack host, LivingEntity entity) {
        if (!attachment.has(PEDataComponents.ATTACHMENT_STATE.get())) {
            setState(attachment, STATE_DISABLED);
        }
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.FREE_TOGGLE; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.spyglass";
    }

    // ── 3D 渲染 ──────────────────────────────────────────────

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new SpyglassAttachmentModel<>(
                modelSet.bakeLayer(SpyglassAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEXTURE;
    }
}
