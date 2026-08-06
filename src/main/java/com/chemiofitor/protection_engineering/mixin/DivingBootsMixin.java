package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.item.DivingSolesItem;
import com.simibubi.create.content.equipment.armor.DivingBootsItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 潜水配重鞋底 Create 兼容 — 注入 {@link DivingBootsItem#getWornItem}：
 * 当生物脚上靴子装有潜水配重鞋底（FOOT 槽附件）时，将其识别为潜水靴，
 * 获得 Create 潜水靴的水下下沉加速效果。
 */
@Mixin(DivingBootsItem.class)
public class DivingBootsMixin {

    @Inject(remap = false, method = "getWornItem", at = @At("RETURN"), cancellable = true)
    private static void getWornItem(Entity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            boolean hasSoles = boots.getItem() instanceof IAttachmentHost host
                    && host.getAttachments(boots).get(SlotTypes.FOOT).getItem() instanceof DivingSolesItem;
            if (hasSoles) {
                cir.setReturnValue(boots);
            }
        }
    }
}
