package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

/**
 * 隔热鞋底 — 脚部附件，隔绝岩浆块伤害。
 */
public class InsulatedSolesItem extends AttachmentItem {

    public InsulatedSolesItem(Properties properties) {
        super(properties, SlotTypes.FOOT);
    }

    @Override
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.insulated_soles";
    }
}
