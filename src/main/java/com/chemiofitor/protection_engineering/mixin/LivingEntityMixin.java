package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.ImprovedSolesItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float pe_reduceDamage(float amount, DamageSource source) {
        if (amount <= 0) return amount;

        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return amount;

        float reduction = 0f;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;

            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                if (entry.getValue().getItem() instanceof IAttachment att) {
                    reduction += att.getDamageReduction();
                }
            }
        }

        if (reduction > 0) {
            reduction = Math.min(reduction, 1.0f); // 上限 100%
            return amount * (1f - reduction);
        }
        return amount;
    }

    /** 改良鞋底：消除冰面/粘液块/蜂蜜块的滑度（也包含灵魂沙的 getSpeedFactor 影响） */
    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void pe_cancelSlipperiness(CallbackInfoReturnable<Float> cir) {
        if (cir.getReturnValue() >= 1.0f) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (hasImprovedSoles(player)) cir.setReturnValue(1.0f);
    }

    private static boolean hasImprovedSoles(Player player) {
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        if (!(boots.getItem() instanceof IAttachmentHost host)) return false;
        return host.getAttachments(boots).get(SlotTypes.FOOT).getItem() instanceof ImprovedSolesItem;
    }
}
