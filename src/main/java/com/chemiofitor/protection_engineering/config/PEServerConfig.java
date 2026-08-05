package com.chemiofitor.protection_engineering.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 服务端配置 — APS、导弹等游戏玩法参数。
 */
public class PEServerConfig {

    public static final ModConfigSpec SPEC;

    // ── APS ──
    public static final ModConfigSpec.DoubleValue APS_INTERCEPT_RANGE;
    public static final ModConfigSpec.IntValue APS_ACTIVE_DURATION;
    public static final ModConfigSpec.IntValue APS_COOLDOWN_TICKS;

    // ── 导弹 ──
    public static final ModConfigSpec.DoubleValue MISSILE_TARGET_RANGE;
    public static final ModConfigSpec.DoubleValue MISSILE_FLIGHT_SPEED;
    public static final ModConfigSpec.DoubleValue MISSILE_CLOSE_SPEED;
    public static final ModConfigSpec.IntValue MISSILE_MAX_LIFE;

    // ── 火箭 ──
    public static final ModConfigSpec.IntValue ROCKET_COOLDOWN_TICKS;

    // ── 激素针 ──
    public static final ModConfigSpec.IntValue HORMONE_COOLDOWN_TICKS;

    // ── 应激背包 ──
    public static final ModConfigSpec.DoubleValue DODGE_STRENGTH;
    public static final ModConfigSpec.IntValue DODGE_COOLDOWN_TICKS;

    // ── 分级修补 ──
    public static final ModConfigSpec.IntValue REPAIR_UNITS_BRASS_SHEET;
    public static final ModConfigSpec.IntValue REPAIR_UNITS_STURDY_SHEET;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("aps");
        APS_INTERCEPT_RANGE = builder
                .comment("APS 巡航拦截半径（格）")
                .defineInRange("interceptRange", 5.0, 1.0, 32.0);
        APS_ACTIVE_DURATION = builder
                .comment("APS 激活窗口时长（tick）")
                .defineInRange("activeDuration", 200, 20, 1200);
        APS_COOLDOWN_TICKS = builder
                .comment("APS 冷却时长（tick）")
                .defineInRange("cooldownTicks", 600, 20, 3600);
        builder.pop();

        builder.push("missile");
        MISSILE_TARGET_RANGE = builder
                .comment("导弹锁定射程（格）")
                .defineInRange("targetRange", 512.0, 20.0, 1024.0);
        MISSILE_FLIGHT_SPEED = builder
                .comment("导弹巡航速度（block/tick）")
                .defineInRange("flightSpeed", 3.0, 1.0, 10.0);
        MISSILE_CLOSE_SPEED = builder
                .comment("导弹末端冲刺速度（block/tick）")
                .defineInRange("closeSpeed", 4.5, 1.0, 15.0);
        MISSILE_MAX_LIFE = builder
                .comment("导弹最大寿命（tick）")
                .defineInRange("maxLife", 200, 40, 600);
        builder.pop();

        builder.push("rocket");
        ROCKET_COOLDOWN_TICKS = builder
                .comment("火箭发射器冷却时长（tick）")
                .defineInRange("cooldownTicks", 200, 20, 3600);
        builder.pop();

        builder.push("hormone");
        HORMONE_COOLDOWN_TICKS = builder
                .comment("激素针冷却时长（tick）")
                .defineInRange("cooldownTicks", 1200, 20, 7200);
        builder.pop();

        builder.push("dodge");
        DODGE_STRENGTH = builder
                .comment("应激反馈背包弹射力度（block/tick）")
                .defineInRange("dodgeStrength", 1.0, 0.2, 10.0);
        DODGE_COOLDOWN_TICKS = builder
                .comment("应激反馈背包冷却时长（tick）")
                .defineInRange("cooldownTicks", 200, 20, 3600);
        builder.pop();

        builder.comment("铁砧分级修补强度：20 = 修满耐久（1 单位 = 5%）。原版铁锭等效 5（25%）")
                .push("repair");
        REPAIR_UNITS_BRASS_SHEET = builder
                .comment("黄铜板修补强度（单位）")
                .defineInRange("brassSheetUnits", 3, 0, 20);
        REPAIR_UNITS_STURDY_SHEET = builder
                .comment("坚固板修补强度（单位）")
                .defineInRange("sturdySheetUnits", 10, 0, 20);
        builder.pop();

        SPEC = builder.build();
    }
}
