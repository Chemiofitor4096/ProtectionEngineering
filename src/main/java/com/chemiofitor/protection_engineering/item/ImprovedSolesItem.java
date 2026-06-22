package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

import javax.annotation.Nullable;

/**
 * 改良鞋底 —— 足部附件。
 * 冰面不打滑、不受粘液块/蜂蜜块影响、不受灵魂沙减速、可在细雪上行走。
 */
public class ImprovedSolesItem extends AttachmentItem {

    public ImprovedSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.improved_soles";
    }
}
