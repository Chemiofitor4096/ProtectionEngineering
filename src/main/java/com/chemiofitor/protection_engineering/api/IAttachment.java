package com.chemiofitor.protection_engineering.api;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
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
     * 附件安装到宿主物品时调用。
     *
     * @param attachment 附件自身的 ItemStack
     * @param host       被安装到的宿主物品 ItemStack（护甲/武器等）
     * @param entity     装备者
     */
    default void onEquip(ItemStack attachment, ItemStack host, LivingEntity entity) {}

    /**
     * 附件从宿主物品卸下时调用。
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

    // ── 客户端渲染 ────────────────────────────────────────────

    /**
     * 创建此附件的 3D 渲染模型。
     * 返回 null 表示该附件无 3D 模型（仅图标）。
     *
     * @param modelSet 用于烘焙模型层的 EntityModelSet
     */
    @Nullable
    default EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return null;
    }

    /**
     * 返回此附件 3D 模型的贴图路径。
     * 返回 null 表示无 3D 模型。
     */
    @Nullable
    default ResourceLocation getAttachmentTexture() {
        return null;
    }

    /** 左臂模型（如额外机械臂），null 表示无 */
    @Nullable
    default EntityModel<?> createLeftArmModel(EntityModelSet modelSet) { return null; }

    @Nullable
    default ResourceLocation getLeftArmTexture() { return null; }

    /** 右臂模型（如额外机械臂），null 表示无 */
    @Nullable
    default EntityModel<?> createRightArmModel(EntityModelSet modelSet) { return null; }

    @Nullable
    default ResourceLocation getRightArmTexture() { return null; }

    /** 左腿模型（如外骨骼），null 表示无 */
    @Nullable
    default EntityModel<?> createLeftLegModel(EntityModelSet modelSet) { return null; }

    @Nullable
    default ResourceLocation getLeftLegTexture() { return null; }

    /** 右腿模型（如外骨骼），null 表示无 */
    @Nullable
    default EntityModel<?> createRightLegModel(EntityModelSet modelSet) { return null; }

    @Nullable
    default ResourceLocation getRightLegTexture() { return null; }

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
     * 向宿主护甲追加属性修饰符。
     * 当护甲被穿戴并计算属性时，通过 {@code ItemAttributeModifierEvent} 调用。
     */
    default void addAttributeModifiers(net.neoforged.neoforge.event.ItemAttributeModifierEvent event) {}

    // ── 减伤 ──────────────────────────────────────────────────

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
