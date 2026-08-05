package com.chemiofitor.protection_engineering.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.impl.animation.IBendHelper;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * PlayerAnimator 弯曲动画兼容 — 独立类隔离 PlayerAnimator 依赖。
 * <p>
 * PlayerAnimator 的 bend（弯曲）动画在 {@code HumanoidModel.renderToBuffer} 内部
 * 通过 PoseStack 变换实现（见其 BipedEntityModelMixin），而护甲层在 model 渲染
 * **之后**执行，弯曲变换已被恢复 → 弯曲动作时护甲不随身体弯曲而"乱飘"。
 * <p>
 * 这里在护甲层渲染上半身前补上 body 弯曲变换（与 PlayerAnimator 对玩家身体
 * 的处理一致）。动画对象从**实体**获取（{@code IAnimatedPlayer}），比从模型
 * 接口判断更可靠。
 * <p>
 * 调用方须先以 {@code ModList.get().isLoaded("playeranimator")} 守卫 —— 本类延迟加载，
 * PlayerAnimator 缺席时不会被加载，不会抛 NoClassDefFoundError。
 */
public final class PlayerAnimCompat {
    private PlayerAnimCompat() {}

    /** 若 PlayerAnimator 动画活跃，将 body 弯曲应用到 PoseStack */
    public static void applyBodyBend(PoseStack pose, LivingEntity entity) {
        if (!(entity instanceof AbstractClientPlayer player)) return;
        if (!(player instanceof IAnimatedPlayer animated)) return;
        AnimationApplier processor = animated.playerAnimator_getAnimation();
        if (processor == null || !processor.isActive()) return;
        IBendHelper.rotateMatrixStack(pose, processor.getBend("body"));
    }
}
