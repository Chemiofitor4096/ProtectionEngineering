package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.DivingDeviceAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 潜水设备 —— 嘴部附件，提供水下呼吸 II。
 */
public class DivingDeviceItem extends AttachmentItem {

    private static final ResourceLocation TEXTURE =
            ProtectionEngineering.asResource("textures/models/armor/diving_device.png");

    public DivingDeviceItem(Properties properties) {
        super(properties, SlotTypes.MOUTH);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.diving_device";
    }

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new DivingDeviceAttachmentModel<>(
                modelSet.bakeLayer(DivingDeviceAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEXTURE;
    }

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        // 每 tick 续 11 秒水下呼吸 II
        entity.addEffect(new MobEffectInstance(
                MobEffects.WATER_BREATHING, 220, 1, false, false, true));
    }

    @Override
    public void onUnequip(ItemStack attachment, ItemStack host, LivingEntity entity) {
        entity.removeEffect(MobEffects.WATER_BREATHING);
    }
}
