package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.ImprovedSolesItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @Unique
    private static boolean protectionengineering$hasImprovedSoles(Player player) {
        return AttachmentUtil.get(player, EquipmentSlot.FEET, SlotTypes.FOOT).getItem() instanceof ImprovedSolesItem;
    }
}
