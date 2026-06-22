package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 音效事件注册 — 附件安装/卸载、设备开关、武器发射、导弹飞行等。
 */
public class PESounds {

    public static final DeferredRegister<SoundEvent> REGISTRY =
            DeferredRegister.create(Registries.SOUND_EVENT, ProtectionEngineering.MODID);

    // ── 附件安装 ─────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> ATTACH_1 =
            REGISTRY.register("attach_1", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("attach_1")));
    public static final DeferredHolder<SoundEvent, SoundEvent> ATTACH_2 =
            REGISTRY.register("attach_2", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("attach_2")));

    // ── 护甲 ─────────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> EQUIP_ENGINEER_ARMOR =
            REGISTRY.register("equip_engineer_armor", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("equip_engineer_armor")));

    // ── 夜视仪 ───────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> NIGHT_VISION_ON =
            REGISTRY.register("night_vision_on", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("night_vision_on")));
    public static final DeferredHolder<SoundEvent, SoundEvent> NIGHT_VISION_OFF =
            REGISTRY.register("night_vision_off", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("night_vision_off")));

    // ── 激素针 ───────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> HORMONE_INJECT =
            REGISTRY.register("hormone_inject", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("hormone_inject")));

    // ── 喷气背包 ─────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> THRUST_JETPACK =
            REGISTRY.register("thrust_jetpack", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("thrust_jetpack")));

    // ── 主动防御系统 ─────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> APS_ACTIVATE =
            REGISTRY.register("aps_activate", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("aps_activate")));

    // ── 导弹系统（预留）───────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_LAUNCH =
            REGISTRY.register("missile_launch", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("missile_launch")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_LOCK =
            REGISTRY.register("missile_lock", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("missile_lock")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_FLIGHT =
            REGISTRY.register("missile_flight", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("missile_flight")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MISSILE_WARNING =
            REGISTRY.register("missile_warning", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("missile_warning")));
    public static final DeferredHolder<SoundEvent, SoundEvent> ROCKET_LAUNCH =
            REGISTRY.register("rocket_launch", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("rocket_launch")));
    public static final DeferredHolder<SoundEvent, SoundEvent> LAUNCH_FAIL =
            REGISTRY.register("launch_fail", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("launch_fail")));

    // ── 闪避系统 ────────────────────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> DODGE_WARNING =
            REGISTRY.register("dodge_warning", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("dodge_warning")));
    public static final DeferredHolder<SoundEvent, SoundEvent> DODGE_JET =
            REGISTRY.register("dodge_jet", () -> SoundEvent.createVariableRangeEvent(ProtectionEngineering.asResource("dodge_jet")));

    public static void init() {}
}
