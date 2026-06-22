package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.renderer.MissileRenderer;
import com.chemiofitor.protection_engineering.client.renderer.RocketProjectileRenderer;
import com.chemiofitor.protection_engineering.registry.PEEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 自定义实体渲染器注册。
 */
@EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT)
public class PEEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PEEntities.ROCKET_PROJECTILE.get(),
                RocketProjectileRenderer::new);
        event.registerEntityRenderer(PEEntities.MISSILE.get(),
                MissileRenderer::new);
    }
}
