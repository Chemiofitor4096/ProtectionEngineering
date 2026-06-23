package com.chemiofitor.protection_engineering.mixin;

import com.chemiofitor.protection_engineering.item.EngineerShieldItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ShieldAngleMixin {

    /** 工程师盾牌防护范围 +20%（180° → 216°） */
    @Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
    private void pe_widenShieldAngle(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return;
        if (!(player.getUseItem().getItem() instanceof EngineerShieldItem)) return;
        if (!player.isBlocking()) return;

        if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_SHIELD)) return;
        if (source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.AbstractArrow a
                && a.getPierceLevel() > 0) return;

        Vec3 srcPos = source.getSourcePosition();
        if (srcPos == null) return;

        Vec3 look = player.calculateViewVector(0.0F, player.getYHeadRot());
        Vec3 toPlayer = srcPos.vectorTo(player.position());
        toPlayer = new Vec3(toPlayer.x, 0.0, toPlayer.z).normalize();

        // 原版 < 0.0（180°），加宽到 < 0.309（216°）
        cir.setReturnValue(toPlayer.dot(look) < 0.309);
    }
}
