package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

import javax.annotation.Nullable;

/**
 * 缓冲护膝 —— 膝部附件，摔落伤害 -10%。
 */
public class CushionedKneecapItem extends AttachmentItem {

    public CushionedKneecapItem(Properties properties) {
        super(properties, SlotTypes.KNEE);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.cushioned_kneecap";
    }

    @Override
    public float getFallDamageReduction() { return 0.10f; }
}
