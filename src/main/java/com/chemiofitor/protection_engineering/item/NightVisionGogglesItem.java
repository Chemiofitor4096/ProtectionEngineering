package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 夜视眼镜 —— 眼部附件，自由开关。
 * 开启后每 tick 续夜视效果，关闭后自动消除。
 */
public class NightVisionGogglesItem extends AttachmentItem {

    public NightVisionGogglesItem(Properties properties) {
        super(properties, Set.of(MobEffects.DARKNESS, MobEffects.BLINDNESS), SlotTypes.EYES);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.FREE_TOGGLE; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.night_vision";
    }

    // ── 状态钩子 ──────────────────────────────────────────────

    @Override
    protected void onStateEnter(ItemStack stack, int newState, LivingEntity entity) {
        if (newState == STATE_READY) {
            entity.level().playSound(null, entity, PESounds.NIGHT_VISION_ON.get(),
                    SoundSource.PLAYERS, 0.6f, 1.0f);
        }
    }

    @Override
    protected void onStateExit(ItemStack stack, int oldState, LivingEntity entity) {
        if (oldState == STATE_READY) {
            entity.removeEffect(MobEffects.NIGHT_VISION);
            entity.level().playSound(null, entity, PESounds.NIGHT_VISION_OFF.get(),
                    SoundSource.PLAYERS, 0.6f, 1.0f);
        }
    }

    // ── Tick ───────────────────────────────────────────────────

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        super.onTick(attachment, host, entity, slot); // 基类状态机

        if (getState(attachment) == STATE_READY) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION, 220, 0, false, false, true));
        } else {
            if (entity.hasEffect(MobEffects.NIGHT_VISION)) {
                entity.removeEffect(MobEffects.NIGHT_VISION);
            }
        }
    }
}
