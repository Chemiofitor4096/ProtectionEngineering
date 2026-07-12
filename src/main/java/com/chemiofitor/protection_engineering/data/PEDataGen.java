package com.chemiofitor.protection_engineering.data;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.REGISTRATE;

/**
 * 数据生成入口 — 语言文件 + 配方 + 动力合成配方。
 */
public class PEDataGen {

    public static void gatherDataHighPriority(GatherDataEvent event) {
        addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        if (event.includeServer()) {
            generator.addProvider(true, new PERecipeProvider(output, event.getLookupProvider()));
            generator.addProvider(true, new PEMechanicalCraftingRecipeGen(output, event.getLookupProvider()));
        }
    }

    private static void addExtraRegistrateData() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> add = provider::add;

            // ── Item Group ─────────────────────────────────────────
            add.accept("itemGroup.protectionengineering", "Protection Engineering");

            // ── Slots ──────────────────────────────────────────────
            add.accept("slot.protectionengineering.eyes", "Eyes");
            add.accept("slot.protectionengineering.mouth", "Mouth");
            add.accept("slot.protectionengineering.shoulder", "Shoulder");
            add.accept("slot.protectionengineering.chestplate", "Chestplate");
            add.accept("slot.protectionengineering.back", "Back");
            add.accept("slot.protectionengineering.arm", "Arm");
            add.accept("slot.protectionengineering.leg", "Leg");
            add.accept("slot.protectionengineering.knee", "Knee");
            add.accept("slot.protectionengineering.foot", "Foot");
            add.accept("slot.protectionengineering.lining", "Lining");
            add.accept("slot.protectionengineering.helmet_decoration", "Helmet Decoration");
            add.accept("slot.protectionengineering.chestplate_decoration", "Chestplate Decoration");
            add.accept("slot.protectionengineering.leggings_decoration", "Leggings Decoration");
            add.accept("slot.protectionengineering.boots_decoration", "Boots Decoration");
            add.accept("slot.protectionengineering.decoration", "Decoration");
            add.accept("slot.protectionengineering.blade", "Blade");
            add.accept("slot.protectionengineering.hilt", "Hilt");
            add.accept("slot.protectionengineering.guard", "Guard");

            // ── Tooltip Headers ────────────────────────────────────
            add.accept("tooltip.protectionengineering.slot", "Slot:");
            add.accept("tooltip.protectionengineering.installed_attachments", "Attachments:");
            add.accept("tooltip.protectionengineering.immunities", "Immunities:");
            add.accept("tooltip.protectionengineering.feature", "Feature:");
            add.accept("tooltip.protectionengineering.prev_page", "Previous Page");
            add.accept("tooltip.protectionengineering.next_page", "Next Page");

            // ── Feature Tooltips ───────────────────────────────────
            add.accept("tooltip.protectionengineering.feature.night_vision", "Night vision (toggle)");
            add.accept("tooltip.protectionengineering.feature.engineer_goggles", "Engineering data overlay");
            add.accept("tooltip.protectionengineering.feature.hormone_injector", "Regen & Resistance 10s · Nausea 5s · 60s CD");
            add.accept("tooltip.protectionengineering.feature.diving_device", "Water Breathing II");
            add.accept("tooltip.protectionengineering.feature.extra_mechanical_arm", "Reach +2");
            add.accept("tooltip.protectionengineering.feature.sturdy_plate", "Armor +2 · Damage -10%");
            add.accept("tooltip.protectionengineering.feature.netherite_plate", "Armor +4 · Damage -15%");
            add.accept("tooltip.protectionengineering.feature.mecha_knuckle", "Melee +20%");
            add.accept("tooltip.protectionengineering.feature.jetpack", "Elytra flight · J thrust");
            add.accept("tooltip.protectionengineering.feature.momentum_jetpack", "Elytra · Speed +20% · J thrust");
            add.accept("tooltip.protectionengineering.feature.dodge_jetpack", "Melee immune & dash · 10s CD");
            add.accept("tooltip.protectionengineering.feature.heavy_exoskeleton", "Armor +2 · Step up 1 · Fall Damage -20%");
            add.accept("tooltip.protectionengineering.feature.light_exoskeleton", "Speed +10% · Jump +0.5 · Step up 1");
            add.accept("tooltip.protectionengineering.feature.cushioned_kneecap", "Fall Damage -10%");
            add.accept("tooltip.protectionengineering.feature.springy_kneecap", "Jump +0.25");
            add.accept("tooltip.protectionengineering.feature.cushioned_soles", "Fall Damage -20% · Height -1");
            add.accept("tooltip.protectionengineering.feature.improved_soles", "No-slip · Immune slime/honey/soul sand · Powder snow walk");
            add.accept("tooltip.protectionengineering.feature.insulated_soles", "Immune to ground heat damage");
            add.accept("tooltip.protectionengineering.feature.frost_soles", "Frost Walker on water");
            add.accept("tooltip.protectionengineering.feature.silent_soles", "Silent movement · Evades Warden");
            add.accept("tooltip.protectionengineering.feature.blast_lining", "Damage -10%");
            add.accept("tooltip.protectionengineering.feature.aps", "Toggle · Intercept 10s · 30s CD");
            add.accept("tooltip.protectionengineering.feature.advanced_aps", "Toggle · Intercept 15s · 30s CD");
            add.accept("tooltip.protectionengineering.feature.rocket_launcher", "Consumes fireworks · 12-shot scatter · 10s CD");
            add.accept("tooltip.protectionengineering.feature.missile", "Lock target · Tracking missile · 60s CD");
            add.accept("tooltip.protectionengineering.feature.spyglass", "Zoom (toggle)");

