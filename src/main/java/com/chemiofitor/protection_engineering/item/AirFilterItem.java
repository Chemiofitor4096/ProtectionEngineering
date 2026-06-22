package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.AirFilterAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

import java.util.Set;

/**
 * 空气过滤器 —— 嘴部附件，免疫剧毒/反胃/虚弱/挖掘疲劳。
 */
public class AirFilterItem extends AttachmentItem {

    private static final ResourceLocation TEXTURE =
            ProtectionEngineering.asResource("textures/models/armor/air_filter.png");

    public AirFilterItem(Properties properties) {
        super(properties, Set.of(MobEffects.POISON, MobEffects.CONFUSION,
                MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN), SlotTypes.MOUTH);
    }

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new AirFilterAttachmentModel<>(
                modelSet.bakeLayer(AirFilterAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEXTURE;
    }
}
