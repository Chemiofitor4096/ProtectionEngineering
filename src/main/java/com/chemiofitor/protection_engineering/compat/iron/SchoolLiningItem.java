package com.chemiofitor.protection_engineering.compat.iron;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.AttachmentItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * 铁魔法学派内衬 — 通用 LINING 槽位附件，仅减免指定学派的魔法伤害。
 * <p>
 * 伤害类型 key 由 {@link IronCompatItems} 注册时传入（铁魔法的 ISSDamageTypes.XXX_MAGIC）。
 * 物品仅在铁魔法加载时注册，因此 key 始终有效。
 */
public class SchoolLiningItem extends AttachmentItem {

    private final Set<ResourceKey<DamageType>> protectedTypes;
    private final String featureKey;

    public SchoolLiningItem(Properties properties,
                            Set<ResourceKey<DamageType>> protectedTypes,
                            String featureKey) {
        super(properties, SlotTypes.LINING);
        this.protectedTypes = Set.copyOf(protectedTypes);
        this.featureKey = featureKey;
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        // 法力上限 +50。基础修饰符 ID 按物品注册名派生（区分学派）；
        // 基类会再追加宿主部件后缀（chest/legs…），故跨护甲、跨学派均可堆叠。
        // （不参与护甲值合并：mergeArmorModifiers 只处理 ARMOR/ARMOR_TOUGHNESS）
        return List.of(IAttachment.bonus(
                "school_lining_mana_" + BuiltInRegistries.ITEM.getKey(this).getPath(),
                AttributeRegistry.MAX_MANA.get(), 50.0,
                AttributeModifier.Operation.ADDITION));
    }

    @Override
    public Set<ResourceKey<DamageType>> getProtectedDamageTypes() {
        return protectedTypes;
    }

    @Override
    public float getDamageReduction() { return 0.10f; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return featureKey;
    }
}
