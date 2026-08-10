package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.registry.PEAttributes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.Set;

/**
 * 轻型外骨骼辅助设备 —— 腿部附件。
 * 移动速度 +10%，跳跃高度 +0.5 格，可以走上 1 格高的方块，免疫缓慢效果。
 */
public class LightExoskeletonItem extends AttachmentItem {

    public LightExoskeletonItem(Properties properties) {
        super(properties, Set.of(MobEffects.MOVEMENT_SLOWDOWN), SlotTypes.LEG);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(
                IAttachment.bonus("light_exo_speed", Attributes.MOVEMENT_SPEED, 0.1,
                        AttributeModifier.Operation.MULTIPLY_TOTAL),
                // 1.20.1 无 vanilla 玩家跳跃属性（Attributes.JUMP_STRENGTH 是马的），
                // 用本模组注册的 jump_strength（默认 0.42 = 原版基准，mixin 消费）
                IAttachment.bonus("light_exo_jump", PEAttributes.JUMP_STRENGTH.get(), 0.12,
                        AttributeModifier.Operation.ADDITION),
                // 1.20.1 无 Attributes.STEP_HEIGHT（1.20.5+ 引入），等价物为 ForgeMod.STEP_HEIGHT_ADDITION，
                // 由 IForgeEntity#getStepHeight 叠加到原版 maxUpStep
                IAttachment.bonus("light_exo_step", ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.4,
                        AttributeModifier.Operation.ADDITION));
    }
}
