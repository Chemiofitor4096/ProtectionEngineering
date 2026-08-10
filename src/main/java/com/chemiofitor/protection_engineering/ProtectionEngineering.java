package com.chemiofitor.protection_engineering;

import com.chemiofitor.protection_engineering.compat.iron.IronCompat;
import com.chemiofitor.protection_engineering.config.PEConfig;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.data.PEDataGen;
import com.chemiofitor.protection_engineering.event.PEForgeEvents;
import com.chemiofitor.protection_engineering.event.PENetworkEvents;
import com.chemiofitor.protection_engineering.item.EngineerGogglesItem;
import com.chemiofitor.protection_engineering.registry.*;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * 工程防护主类 — Forge 1.20.1 模组入口。
 * <p>
 * 提供工程师护甲系统：护甲可安装附件，附件提供被动免疫、属性加成或主动技能。
 */
@Mod(ProtectionEngineering.MODID)
public class ProtectionEngineering {
    public static final String MODID = "protectionengineering";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Registrate REGISTRATE = Registrate.create(MODID);

    // ── Constructor ────────────────────────────────────────────
    public ProtectionEngineering() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PEConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PEServerConfig.SPEC);

        PESounds.REGISTRY.register(modEventBus);
        PEEntities.REGISTRY.register(modEventBus);
        PEArmInteractionPointTypes.REGISTRY.register(modEventBus);
        PEAttributes.REGISTRY.register(modEventBus);
        modEventBus.addListener(PEAttributes::onEntityAttributeModification);

        // 必须在 PEItems.init() 之前调用，确保物品注册时 defaultCreativeModeTab 已设置
        REGISTRATE.defaultCreativeTab("engineering_tab",
                tab -> tab.title(Component.translatable("itemGroup.protectionengineering"))
        ).register();

        PEItems.init();
        PEWorkbench.init();
        PEArmorEmitter.init();

        EngineerGogglesItem.registerGogglesPredicate();

        modEventBus.addListener(EventPriority.HIGHEST, PEDataGen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, PEDataGen::gatherData);

        if (ModList.get().isLoaded("irons_spellbooks")) {
            IronCompat.register();
        }

        PENetworkEvents.register();
        MinecraftForge.EVENT_BUS.register(new PEForgeEvents());
    }

    // ── Helpers ─────────────────────────────────────────────────
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
