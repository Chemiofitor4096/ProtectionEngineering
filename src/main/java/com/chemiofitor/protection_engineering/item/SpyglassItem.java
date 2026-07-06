package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.registry.PEDataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 单筒望远镜 —— 眼部附件，可按键开关。
 * 激活时缩放视野（同原版望远镜）。
 */
public class SpyglassItem extends AttachmentItem {

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
}
