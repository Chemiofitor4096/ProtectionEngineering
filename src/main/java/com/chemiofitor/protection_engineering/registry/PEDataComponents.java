package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 自定义数据组件注册。
 */
public class PEDataComponents {

    public static final DeferredRegister<DataComponentType<?>> REGISTRY =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ProtectionEngineering.MODID);

    /** 护甲附件数据 —— 存储护甲上安装的所有附件 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AttachmentsData>> ATTACHMENTS =
            REGISTRY.register("attachments",
                    () -> DataComponentType.<AttachmentsData>builder()
                            .persistent(AttachmentsData.CODEC)
                            .networkSynchronized(AttachmentsData.STREAM_CODEC)
                            .build()
            );

    /** 附件开关状态 —— 可开关附件的启用/禁用标志 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ATTACHMENT_ACTIVE =
            REGISTRY.register("attachment_active",
                    () -> DataComponentType.<Boolean>builder()
                            .persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
                            .build()
            );

    /** 附件冷却结束 tick —— 一次性激活附件的冷却计时 */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> ATTACHMENT_COOLDOWN =
            REGISTRY.register("attachment_cooldown",
                    () -> DataComponentType.<Long>builder()
                            .persistent(Codec.LONG)
                            .networkSynchronized(ByteBufCodecs.VAR_LONG)
                            .build()
            );

    /** 附件状态机 —— 统一状态标识 (0=DISABLED, 1=READY, 2=ACTIVE, 3=COOLING) */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ATTACHMENT_STATE =
            REGISTRY.register("attachment_state",
                    () -> DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
                            .build()
            );
}
