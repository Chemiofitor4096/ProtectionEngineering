package com.chemiofitor.protection_engineering.item;

import javax.annotation.Nullable;

/**
 * 高级主动防御系统 —— 肩部附件，15 秒内免疫投射物并爆炸反击。
 */
public class AdvancedApsItem extends ApsItem {

    public AdvancedApsItem(Properties properties) {
        super(properties);
    }

    @Override
    protected double durationMultiplier() { return 1.5; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.advanced_aps";
    }
}
