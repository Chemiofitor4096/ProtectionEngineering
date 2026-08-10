package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.renderer.MissileRenderer;
import com.chemiofitor.protection_engineering.client.renderer.RocketProjectileRenderer;
import com.chemiofitor.protection_engineering.registry.PEEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * 自定义实体渲染器注册。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PEEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PEEntities.ROCKET_PROJECTILE.get(),
                RocketProjectileRenderer::new);
        event.registerEntityRenderer(PEEntities.MISSILE.get(),
                MissileRenderer::new);
    }
}
