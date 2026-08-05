package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 防火内衬 — 通用 LINING 槽位附件，仅减免火焰类伤害。
 * <p>
 * 用 {@code DamageTypeTags.IS_FIRE} 而非逐个枚举 DamageType：火伤有
 * in_fire / on_fire / lava / hot_floor / fireball / unattributed_fireball /
 * campfire 共 7 种，且模组会往该标签追加自己的火伤，枚举容易漏。
 */
public class FireLiningItem extends AttachmentItem {

    private static final Set<TagKey<DamageType>> FIRE_TAGS = Set.of(DamageTypeTags.IS_FIRE);

    public FireLiningItem(Properties properties) {
        super(properties, SlotTypes.LINING);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.fire_lining";
    }

    @Override
    public Set<TagKey<DamageType>> getProtectedDamageTypeTags() {
        return FIRE_TAGS;
    }

    @Override
    public float getDamageReduction() { return 0.10f; }
}
