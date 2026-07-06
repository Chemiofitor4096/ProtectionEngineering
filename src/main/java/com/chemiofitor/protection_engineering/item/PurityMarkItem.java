package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;

/**
 * 纯洁印记 — 胸甲槽位装饰附件，无功能效果。
 */
public class PurityMarkItem extends AttachmentItem {

    public PurityMarkItem(Properties properties) {
        super(properties, SlotTypes.CHESTPLATE_DECORATION);
    }
}

