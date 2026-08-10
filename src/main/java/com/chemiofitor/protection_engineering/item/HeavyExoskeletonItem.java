package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * 重型外骨骼辅助设备 —— 腿部附件。
 * 护甲值 +2，可以走上 1 格高的方块，降低 20% 摔落伤害，免疫缓慢效果。
 */
public class HeavyExoskeletonItem extends AttachmentItem {

    public HeavyExoskeletonItem(Properties properties) {
        super(properties, Set.of(MobEffects.MOVEMENT_SLOWDOWN), SlotTypes.LEG);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.heavy_exoskeleton";
    }

    /** 摔落伤害减免 20% */
    @Override
    public float getFallDamageReduction() { return 0.20f; }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(
                IAttachment.bonus("heavy_exo_armor", Attributes.ARMOR, 2.0,
                        AttributeModifier.Operation.ADDITION),
                // 1.20.1 无 Attributes.STEP_HEIGHT（1.20.5+ 引入），等价物为 ForgeMod.STEP_HEIGHT_ADDITION
                IAttachment.bonus("heavy_exo_step", ForgeMod.STEP_HEIGHT_ADDITION.get(), 0.4,
                        AttributeModifier.Operation.ADDITION));
    }
}
