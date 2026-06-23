package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.item.EngineerShieldItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public class PlayerShieldMixin {

    /** 工程师盾牌斧头破防禁用时间减半：100 → 50 tick */
    @ModifyArg(method = "disableShield", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"),
            index = 1)
    private int pe_modifyShieldCooldown(int originalDuration) {
        Player self = (Player) (Object) this;
        ItemStack useItem = self.getUseItem();
        if (useItem.getItem() instanceof EngineerShieldItem) {
            return EngineerShieldItem.COOLDOWN_TICKS;
        }
        return originalDuration;
    }
}
