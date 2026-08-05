package com.chemiofitor.protection_engineering.compat.iron;

import com.tterrag.registrate.util.entry.ItemEntry;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Rarity;

import java.util.Set;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.REGISTRATE;

/**
 * 铁魔法学派内衬的注册点 — 仅在铁魔法加载时由 {@link IronCompat} 调用。
 * <p>
 * 9 个学派各一个 LINING 内衬，减免对应学派的魔法伤害。
 * 条件注册保证：铁魔法缺席时物品完全不存在，避免指令掏出无法生效的坏物品。
 */
public class IronCompatItems {

    public static final ItemEntry<SchoolLiningItem> FIRE_MAGIC_LINING =
            lining("fire_magic_lining", ISSDamageTypes.FIRE_MAGIC);
    public static final ItemEntry<SchoolLiningItem> ICE_MAGIC_LINING =
            lining("ice_magic_lining", ISSDamageTypes.ICE_MAGIC);
    public static final ItemEntry<SchoolLiningItem> LIGHTNING_MAGIC_LINING =
            lining("lightning_magic_lining", ISSDamageTypes.LIGHTNING_MAGIC);
    public static final ItemEntry<SchoolLiningItem> HOLY_MAGIC_LINING =
            lining("holy_magic_lining", ISSDamageTypes.HOLY_MAGIC);
    public static final ItemEntry<SchoolLiningItem> ENDER_MAGIC_LINING =
            lining("ender_magic_lining", ISSDamageTypes.ENDER_MAGIC);
    public static final ItemEntry<SchoolLiningItem> BLOOD_MAGIC_LINING =
            lining("blood_magic_lining", ISSDamageTypes.BLOOD_MAGIC);
    public static final ItemEntry<SchoolLiningItem> EVOCATION_MAGIC_LINING =
            lining("evocation_magic_lining", ISSDamageTypes.EVOCATION_MAGIC);
    public static final ItemEntry<SchoolLiningItem> ELDRITCH_MAGIC_LINING =
            lining("eldritch_magic_lining", ISSDamageTypes.ELDRITCH_MAGIC);
    public static final ItemEntry<SchoolLiningItem> NATURE_MAGIC_LINING =
            lining("nature_magic_lining", ISSDamageTypes.NATURE_MAGIC);

    private static ItemEntry<SchoolLiningItem> lining(String id, ResourceKey<DamageType> type) {
        return REGISTRATE
                .item(id, p -> new SchoolLiningItem(p, Set.of(type),
                        "tooltip.protectionengineering.feature." + id))
                .properties(p -> p.stacksTo(1).rarity(Rarity.RARE))
                .register();
    }

    /** 触发类加载，执行所有静态字段的注册。 */
    public static void init() {}
}
