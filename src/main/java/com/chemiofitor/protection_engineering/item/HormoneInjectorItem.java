package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 刺激性治疗针 —— 眼部附件，按键激活。
 * 使用后：10s 生命恢复 + 抗性提升，5s 反胃，60s 冷却。
 */
public class HormoneInjectorItem extends AttachmentItem {

    private static final int EFFECT_DURATION = 200;
    private static final int NAUSEA_DURATION = 100;

    public HormoneInjectorItem(Properties properties) {
        super(properties, SlotTypes.EYES);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }

    @Override
    public long getCooldownDuration() { return PEServerConfig.HORMONE_COOLDOWN_TICKS.get(); }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.hormone_injector";
    }

    // ── 一次性激活 ────────────────────────────────────────────

    @Override
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION, 0,
                false, true, true));
        entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, 0,
                false, true, true));
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, NAUSEA_DURATION, 0,
                false, true, true));

        entity.level().playSound(null, entity, PESounds.HORMONE_INJECT.get(),
                SoundSource.PLAYERS, 0.8f, 1.0f);
    }
}
