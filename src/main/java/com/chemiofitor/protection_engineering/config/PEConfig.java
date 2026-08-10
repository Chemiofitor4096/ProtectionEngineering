package com.chemiofitor.protection_engineering.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 客户端配置 — HUD 覆盖层位置等。
 */
public class PEConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue HUD_OFFSET_X;
    public static final ForgeConfigSpec.IntValue HUD_OFFSET_Y;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("HUD");
        HUD_OFFSET_X = builder
                .comment("HUD 距屏幕右边的距离（像素）")
                .defineInRange("hudOffsetX", 4, 0, 500);
        HUD_OFFSET_Y = builder
                .comment("HUD 距屏幕顶部的距离（像素）")
                .defineInRange("hudOffsetY", 4, 0, 500);
        builder.pop();

        SPEC = builder.build();
    }
}
