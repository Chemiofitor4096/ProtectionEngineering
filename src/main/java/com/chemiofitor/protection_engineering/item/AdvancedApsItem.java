package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.model.AdvancedApsAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * 高级主动防御系统 —— 肩部附件，15 秒内免疫投射物并爆炸反击。
 */
public class AdvancedApsItem extends ApsItem {

    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/advanced_aps.png");

    public AdvancedApsItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.advanced_aps";
    }

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new AdvancedApsAttachmentModel<>(
                modelSet.bakeLayer(AdvancedApsAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() { return TEX; }
}
