package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 防爆内衬 — 通用 LINING 槽位附件，仅减免爆炸伤害 + 击退抗性。
 */
public class BlastLiningItem extends AttachmentItem {

    private static final ResourceLocation ARMOR_ID =
            ProtectionEngineering.asResource("blast_lining_armor");

    private static final Set<ResourceKey<DamageType>> EXPLOSION_TYPES = Set.of(
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.withDefaultNamespace("explosion")),
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.withDefaultNamespace("player_explosion"))
    );

    public BlastLiningItem(Properties properties) {
        super(properties, SlotTypes.LINING);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.blast_lining";
    }

    @Override
    public Set<ResourceKey<DamageType>> getProtectedDamageTypes() {
        return EXPLOSION_TYPES;
    }

    @Override
    public float getDamageReduction() { return 0.10f; }
}
