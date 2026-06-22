package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

import javax.annotation.Nullable;

/**
 * 缓冲鞋底 —— 足部附件，摔落伤害 -20%，等效摔落高度 -1（5 格下落才会受伤）。
 */
public class CushionedSolesItem extends AttachmentItem {

    public CushionedSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.cushioned_soles";
    }

    @Override
    public float getFallDamageReduction() { return 0.20f; }

    @Override
    public float getFallDistanceReduction() { return 1.0f; }
}
