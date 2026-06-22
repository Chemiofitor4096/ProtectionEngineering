package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.Set;

/**
 * 简单的附件物品 —— 无额外行为逻辑的具体类。
 * <p>
 * 继承 {@link AttachmentItem}，仅传递槽位和免疫信息。
 * 用于 Registrate 注册物品时空壳占位。
 */
public class SimpleAttachmentItem extends AttachmentItem {

    /** 无免疫效果的附件 */
    public SimpleAttachmentItem(Properties properties, SlotType... slots) {
        super(properties, slots);
    }

    /** 带状态效果免疫的附件 */
    public SimpleAttachmentItem(Properties properties, Set<Holder<MobEffect>> immunities, SlotType... slots) {
        super(properties, immunities, slots);
    }
}
