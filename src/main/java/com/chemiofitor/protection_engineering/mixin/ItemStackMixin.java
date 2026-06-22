package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.ImprovedSolesItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "canWalkOnPowderedSnow", at = @At("RETURN"), cancellable = true)
    private void pe_canWalkOnPowderedSnow(LivingEntity wearer, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return; // 已经 true 则跳过

        if (!(wearer.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IAttachmentHost host)) return;
        var data = host.getAttachments(wearer.getItemBySlot(EquipmentSlot.FEET));
        if (data.get(SlotTypes.FOOT).getItem() instanceof ImprovedSolesItem) {
            cir.setReturnValue(true);
        }
    }
}
