package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.client.layer.PEArmorLayer;
import com.chemiofitor.protection_engineering.client.model.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * 客户端模型层注册 — 护甲 + 附件 3D 模型层定义及渲染层注入。
 */
@Mod.EventBusSubscriber(modid = ProtectionEngineering.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PEModelLayers {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        PEAttachmentModelSetup.init(); // 注册所有附件物品→模型的映射

        event.registerLayerDefinition(EngineerHoodModel.LAYER_LOCATION, EngineerHoodModel::createBodyLayer);
        event.registerLayerDefinition(EngineerChestplateModel.LAYER_LOCATION, EngineerChestplateModel::createBodyLayer);
        event.registerLayerDefinition(EngineerChestplateRightArmModel.LAYER_LOCATION, EngineerChestplateRightArmModel::createBodyLayer);
        event.registerLayerDefinition(EngineerChestplateLeftArmModel.LAYER_LOCATION, EngineerChestplateLeftArmModel::createBodyLayer);
        event.registerLayerDefinition(EngineerLeggingsRightModel.LAYER_LOCATION, EngineerLeggingsRightModel::createBodyLayer);
        event.registerLayerDefinition(EngineerLeggingsLeftModel.LAYER_LOCATION, EngineerLeggingsLeftModel::createBodyLayer);
        event.registerLayerDefinition(EngineerBootsRightModel.LAYER_LOCATION, EngineerBootsRightModel::createBodyLayer);
        event.registerLayerDefinition(EngineerBootsLeftModel.LAYER_LOCATION, EngineerBootsLeftModel::createBodyLayer);

        // 附件模型
        event.registerLayerDefinition(EngineerGogglesAttachmentModel.LAYER_LOCATION, EngineerGogglesAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(AirFilterAttachmentModel.LAYER_LOCATION, AirFilterAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(NightVisionGogglesAttachmentModel.LAYER_LOCATION, NightVisionGogglesAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(DivingDeviceAttachmentModel.LAYER_LOCATION, DivingDeviceAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(HormoneInjectorAttachmentModel.LAYER_LOCATION, HormoneInjectorAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(ExtraMechArmLeftAttachmentModel.LAYER_LOCATION, ExtraMechArmLeftAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(ExtraMechArmRightAttachmentModel.LAYER_LOCATION, ExtraMechArmRightAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(PlateAttachmentModel.LAYER_LOCATION, PlateAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(MechaKnuckleLeftAttachmentModel.LAYER_LOCATION, MechaKnuckleLeftAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(MechaKnuckleRightAttachmentModel.LAYER_LOCATION, MechaKnuckleRightAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(HeavyExoskeletonLeftModel.LAYER_LOCATION, HeavyExoskeletonLeftModel::createBodyLayer);
        event.registerLayerDefinition(LightExoskeletonLeftModel.LAYER_LOCATION, LightExoskeletonLeftModel::createBodyLayer);
        event.registerLayerDefinition(JetpackAttachmentModel.LAYER_LOCATION, JetpackAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(MomentumJetpackAttachmentModel.LAYER_LOCATION, MomentumJetpackAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(DodgeJetpackAttachmentModel.LAYER_LOCATION, DodgeJetpackAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(MissileModel.LAYER_LOCATION, MissileModel::createBodyLayer);
        event.registerLayerDefinition(ApsAttachmentModel.LAYER_LOCATION, ApsAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(AdvancedApsAttachmentModel.LAYER_LOCATION, AdvancedApsAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(RocketPackAttachmentModel.LAYER_LOCATION, RocketPackAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(MissilePackAttachmentModel.LAYER_LOCATION, MissilePackAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(SpyglassAttachmentModel.LAYER_LOCATION, SpyglassAttachmentModel::createBodyLayer);
        event.registerLayerDefinition(PurityMarkAttachmentModel.LAYER_LOCATION, PurityMarkAttachmentModel::createBodyLayer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        // 玩家（所有皮肤变体）
        event.getSkins().forEach(skin -> {
            if (event.getSkin(skin) instanceof PlayerRenderer pr) {
                pr.addLayer((RenderLayer) new PEArmorLayer(pr, event.getEntityModels()));
            }
        });

        // 盔甲架 + 所有使用 HumanoidModel 的类人生物（僵尸、骷髅、凋灵骷髅等）
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (type == EntityType.PLAYER) continue; // 玩家已处理
            @SuppressWarnings("unchecked")
            EntityType<? extends LivingEntity> livingType = (EntityType<? extends LivingEntity>) type;
            @SuppressWarnings("rawtypes")
            LivingEntityRenderer renderer = (LivingEntityRenderer) event.getRenderer(livingType);
            if (renderer == null) continue;
            if (renderer instanceof ArmorStandRenderer || renderer.getModel() instanceof HumanoidModel) {
                renderer.addLayer((RenderLayer) new PEArmorLayer(renderer, event.getEntityModels()));
            }
        }
    }
}
