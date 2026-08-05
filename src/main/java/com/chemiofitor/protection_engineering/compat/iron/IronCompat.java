package com.chemiofitor.protection_engineering.compat.iron;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

/**
 * 铁魔法兼容入口 — 仅当 Iron's Spells 'n Spellbooks 加载时由主类调用。
 * <p>
 * 魔法内衬只在铁魔法加载时注册：否则没装铁魔法也能用指令掏出物品，
 * 而它们的伤害类型 key 在铁魔法缺席时匹配不到任何伤害，属于坏物品。
 * <p>
 * 对外只暴露这一个类（由 {@code ProtectionEngineering} 构造器在
 * {@code ModList.get().isLoaded("irons_spellbooks")} 守卫后调用）。
 */
public class IronCompat {

    private static boolean registered = false;

    public static void register(IEventBus modEventBus, ModContainer modContainer) {
        if (registered) return;
        registered = true;

        IronCompatItems.init();
        ProtectionEngineering.LOGGER.info("[Protection Engineering] Iron's Spells compat enabled");
    }
}
