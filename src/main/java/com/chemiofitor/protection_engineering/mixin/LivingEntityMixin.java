package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.ImprovedSolesItem;
import com.chemiofitor.protection_engineering.registry.PEAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void pe_cancelSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (cir.getReturnValue() >= 1.0f) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (protectionengineering$hasImprovedSoles(player)) cir.setReturnValue(1.0f);
    }

    /**
     * 1.20.1 无玩家跳跃属性（vanilla getJumpPower 硬编码 0.42，且 Attributes.JUMP_STRENGTH
     * 实为马的属性）。用 {@link PEAttributes#JUMP_STRENGTH} 属性值替换常量 0.42：
     * 默认 0.42 与原版等价，ADDITION 修饰符直接叠加（语义与 1.21.1 完全一致）。
     * 原版公式（getBlockJumpFactor / getJumpBoostPower）保持不变 —— getBlockJumpFactor
     * 在 1.20.1 是 protected，mixin 类跨包无法直接调用，故用 @ModifyConstant 留在目标方法内。
     * 仅玩家等已挂载该属性的实体生效，马等生物返回原值保持原逻辑。
     */
    @ModifyConstant(method = "getJumpPower", constant = @Constant(floatValue = 0.42F))
    private float pe_jumpStrength(float original) {
        LivingEntity self = (LivingEntity) (Object) this;
        AttributeInstance jumpStrength = self.getAttribute(PEAttributes.JUMP_STRENGTH.get());
        if (jumpStrength == null) return original;
        return (float) jumpStrength.getValue();
    }

    @Unique
    private static boolean protectionengineering$hasImprovedSoles(Player player) {
        return AttachmentUtil.get(player, EquipmentSlot.FEET, SlotTypes.FOOT).getItem() instanceof ImprovedSolesItem;
    }
}
