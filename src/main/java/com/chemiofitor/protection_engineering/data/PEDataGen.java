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

            add.accept("itemGroup.protectionengineering", "Protection Engineering");
            add.accept("slot.protectionengineering.eyes", "Eyes");
            add.accept("slot.protectionengineering.mouth", "Mouth");
            add.accept("slot.protectionengineering.shoulder", "Shoulder");
            add.accept("slot.protectionengineering.chestplate", "Chestplate");
            add.accept("slot.protectionengineering.back", "Back");
            add.accept("slot.protectionengineering.arm", "Arm");
            add.accept("slot.protectionengineering.leg", "Leg");
            add.accept("slot.protectionengineering.knee", "Knee");
            add.accept("slot.protectionengineering.foot", "Foot");
            add.accept("slot.protectionengineering.blade", "Blade");
            add.accept("slot.protectionengineering.hilt", "Hilt");
            add.accept("slot.protectionengineering.guard", "Guard");

            add.accept("tooltip.protectionengineering.slot", "Slot:");
            add.accept("tooltip.protectionengineering.installed_attachments", "Attachments:");
            add.accept("tooltip.protectionengineering.immunities", "Immunities:");
            add.accept("tooltip.protectionengineering.feature", "Feature:");

            add.accept("tooltip.protectionengineering.feature.night_vision", "Grants night vision (toggleable)");
            add.accept("tooltip.protectionengineering.feature.engineer_goggles", "Grants engineering data overlay");
            add.accept("tooltip.protectionengineering.feature.hormone_injector", "Grants Regeneration & Resistance (10s), Nausea (5s), cooldown (60s)");
            add.accept("tooltip.protectionengineering.feature.diving_device", "Grants Water Breathing II");
            add.accept("tooltip.protectionengineering.feature.extra_mechanical_arm", "Reach +2");
            add.accept("tooltip.protectionengineering.feature.sturdy_plate", "Armor +2 · 10% DMG reduction");
            add.accept("tooltip.protectionengineering.feature.netherite_plate", "Armor +4 · 15% DMG reduction");
            add.accept("tooltip.protectionengineering.feature.mecha_knuckle", "Melee damage +20%");
            add.accept("tooltip.protectionengineering.feature.jetpack", "Grants elytra flight · J to thrust");
            add.accept("tooltip.protectionengineering.feature.momentum_jetpack", "Grants elytra flight · Speed +20% · J soul-fire thrust");
            add.accept("tooltip.protectionengineering.feature.dodge_jetpack", "Immune to melee & dash · 10s CD");
            add.accept("tooltip.protectionengineering.feature.heavy_exoskeleton", "Armor +2 · Step up 1 block · Fall DMG -20%");
            add.accept("tooltip.protectionengineering.feature.light_exoskeleton", "Speed +10% · Jump +0.5 · Step up 1 block");
            add.accept("tooltip.protectionengineering.feature.cushioned_kneecap", "Fall DMG -10%");
            add.accept("tooltip.protectionengineering.feature.cushioned_soles", "Fall DMG -20% · Fall distance -1");
            add.accept("tooltip.protectionengineering.feature.improved_soles", "No-slip on ice · Immune to slime/honey · Immune to soul sand · Walk on powder snow");
            add.accept("tooltip.protectionengineering.feature.insulated_soles", "Immune to magma block damage");
            add.accept("tooltip.protectionengineering.feature.aps", "Manual toggle · 10s intercept · 30s CD");
            add.accept("tooltip.protectionengineering.feature.advanced_aps", "Manual toggle · 15s intercept · 30s CD");
            add.accept("tooltip.protectionengineering.feature.rocket_launcher", "Consumes fireworks · Scatters up to 12 rockets · 10s CD");
            add.accept("tooltip.protectionengineering.feature.missile", "Locks target · Launches tracking missile · 60s CD");
            add.accept("tooltip.protectionengineering.feature.spyglass", "Zoomable telescope (toggleable)");

            add.accept("subtitles.protectionengineering.equip_engineer_armor", "Engineer armor clanks");
            add.accept("subtitles.protectionengineering.night_vision_on", "Night vision activates");
            add.accept("subtitles.protectionengineering.night_vision_off", "Night vision deactivates");
            add.accept("subtitles.protectionengineering.hormone_inject", "Hormone injector hisses");
            add.accept("subtitles.protectionengineering.thrust_jetpack", "Jetpack thrusts");
            add.accept("subtitles.protectionengineering.dodge_warning", "Dodge pack: Alert");
            add.accept("subtitles.protectionengineering.dodge_jet", "Dodge pack: Jet");

            add.accept("key.categories.protectionengineering", "Protection Engineering");
            add.accept("key.protectionengineering.toggle_night_vision", "Toggle Night Vision");
            add.accept("key.protectionengineering.activate_hormone", "Activate Hormone Injector");
            add.accept("key.protectionengineering.thrust_jetpack", "Jetpack Thrust");
            add.accept("key.protectionengineering.toggle_aps", "Toggle APS");
            add.accept("key.protectionengineering.activate_rocket_launcher", "Activate Rocket Launcher");
            add.accept("key.protectionengineering.activate_missile", "Launch Guided Missile");
            add.accept("key.protectionengineering.toggle_spyglass", "Toggle Spyglass");

            add.accept("hud.protectionengineering.cooldown_title", "⚙ Attachment Status");

            add.accept("message.protectionengineering.missile_target", "Target locked: %s");
            add.accept("message.protectionengineering.missile_empty", "No guided missile in inventory!");
            add.accept("message.protectionengineering.missile_no_target", "No valid target locked!");

            add.accept("tooltip.protectionengineering.missile_item", "Ammunition for the Portable Missile Backpack");
        });
    }
}
