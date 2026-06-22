package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.client.model.MomentumJetpackAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 动量容器背包 —— 背部附件，喷气背包升级版：速度+20%，灵魂火粒子。
 */
public class MomentumJetpackItem extends JetpackItem {

    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/momentum_jetpack.png");

    public MomentumJetpackItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.momentum_jetpack";
    }

    // ── 3D 渲染 ──────────────────────────────────────────────

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new MomentumJetpackAttachmentModel<>(
                modelSet.bakeLayer(MomentumJetpackAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEX;
    }

    // ── 速度提升 ──────────────────────────────────────────────

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        // 先处理推力（继承自 JetpackItem）
        super.onTick(attachment, host, entity, slot);

        // 被动速度加成：每 tick 水平微增，累计约 +20% 终端速度
        if (entity instanceof Player player && player.isFallFlying() && !player.level().isClientSide()) {
            Vec3 vel = player.getDeltaMovement();
            player.setDeltaMovement(vel.x * 1.002, vel.y, vel.z * 1.002);
        }
    }

}
