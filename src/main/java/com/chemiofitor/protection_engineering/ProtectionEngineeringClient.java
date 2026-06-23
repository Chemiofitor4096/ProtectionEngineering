package com.chemiofitor.protection_engineering;

import com.chemiofitor.protection_engineering.registry.PEItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;

@Mod(value = ProtectionEngineering.MODID, dist = Dist.CLIENT)
public class ProtectionEngineeringClient {

    public ProtectionEngineeringClient(ModContainer container) {
        container.getEventBus().register(this);
    }

    @SubscribeEvent
    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(PEItems.ENGINEER_SHIELD.get(),
                    ResourceLocation.withDefaultNamespace("blocking"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }
}
