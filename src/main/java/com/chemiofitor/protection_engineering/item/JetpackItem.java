package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.entity.ThrustEntity;
import com.chemiofitor.protection_engineering.event.PEGameEvents;
import com.chemiofitor.protection_engineering.registry.PEEntities;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * 喷气背包 —— 背部附件，提供鞘翅滑翔能力，可按热键推力加速。
 */
public class JetpackItem extends AttachmentItem {

    public JetpackItem(Properties properties) {
        super(properties, SlotTypes.BACK);
    }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ALWAYS_ON; }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.jetpack";
    }

    // ── Tick：生成推进实体（模拟烟花火箭挂载助推）─────────────

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        if (!(entity instanceof Player player) || player.level().isClientSide()) return;

        if (!PEGameEvents.thrustRequests.remove(player.getUUID())) return;
        if (!player.isFallFlying()) return;
        if (ThrustEntity.ACTIVE.contains(player.getUUID())) return;

        ThrustEntity thrust = new ThrustEntity(PEEntities.THRUST.get(), player.level());
        boolean soul = this instanceof MomentumJetpackItem;
        int duration = soul ? 100 : 60;
        thrust.init(player, soul ? 2.0 : 1.5, soul, duration);
        player.level().addFreshEntity(thrust);

        player.level().playSound(null, player, PESounds.THRUST_JETPACK.get(),
                SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}
