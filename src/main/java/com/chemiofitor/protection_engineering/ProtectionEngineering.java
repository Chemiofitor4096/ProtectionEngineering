package com.chemiofitor.protection_engineering;

import com.chemiofitor.protection_engineering.compat.DetailArmorBarCompat;
import com.chemiofitor.protection_engineering.config.PEConfig;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.data.PEDataGen;
import com.chemiofitor.protection_engineering.event.PENeoForgeEvents;
import com.chemiofitor.protection_engineering.item.EngineerGogglesItem;
import com.chemiofitor.protection_engineering.registry.*;
import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * 工程防护主类 — NeoForge 1.21.1 模组入口。
 * <p>
 * 提供工程师护甲系统：护甲可安装附件，附件提供被动免疫、属性加成或主动技能。
 */
@Mod(ProtectionEngineering.MODID)
public class ProtectionEngineering {
    public static final String MODID = "protectionengineering";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Registrate REGISTRATE = Registrate.create(MODID);

    // ── Constructor ────────────────────────────────────────────
    public ProtectionEngineering(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, PEConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, PEServerConfig.SPEC);

        PEDataComponents.REGISTRY.register(modEventBus);
        PEArmorMaterials.REGISTRY.register(modEventBus);
        PESounds.REGISTRY.register(modEventBus);
        PEEntities.REGISTRY.register(modEventBus);

        // 必须在 PEItems.init() 之前调用，确保物品注册时 defaultCreativeModeTab 已设置
        REGISTRATE.defaultCreativeTab("engineering_tab",
                tab -> tab.title(Component.translatable("itemGroup.protectionengineering"))
        ).register();

        PEItems.init();
        PEEntities.init();
        PEWorkbench.init();

        EngineerGogglesItem.registerGogglesPredicate();

        modEventBus.addListener(EventPriority.HIGHEST, PEDataGen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, PEDataGen::gatherData);

        if (ModList.get().isLoaded("detailab")) {
            modEventBus.addListener(DetailArmorBarCompat::onClientSetup);
        }

        NeoForge.EVENT_BUS.register(new PENeoForgeEvents());
    }

    // ── Helpers ─────────────────────────────────────────────────
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
