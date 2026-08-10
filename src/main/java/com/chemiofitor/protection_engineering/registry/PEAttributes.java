package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 模组自有属性注册。
 * <p>
 * 1.20.1 的 {@code Attributes.JUMP_STRENGTH} 实为马的 {@code horse.jump_strength}，
 * 玩家实体无此属性实例且 1.20.1 跳跃逻辑（LivingEntity#getJumpPower）不消费任何属性。
 * 为复刻 1.21.1 语义（玩家跳跃初速 0.42，ADDITION 直接叠加），注册自有
 * {@code jump_strength} 属性：基准值 0.42 与原版一致，由 {@code LivingEntityMixin} 消费，
 * 数值（LightExo +0.12 / SpringyKneecap +0.06）与 1.21.1 完全一致。
 */
public final class PEAttributes {

    private PEAttributes() {}

    public static final DeferredRegister<Attribute> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ProtectionEngineering.MODID);

    /** 玩家跳跃初速度（方块/tick），默认 0.42 即原版基准 */
    public static final RegistryObject<Attribute> JUMP_STRENGTH = REGISTRY.register("jump_strength",
            () -> new RangedAttribute("attribute.name.protectionengineering.jump_strength",
                    0.42D, -1024.0D, 1024.0D).setSyncable(true));

    /** 仅挂到玩家（马等生物保持原逻辑）；setSyncable 使客户端跳跃预测使用同步后的值 */
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, JUMP_STRENGTH.get());
    }
}
