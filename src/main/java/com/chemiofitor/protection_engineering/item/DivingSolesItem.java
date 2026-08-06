package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

import javax.annotation.Nullable;

/**
 * 潜水配重鞋底 —— 足部附件。
 * 被 Create 识别为潜水靴：水下可主动下沉、水平移动加速（配重效果）。
 */
public class DivingSolesItem extends AttachmentItem {

    public DivingSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.diving_soles";
    }
}
