package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.entity.MissileEntity;
import com.chemiofitor.protection_engineering.entity.RocketProjectile;
import com.chemiofitor.protection_engineering.entity.ThrustEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 实体类型注册 — 推力实体、火箭射弹、制导导弹。
 */
public class PEEntities {

    public static final DeferredRegister<EntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.ENTITY_TYPE, ProtectionEngineering.MODID);

    public static final RegistryObject<EntityType<ThrustEntity>> THRUST =
            REGISTRY.register("thrust",
                    () -> EntityType.Builder.<ThrustEntity>of(ThrustEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .clientTrackingRange(0)
                            .updateInterval(Integer.MAX_VALUE)
                            .noSummon()
                            .build(ProtectionEngineering.asResource("thrust").toString()));

    public static final RegistryObject<EntityType<RocketProjectile>> ROCKET_PROJECTILE =
            REGISTRY.register("rocket_projectile",
                    () -> EntityType.Builder.<RocketProjectile>of(RocketProjectile::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ProtectionEngineering.asResource("rocket_projectile").toString()));

    public static final RegistryObject<EntityType<MissileEntity>> MISSILE =
            REGISTRY.register("missile",
                    () -> EntityType.Builder.<MissileEntity>of(MissileEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build(ProtectionEngineering.asResource("missile").toString()));
}
