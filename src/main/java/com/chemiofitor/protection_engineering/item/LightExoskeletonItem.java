package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.Set;

/**
 * 轻型外骨骼辅助设备 —— 腿部附件。
 * 移动速度 +10%，跳跃高度 +0.5 格，可以走上 1 格高的方块，免疫缓慢效果。
 */
public class LightExoskeletonItem extends AttachmentItem {

    private static final ResourceLocation SPEED_ID =
            ProtectionEngineering.asResource("light_exo_speed");
    private static final ResourceLocation JUMP_ID =
            ProtectionEngineering.asResource("light_exo_jump");
    private static final ResourceLocation STEP_ID =
            ProtectionEngineering.asResource("light_exo_step");

    public LightExoskeletonItem(Properties properties) {
        super(properties, Set.of(MobEffects.MOVEMENT_SLOWDOWN), SlotTypes.LEG);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(
                new IAttachment.AttributeBonus(SPEED_ID, Attributes.MOVEMENT_SPEED, 0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                new IAttachment.AttributeBonus(JUMP_ID, Attributes.JUMP_STRENGTH, 0.12,
                        AttributeModifier.Operation.ADD_VALUE),
                new IAttachment.AttributeBonus(STEP_ID, Attributes.STEP_HEIGHT, 0.4,
                        AttributeModifier.Operation.ADD_VALUE));
    }
}
