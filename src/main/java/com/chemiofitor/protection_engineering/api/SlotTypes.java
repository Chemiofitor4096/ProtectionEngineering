package com.chemiofitor.protection_engineering.api;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;

/**
 * 内置槽位类型常量池。
 * <p>
 * 所有槽位在类加载时注册到 {@link SlotType#registry()}。
 * 添加新槽位只需在此处加一行 {@code public static final SlotType XXX = register(...)}。
 * <p>
 * 第三方可通过 {@link SlotType#register(ResourceLocation, SlotType.SlotCategory)} 注册自定义槽位，
 * 无需修改此文件。
 */
public final class SlotTypes {
    private SlotTypes() {}

    // ────────────────────────────────────────────────────────────
    //  头盔槽位 → 头盔装备槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType EYES       = register("eyes",       SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.HEAD);
    public static final SlotType MOUTH      = register("mouth",      SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.HEAD);

    // ────────────────────────────────────────────────────────────
    //  胸甲槽位 → 胸甲装备槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType SHOULDER   = register("shoulder",   SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.CHEST);
    public static final SlotType CHESTPLATE = register("chestplate", SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.CHEST);
    public static final SlotType BACK       = register("back",       SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.CHEST);
    public static final SlotType ARM        = register("arm",         SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.CHEST);

    // ────────────────────────────────────────────────────────────
    //  护腿槽位 → 护腿装备槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType LEG        = register("leg",        SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.LEGS);
    public static final SlotType KNEE       = register("knee",       SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.LEGS);

    // ────────────────────────────────────────────────────────────
    //  靴子槽位 → 靴子装备槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType FOOT       = register("foot",       SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.FEET);

    // ────────────────────────────────────────────────────────────
    //  通用槽位 (所有护甲均有) → 任意装备槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType LINING               = register("lining",                SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.ANY);
    public static final SlotType HELMET_DECORATION    = register("helmet_decoration",     SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.ANY);
    public static final SlotType CHESTPLATE_DECORATION = register("chestplate_decoration", SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.ANY);
    public static final SlotType LEGGINGS_DECORATION  = register("leggings_decoration",   SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.ANY);
    public static final SlotType BOOTS_DECORATION     = register("boots_decoration",      SlotType.SlotCategory.ARMOR, EquipmentSlotGroup.ANY);

    // ────────────────────────────────────────────────────────────
    //  武器槽位 (预留 —— 锯剑等) → 任意手持槽
    // ────────────────────────────────────────────────────────────
    public static final SlotType BLADE      = register("blade",      SlotType.SlotCategory.WEAPON, EquipmentSlotGroup.HAND);
    public static final SlotType HILT       = register("hilt",       SlotType.SlotCategory.WEAPON, EquipmentSlotGroup.HAND);
    public static final SlotType GUARD      = register("guard",      SlotType.SlotCategory.WEAPON, EquipmentSlotGroup.HAND);

    // ── helpers ─────────────────────────────────────────────────

    private static SlotType register(String path, SlotType.SlotCategory category, EquipmentSlotGroup group) {
        return SlotType.register(ProtectionEngineering.asResource(path), category, group);
    }
}
