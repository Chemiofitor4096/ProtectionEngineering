package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.model.*;
import com.chemiofitor.protection_engineering.item.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;

/**
 * 集中注册所有附件物品 → 3D 模型的映射。
 * <p>
 * 此类仅存在于客户端包，通过 {@link PEModelLayers} 的客户端事件调用。
 * 模型和纹理数据不再存储在物品类中，彻底避免服务端加载时
 * 引用 {@code net.minecraft.client.model.EntityModel} 等客户端专用类。
 */
public final class PEAttachmentModelSetup {

    private PEAttachmentModelSetup() {}

    /** 在客户端初始化阶段调用一次。幂等。 */
    public static void init() {
        // ── 头盔附件 (EYES / MOUTH) ──────────────────────────

        registerMain(AirFilterItem.class,
                ms -> new AirFilterAttachmentModel<>(ms.bakeLayer(AirFilterAttachmentModel.LAYER_LOCATION)),
                "air_filter.png");

        registerMain(DivingDeviceItem.class,
                ms -> new DivingDeviceAttachmentModel<>(ms.bakeLayer(DivingDeviceAttachmentModel.LAYER_LOCATION)),
                "diving_device.png");

        registerMain(EngineerGogglesItem.class,
                ms -> new EngineerGogglesAttachmentModel<>(ms.bakeLayer(EngineerGogglesAttachmentModel.LAYER_LOCATION)),
                "engineer_goggles.png");

        registerMain(NightVisionGogglesItem.class,
                ms -> new NightVisionGogglesAttachmentModel<>(ms.bakeLayer(NightVisionGogglesAttachmentModel.LAYER_LOCATION)),
                "night_vision_goggles.png");

        registerMain(SpyglassItem.class,
                ms -> new SpyglassAttachmentModel<>(ms.bakeLayer(SpyglassAttachmentModel.LAYER_LOCATION)),
                "spyglass.png");

        registerMain(HormoneInjectorItem.class,
                ms -> new HormoneInjectorAttachmentModel<>(ms.bakeLayer(HormoneInjectorAttachmentModel.LAYER_LOCATION)),
                "hormone_injector.png");

        // ── 胸甲附件 — 肩部 (SHOULDER) ─────────────────────

        registerMain(ApsItem.class,
                ms -> new ApsAttachmentModel<>(ms.bakeLayer(ApsAttachmentModel.LAYER_LOCATION)),
                "aps.png");

        registerMain(AdvancedApsItem.class,
                ms -> new AdvancedApsAttachmentModel<>(ms.bakeLayer(AdvancedApsAttachmentModel.LAYER_LOCATION)),
                "advanced_aps.png");

        registerMain(RocketLauncherItem.class,
                ms -> new RocketPackAttachmentModel<>(ms.bakeLayer(RocketPackAttachmentModel.LAYER_LOCATION)),
                "rocket_pack.png");

        // ── 胸甲附件 — 胸甲板 (CHESTPLATE) ──────────────────

        registerMain(SturdyPlateItem.class,
                ms -> new PlateAttachmentModel<>(ms.bakeLayer(PlateAttachmentModel.LAYER_LOCATION)),
                "sturdy_plate.png");

        registerMain(NetheritePlateItem.class,
                ms -> new PlateAttachmentModel<>(ms.bakeLayer(PlateAttachmentModel.LAYER_LOCATION)),
                "netherite_plate.png");

        registerMain(PurityMarkItem.class,
                ms -> new PurityMarkAttachmentModel<>(ms.bakeLayer(PurityMarkAttachmentModel.LAYER_LOCATION)),
                "purity_mark.png");

        // ── 胸甲附件 — 背部 (BACK) ──────────────────────────

        registerMain(JetpackItem.class,
                ms -> new JetpackAttachmentModel<>(ms.bakeLayer(JetpackAttachmentModel.LAYER_LOCATION)),
                "jetpack.png");

        registerMain(MomentumJetpackItem.class,
                ms -> new MomentumJetpackAttachmentModel<>(ms.bakeLayer(MomentumJetpackAttachmentModel.LAYER_LOCATION)),
                "momentum_jetpack.png");

        registerMain(DodgeJetpackItem.class,
                ms -> new DodgeJetpackAttachmentModel<>(ms.bakeLayer(DodgeJetpackAttachmentModel.LAYER_LOCATION)),
                "dodge_jetpack.png");

        registerMain(MissilePackItem.class,
                ms -> new MissilePackAttachmentModel<>(ms.bakeLayer(MissilePackAttachmentModel.LAYER_LOCATION)),
                "missile_pack.png");

        // ── 胸甲附件 — 手臂 (ARM) ───────────────────────────

        PEAttachmentModelRegistry.registerArm(ExtraMechanicalArmItem.class,
                new PEAttachmentModelRegistry.LimbModelProvider() {
                    @Override
                    public EntityModel<?> createLeft(EntityModelSet ms) {
                        return new ExtraMechArmLeftAttachmentModel<>(ms.bakeLayer(ExtraMechArmLeftAttachmentModel.LAYER_LOCATION));
                    }
                    @Override
                    public EntityModel<?> createRight(EntityModelSet ms) {
                        return new ExtraMechArmRightAttachmentModel<>(ms.bakeLayer(ExtraMechArmRightAttachmentModel.LAYER_LOCATION));
                    }
                },
                tex("extra_mechanical_arm.png"));

        PEAttachmentModelRegistry.registerArm(MechaKnuckleItem.class,
                new PEAttachmentModelRegistry.LimbModelProvider() {
                    @Override
                    public EntityModel<?> createLeft(EntityModelSet ms) {
                        return new MechaKnuckleLeftAttachmentModel<>(ms.bakeLayer(MechaKnuckleLeftAttachmentModel.LAYER_LOCATION));
                    }
                    @Override
                    public EntityModel<?> createRight(EntityModelSet ms) {
                        return new MechaKnuckleRightAttachmentModel<>(ms.bakeLayer(MechaKnuckleRightAttachmentModel.LAYER_LOCATION));
                    }
                },
                tex("mecha_knuckle.png"));

        // ── 腿部附件 (LEG) — 仅左腿模型，右腿由渲染层 X 轴镜像 ──

        PEAttachmentModelRegistry.registerLeg(LightExoskeletonItem.class,
                new PEAttachmentModelRegistry.LimbModelProvider() {
                    @Override
                    public EntityModel<?> createLeft(EntityModelSet ms) {
                        return new LightExoskeletonLeftModel<>(ms.bakeLayer(LightExoskeletonLeftModel.LAYER_LOCATION));
                    }
                },
                tex("light_exoskeleton.png"));

        PEAttachmentModelRegistry.registerLeg(HeavyExoskeletonItem.class,
                new PEAttachmentModelRegistry.LimbModelProvider() {
                    @Override
                    public EntityModel<?> createLeft(EntityModelSet ms) {
                        return new HeavyExoskeletonLeftModel<>(ms.bakeLayer(HeavyExoskeletonLeftModel.LAYER_LOCATION));
                    }
                },
                tex("heavy_exoskeleton.png"));
    }

    // ── helper ──────────────────────────────────────────────

    private static void registerMain(Class<?> itemClass,
                                      PEAttachmentModelRegistry.MainModelProvider provider,
                                      String textureName) {
        PEAttachmentModelRegistry.registerMain(itemClass, provider, tex(textureName));
    }

    private static ResourceLocation tex(String name) {
        return ProtectionEngineering.asResource("textures/models/armor/" + name);
    }
}
