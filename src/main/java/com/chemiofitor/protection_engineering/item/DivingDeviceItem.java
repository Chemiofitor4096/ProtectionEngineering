package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 潜水设备 —— 嘴部附件，提供水下呼吸 II。
 */
public class DivingDeviceItem extends AttachmentItem {

    public DivingDeviceItem(Properties properties) {
        super(properties, SlotTypes.MOUTH);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.diving_device";
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
