package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 通用减伤内衬 — LINING 槽位附件，仅减免指定标签（如爆炸/火焰）的伤害。
 * <p>
 * 防爆内衬（{@code IS_EXPLOSION}）与防火内衬（{@code IS_FIRE}）共用此类，注册时传入标签与 feature key。
 */
public class LiningItem extends AttachmentItem {

    private final Set<TagKey<DamageType>> protectedTags;
    private final String featureKey;

    public LiningItem(Properties properties, Set<TagKey<DamageType>> protectedTags, String featureKey) {
        super(properties, SlotTypes.LINING);
        this.protectedTags = Set.copyOf(protectedTags);
        this.featureKey = featureKey;
    }

    @Override
    public Set<TagKey<DamageType>> getProtectedDamageTypeTags() {
        return protectedTags;
    }

    @Override
    public float getDamageReduction() { return 0.10f; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return featureKey;
    }
}
