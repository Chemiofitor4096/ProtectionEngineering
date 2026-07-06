package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.item.*;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ArmorItem;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.REGISTRATE;
import static com.chemiofitor.protection_engineering.registry.PEArmorMaterials.DURABILITY_FACTOR;
import static com.chemiofitor.protection_engineering.registry.PEArmorMaterials.ENGINEER_ARMOR;

/**
 * 所有物品的集中注册点。
 * <p>
 * 包含 4 件基础护甲和 19 个附件物品（空壳占位）。
 * 使用 Registrate 自动生成模型 (defaultModel) 和本地化 (defaultLang)。
 */
@SuppressWarnings("unused")
public class PEItems {

    // ══════════════════════════════════════════════════════════════
    //  基础护甲 (4)
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<EngineerHoodItem> ENGINEER_HOOD = REGISTRATE
            .item("engineer_hood", p -> new EngineerHoodItem(ENGINEER_ARMOR,
                    p.durability(ArmorItem.Type.HELMET.getDurability(DURABILITY_FACTOR))))
            .properties(p -> p.stacksTo(1))
            .clientExtension(() -> () -> com.chemiofitor.protection_engineering.client.PEClientExtensions.INSTANCE)
            .register();

    public static final ItemEntry<EngineerChestplateItem> ENGINEER_CHESTPLATE = REGISTRATE
            .item("engineer_chestplate", p -> new EngineerChestplateItem(ENGINEER_ARMOR,
                    p.durability(ArmorItem.Type.CHESTPLATE.getDurability(DURABILITY_FACTOR))))
            .properties(p -> p.stacksTo(1))
            .clientExtension(() -> () -> com.chemiofitor.protection_engineering.client.PEClientExtensions.INSTANCE)
            .register();

    public static final ItemEntry<EngineerLeggingsItem> ENGINEER_LEGGINGS = REGISTRATE
            .item("engineer_leggings", p -> new EngineerLeggingsItem(ENGINEER_ARMOR,
                    p.durability(ArmorItem.Type.LEGGINGS.getDurability(DURABILITY_FACTOR))))
            .properties(p -> p.stacksTo(1))
            .clientExtension(() -> () -> com.chemiofitor.protection_engineering.client.PEClientExtensions.INSTANCE)
            .register();

    public static final ItemEntry<EngineerBootsItem> ENGINEER_BOOTS = REGISTRATE
            .item("engineer_boots", p -> new EngineerBootsItem(ENGINEER_ARMOR,
                    p.durability(ArmorItem.Type.BOOTS.getDurability(DURABILITY_FACTOR))))
            .properties(p -> p.stacksTo(1))
            .clientExtension(() -> () -> com.chemiofitor.protection_engineering.client.PEClientExtensions.INSTANCE)
            .register();