            // ── Subtitles ──────────────────────────────────────────
            add.accept("subtitles.protectionengineering.equip_engineer_armor", "Engineer armor clanks");
            add.accept("subtitles.protectionengineering.night_vision_on", "Night vision activates");
            add.accept("subtitles.protectionengineering.night_vision_off", "Night vision deactivates");
            add.accept("subtitles.protectionengineering.hormone_inject", "Hormone injector hisses");
            add.accept("subtitles.protectionengineering.thrust_jetpack", "Jetpack thrusts");
            add.accept("subtitles.protectionengineering.dodge_warning", "Dodge pack: Alert");
            add.accept("subtitles.protectionengineering.dodge_jet", "Dodge pack: Jet");

            // ── Key Bindings ───────────────────────────────────────
            add.accept("key.categories.protectionengineering", "Protection Engineering");
            add.accept("key.protectionengineering.toggle_night_vision", "Toggle Night Vision");
            add.accept("key.protectionengineering.activate_hormone", "Activate Hormone Injector");
            add.accept("key.protectionengineering.thrust_jetpack", "Jetpack Thrust");
            add.accept("key.protectionengineering.toggle_aps", "Toggle APS");
            add.accept("key.protectionengineering.activate_rocket_launcher", "Activate Rocket Launcher");
            add.accept("key.protectionengineering.activate_missile", "Launch Guided Missile");
            add.accept("key.protectionengineering.toggle_spyglass", "Toggle Spyglass");

            // ── HUD ────────────────────────────────────────────────
            add.accept("hud.protectionengineering.cooldown_title", "⚙ Attachment Status");

            // ── Messages: Hormone ──────────────────────────────────
            add.accept("message.protectionengineering.hormone_used", "§aHormone injector activated");
            add.accept("message.protectionengineering.hormone_cooldown", "§cCooling down… %s s");
            add.accept("message.protectionengineering.hormone_ready", "§aHormone injector ready");

            // ── Messages: Dodge ────────────────────────────────────
            add.accept("message.protectionengineering.dodge_activated", "§bDodge pack activated! Damage immune");
            add.accept("message.protectionengineering.dodge_cooldown", "§cDodge pack cooling… %s s");
            add.accept("message.protectionengineering.dodge_danger", "§cHostile nearby, dodge not triggered");

            // ── Messages: APS ──────────────────────────────────────
            add.accept("message.protectionengineering.aps_activated", "§aAPS activated");
            add.accept("message.protectionengineering.aps_cooldown_start", "§eAPS entering cooldown");
            add.accept("message.protectionengineering.aps_cooldown", "§cCooling down… %s s");
            add.accept("message.protectionengineering.aps_ready", "§aAPS ready");

            // ── Messages: Rocket Launcher ──────────────────────────
            add.accept("message.protectionengineering.rocket_launcher_activated", "§aRocket launched!");
            add.accept("message.protectionengineering.rocket_launcher_cooldown", "§cRocket launcher cooling… %s s");
            add.accept("message.protectionengineering.rocket_launcher_ready", "§aRocket launcher ready");

            // ── Messages: Missile ──────────────────────────────────
            add.accept("message.protectionengineering.missile_target", "§cTarget locked: §f%s");
            add.accept("message.protectionengineering.missile_empty", "§cNo guided missile in inventory!");
            add.accept("message.protectionengineering.missile_no_target", "§cNo valid target locked!");
            add.accept("message.protectionengineering.missile_activated", "§aMissile launched!");
            add.accept("message.protectionengineering.missile_cooldown", "§cMissile system cooling… %s s");
            add.accept("message.protectionengineering.missile_ready", "§aMissile system ready");

            // ── Misc Tooltips ──────────────────────────────────────
            add.accept("tooltip.protectionengineering.missile_item", "Ammunition for the Portable Missile Backpack");
        });
    }
}
