package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.effect.MobEffects;

import java.util.Set;

/**
 * 空气过滤器 —— 嘴部附件，免疫剧毒/反胃/虚弱/挖掘疲劳。
 */
public class AirFilterItem extends AttachmentItem {

    public AirFilterItem(Properties properties) {
        super(properties, Set.of(MobEffects.POISON, MobEffects.CONFUSION,
                MobEffects.WEAKNESS, MobEffects.DIG_SLOWDOWN), SlotTypes.MOUTH);
    }
}
