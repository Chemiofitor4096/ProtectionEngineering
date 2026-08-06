package com.chemiofitor.protection_engineering.api;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * 附件物品接口。
 * <p>
 * 每个附件声明它可以安装到哪些 {@link SlotType}：
 * 一个附件可以适配多种槽位（如通用加固板可装 CHESTPLATE 或 GUARD）。
 * <p>
 * 所有方法都有默认空实现 —— 附件只需重写它需要的。
 */
public interface IAttachment {

    /** 此附件可安装到的槽位类型集合 */
    Set<SlotType> compatibleSlots();

    /**
     * 宿主护甲被玩家穿上时调用（非附件安装/拆卸）。
     *
     * @param attachment 附件自身的 ItemStack
     * @param host       宿主护甲 ItemStack
     * @param entity     装备者（玩家）
     */
    default void onEquip(ItemStack attachment, ItemStack host, LivingEntity entity) {}

    /**
     * 宿主护甲被玩家脱下时调用（非附件安装/拆卸）。
     */
    default void onUnequip(ItemStack attachment, ItemStack host, LivingEntity entity) {}

    /**
     * 附件安装后每 tick 调用。
     *
     * @param attachment 附件自身的 ItemStack
     * @param host       宿主物品 ItemStack
     * @param entity     装备者
     * @param slot       附件当前所在的槽位类型
     */
    default void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {}

    /**
     * 检查此附件是否可以安装到指定的宿主物品上。
     * 默认返回 true，子类可重写以添加额外条件。
     *
     * @param host 目标宿主物品 ItemStack
     */
    default boolean canInstallOn(ItemStack host) {
        return true;
    }

    // ── 状态效果免疫 ──────────────────────────────────────────

    /**
     * 此附件提供的状态效果免疫集合。
     * 装备此附件后，集合内的效果将被阻止应用到玩家身上。
     * 默认返回空集。
     */
    default Set<Holder<MobEffect>> getImmunities() {
        return Set.of();
    }

    // ── Tooltip ───────────────────────────────────────────────

    /**
     * 功能描述翻译 key，给 tooltip 用。
     * 返回 null 表示无特殊功能描述。
     */
    @Nullable
    default String getFeatureKey() {
        return null;
    }

    // ── 属性修改 ──────────────────────────────────────────────

    /**
     * 附件提供的属性修饰符声明。
     * <p>
     * 声明同时驱动两处：{@code addAttributeModifiers} 将声明应用到宿主护甲（实际生效），
     * 附件 tooltip 用原版"当作为部件安装时：+X 属性"风格展示。声明一次，两处复用。
     * <p>
     * 生效的装备槽组不在此声明 —— 由附件兼容的 {@link SlotType#equipmentSlotGroup()}
     * 自动推导（单组 → 该组；跨组 → {@code ANY}），避免手写错槽导致属性不生效。
     *
     * @param id        修饰符唯一 ID（模组命名空间）
     * @param attribute 目标属性
     * @param amount    数值（ADD_MULTIPLIED_* 为小数，如 0.2 = +20%）
     * @param operation 操作类型
     */
    record AttributeBonus(ResourceLocation id, Holder<Attribute> attribute,
                          double amount, AttributeModifier.Operation operation) {}

    /** 便捷工厂：以模组命名空间生成修饰符 ID */
    static AttributeBonus bonus(String path, Holder<Attribute> attribute, double amount,
                                AttributeModifier.Operation operation) {
        return new AttributeBonus(ProtectionEngineering.asResource(path), attribute, amount, operation);
    }

    /**
     * 此附件提供的属性修饰符列表。tooltip 与事件共用，默认空。
     */
    default List<AttributeBonus> getAttributeBonuses() {
        return List.of();
    }

    /**
     * 向宿主护甲追加属性修饰符。
     * 当护甲被穿戴并计算属性时，通过 {@code ItemAttributeModifierEvent} 调用。
     */
    default void addAttributeModifiers(ItemAttributeModifierEvent event) {}

    // ── 减伤 ──────────────────────────────────────────────────

    /**
     * 限定减免的伤害类型。空集 = 全类型（向后兼容旧附件）。
     * 非空时仅对匹配的 DamageType 生效。
     */
    default Set<ResourceKey<DamageType>> getProtectedDamageTypes() {
        return Set.of();
    }

    /**
     * 限定减免的伤害类型标签。与 {@link #getProtectedDamageTypes()} 取并集。
     * <p>
     * 火伤有 7 种原版 DamageType（且模组会追加），逐个枚举容易漏，
     * 用 {@code DamageTypeTags.IS_FIRE} 之类的标签更稳。
     */
    default Set<TagKey<DamageType>> getProtectedDamageTypeTags() {
        return Set.of();
    }

    /** 伤害减免比例 (0~1)。坚固防护板 0.10，下界合金 0.15 */
    default float getDamageReduction() { return 0f; }

    /** 摔落伤害减免比例 (0~1)。重型外骨骼 0.20 */
    default float getFallDamageReduction() { return 0f; }

    /** 摔落距离减免（格）。缓冲鞋底 1.0 */
    default float getFallDistanceReduction() { return 0f; }

    // ── 冷却/激活时长（供 HUD 显示）────────────────────────────

    /**
     * 激活窗口时长（tick）。仅 {@link ControlPattern#ACTIVE_COOLDOWN} 模式使用。
     * 返回 0 表示无激活窗口（如 FREE_TOGGLE 和 ONE_SHOT_COOLDOWN）。
     * <p>
     * 不为 0 时，HUD 在激活期内显示 ⚡ 倒计时。
     */
    default long getActiveDuration() { return 0L; }

    /**
     * 冷却时长（tick）。{@link ControlPattern#ACTIVE_COOLDOWN} 和
     * {@link ControlPattern#ONE_SHOT_COOLDOWN} 模式使用。
     * 返回 0 表示无冷却（如 FREE_TOGGLE 和 PASSIVE）。
     * <p>
     * 不为 0 时，HUD 在冷却期内显示 ⌛ 倒计时。
     */
    default long getCooldownDuration() { return 0L; }

    // ── 统一状态机 ──────────────────────────────────────────────

    /** 关闭（自由切换型 off） */
    int STATE_DISABLED = 0;
    /** 就绪/开启（可激活、常开） */
    int STATE_READY = 1;
    /** 激活窗口中（仅 ACTIVE_COOLDOWN 模式） */
    int STATE_ACTIVE = 2;
    /** 冷却中 */
    int STATE_COOLING = 3;

    /** 附件控制模式 */
    enum ControlPattern {
        /** 自由开关：DISABLED ↔ READY */
        FREE_TOGGLE,
        /** 激活窗口 + 冷却：READY → ACTIVE → COOLING → READY */
        ACTIVE_COOLDOWN,
        /** 一次性 + 冷却：READY → COOLING → READY */
        ONE_SHOT_COOLDOWN,
        /** 常开：始终 READY */
        ALWAYS_ON,
        /** 纯被动：无状态 */
        PASSIVE
    }

    /** 各附件声明自己的控制模式，默认 PASSIVE */
    default ControlPattern getControlPattern() {
        return ControlPattern.PASSIVE;
    }

    /** 是否可切换 — 从控制模式派生 */
    default boolean isToggleable() {
        return getControlPattern() == ControlPattern.FREE_TOGGLE
                || getControlPattern() == ControlPattern.ACTIVE_COOLDOWN
                || getControlPattern() == ControlPattern.ONE_SHOT_COOLDOWN;
    }
}
