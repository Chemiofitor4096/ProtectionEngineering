package com.chemiofitor.protection_engineering;

import com.chemiofitor.protection_engineering.registry.PEItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 客户端初始化 — 盾牌格挡模型谓词。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProtectionEngineeringClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(PEItems.ENGINEER_SHIELD.get(),
                    new ResourceLocation("blocking"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }
}
