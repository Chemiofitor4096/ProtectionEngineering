package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 防爆内衬 — 通用 LINING 槽位附件，仅减免爆炸伤害。
 * <p>
 * 用 {@code DamageTypeTags.IS_EXPLOSION} 而非逐个枚举：爆炸伤害含
 * explosion / player_explosion / fireworks / bad_respawn_point 共 4 种，
 * 与原版爆炸保护附魔的判断方式完全同源，模组追加的爆炸类型也会自动覆盖。
 */
public class BlastLiningItem extends AttachmentItem {

    private static final Set<TagKey<DamageType>> EXPLOSION_TAGS = Set.of(DamageTypeTags.IS_EXPLOSION);

    public BlastLiningItem(Properties properties) {
        super(properties, SlotTypes.LINING);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.blast_lining";
    }

    @Override
    public Set<TagKey<DamageType>> getProtectedDamageTypeTags() {
        return EXPLOSION_TAGS;
    }

    @Override
    public float getDamageReduction() { return 0.10f; }
}
