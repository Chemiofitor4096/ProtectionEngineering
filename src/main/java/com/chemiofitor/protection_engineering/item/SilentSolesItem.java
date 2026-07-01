package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

import javax.annotation.Nullable;

/**
 * 静音鞋底 —— 足部附件。
 * 行走/落地/游泳/鞘翅滑翔时不会触发 Sculk 振动，Warden 无法通过振动探测到穿戴者。
 */
public class SilentSolesItem extends AttachmentItem {

    public SilentSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.silent_soles";
    }
}