    // ══════════════════════════════════════════════════════════════
    //  头盔附件 (5) — EYES / MOUTH
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<AirFilterItem> AIR_FILTER = REGISTRATE
            .item("air_filter", AirFilterItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<DivingDeviceItem> DIVING_DEVICE = REGISTRATE
            .item("diving_device", DivingDeviceItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<EngineerGogglesItem> ENGINEER_GOGGLES = REGISTRATE
            .item("engineer_goggles", EngineerGogglesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<NightVisionGogglesItem> NIGHT_VISION_GOGGLES = REGISTRATE
            .item("night_vision_goggles", NightVisionGogglesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<HormoneInjectorItem> HORMONE_INJECTOR = REGISTRATE
            .item("hormone_injector", HormoneInjectorItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<SpyglassItem> SPYGLASS = REGISTRATE
            .item("spyglass", SpyglassItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  胸甲附件 — 肩部 (3) — SHOULDER
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<ApsItem> APS = REGISTRATE
            .item("aps", ApsItem::new)
            .lang("Active Protection System")
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<AdvancedApsItem> ADVANCED_APS = REGISTRATE
            .item("advanced_aps", AdvancedApsItem::new)
            .lang("Advanced Active Protection System")
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<RocketLauncherItem> ROCKET_LAUNCHER = REGISTRATE
            .item("rocket_launcher", RocketLauncherItem::new)
            .lang("Rocket Launcher")
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  胸甲附件 — 胸甲板 (2) — CHESTPLATE
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<SturdyPlateItem> STURDY_PLATE = REGISTRATE
            .item("sturdy_plate", SturdyPlateItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<NetheritePlateItem> NETHERITE_PLATE = REGISTRATE
            .item("netherite_plate", NetheritePlateItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<PurityMarkItem> PURITY_MARK = REGISTRATE
            .item("purity_mark", PurityMarkItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  胸甲附件 — 背部 (5) — BACK
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<JetpackItem> JETPACK = REGISTRATE
            .item("jetpack", JetpackItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<MomentumJetpackItem> MOMENTUM_JETPACK = REGISTRATE
            .item("momentum_jetpack", MomentumJetpackItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<DodgeJetpackItem> DODGE_JETPACK = REGISTRATE
            .item("dodge_jetpack", DodgeJetpackItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<MissilePackItem> MISSILE_PACK = REGISTRATE
            .item("missile_pack", MissilePackItem::new)
            .lang("Portable Missile Backpack")
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<MissileItem> MISSILE = REGISTRATE
            .item("missile", MissileItem::new)
            .lang("Guided Missile")
            .properties(p -> p.stacksTo(16))
            .register();

    public static final ItemEntry<EngineerSawSwordItem> ENGINEER_SAW_SWORD = REGISTRATE
            .item("engineer_saw_sword", EngineerSawSwordItem::new)
            .lang("Engineer Saw Sword")
            .model(AssetLookup.existingItemModel())
            .properties(p -> p.stacksTo(1).fireResistant())
            .register();

    // ══════════════════════════════════════════════════════════════
    //  胸甲附件 — 手臂 (2) — ARM
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<ExtraMechanicalArmItem> EXTRA_MECHANICAL_ARM = REGISTRATE
            .item("extra_mechanical_arm", ExtraMechanicalArmItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<MechaKnuckleItem> MECHANICAL_GAUNTLET = REGISTRATE
            .item("mechanical_gauntlet", MechaKnuckleItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  腿甲附件 (3) — LEG / KNEE
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<LightExoskeletonItem> LIGHT_EXOSKELETON = REGISTRATE
            .item("light_exoskeleton", LightExoskeletonItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<HeavyExoskeletonItem> HEAVY_EXOSKELETON = REGISTRATE
            .item("heavy_exoskeleton", HeavyExoskeletonItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<CushionedKneecapItem> CUSHIONED_KNEECAP = REGISTRATE
            .item("cushioned_kneecap", CushionedKneecapItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  通用附件 (1) — LINING
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<BlastLiningItem> BLAST_LINING = REGISTRATE
            .item("blast_lining", BlastLiningItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  靴子附件 (2) — FOOT
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<ImprovedSolesItem> IMPROVED_SOLES = REGISTRATE
            .item("improved_soles", ImprovedSolesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<CushionedSolesItem> CUSHIONED_SOLES = REGISTRATE
            .item("cushioned_soles", CushionedSolesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<InsulatedSolesItem> INSULATED_SOLES = REGISTRATE
            .item("insulated_soles", InsulatedSolesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<SilentSolesItem> SILENT_SOLES = REGISTRATE
            .item("silent_soles", SilentSolesItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  武器 / 工具 (2)
    // ══════════════════════════════════════════════════════════════

    public static final ItemEntry<EngineerShieldItem> ENGINEER_SHIELD = REGISTRATE
            .item("engineer_shield", p -> new EngineerShieldItem(
                    p.durability(1008)))  // 336 × 3
            .lang("Engineer Shield")
            .model(AssetLookup.existingItemModel())
            .properties(p -> p.stacksTo(1))
            .register();

    // ══════════════════════════════════════════════════════════════
    //  初始化
    // ══════════════════════════════════════════════════════════════

    /** 触发类加载，执行所有静态字段的注册。在 Mod 构造器中调用。 */
    public static void init() {}
}
