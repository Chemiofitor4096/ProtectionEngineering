package com.chemiofitor.protection_engineering.compat;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.registry.PEItems;
import com.redlimerl.detailab.api.DetailArmorBarAPI;
import com.redlimerl.detailab.api.render.ArmorBarRenderManager;
import com.redlimerl.detailab.api.render.BarRenderManager;
import com.redlimerl.detailab.api.render.TextureOffset;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.*;

public class DetailArmorBarCompat {

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(
            ProtectionEngineering.MODID, "textures/gui/engineer_armor_bar.png");
    // 纹理 36×9: 半格(0,0) 满格(9,0) 半轮廓(18,0) 全轮廓(27,0)

    public static void register(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(DetailArmorBarCompat::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> DetailArmorBarAPI.customArmorBarBuilder()
                .armor(PEItems.ENGINEER_HOOD.get(),
                       PEItems.ENGINEER_CHESTPLATE.get(),
                       PEItems.ENGINEER_LEGGINGS.get(),
                       PEItems.ENGINEER_BOOTS.get())
                .render(stack -> new ArmorBarRenderManager(
                        new BarRenderManager.Texture(TEX, 64, 32, new TextureOffset(9, 0)),
                        new BarRenderManager.Texture(TEX, 64, 32, new TextureOffset(0, 0)),
                        new BarRenderManager.Texture(TEX, 64, 32, new TextureOffset(27, 0)),
                        new BarRenderManager.Texture(TEX, 64, 32, new TextureOffset(18, 0)),
                        Color.WHITE
                ))
                .register());
    }
}
