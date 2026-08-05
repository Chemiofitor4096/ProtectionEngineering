package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.registry.PEDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import static com.chemiofitor.protection_engineering.registry.PEDataComponents.ATTACHMENT_COOLDOWN;
import static com.chemiofitor.protection_engineering.registry.PEDataComponents.ATTACHMENT_STATE;

/**
 * 附件物品的抽象基类 — 统一状态机。
 */
public abstract class AttachmentItem extends Item implements IAttachment {

    private final Set<SlotType> compatibleSlots;
    private final Set<Holder<MobEffect>> immunities;

    /** 无状态效果免疫的附件 */
    public AttachmentItem(Properties properties, SlotType... slots) {
        this(properties, Set.of(), slots);
    }

    /** 带状态效果免疫的附件 */
    public AttachmentItem(Properties properties, Set<Holder<MobEffect>> immunities, SlotType... slots) {
        super(properties);
        this.compatibleSlots = Set.of(slots);
        this.immunities = Set.copyOf(immunities);
        if (compatibleSlots.isEmpty()) {
            throw new IllegalArgumentException("AttachmentItem must declare at least one compatible slot");
        }
    }

    @Override
    public Set<SlotType> compatibleSlots() { return compatibleSlots; }

    @Override
    public Set<Holder<MobEffect>> getImmunities() { return immunities; }

    // ── 状态机：读写 ──────────────────────────────────────────

    public int getState(ItemStack stack) {
        Integer s = stack.get(ATTACHMENT_STATE.get());
        if (s != null) return s;
        return migrateState(stack);
    }

    protected void setState(ItemStack stack, int state) {
        stack.set(ATTACHMENT_STATE.get(), state);
        // 进入无限状态时清计时器
        if (state == STATE_DISABLED || state == STATE_READY) {
            stack.remove(ATTACHMENT_COOLDOWN.get());
        }
    }

    public long getTimer(ItemStack stack) {
        return stack.getOrDefault(ATTACHMENT_COOLDOWN.get(), 0L);
    }

    protected void setTimer(ItemStack stack, long endTick) {
        stack.set(ATTACHMENT_COOLDOWN.get(), endTick);
    }

    // ── 旧存档迁移 ────────────────────────────────────────────

    private int migrateState(ItemStack stack) {
        Boolean active = stack.get(PEDataComponents.ATTACHMENT_ACTIVE.get());
        if (active == null) {
            return getControlPattern() == ControlPattern.ALWAYS_ON ? STATE_READY : STATE_DISABLED;
        }
        switch (getControlPattern()) {
            case ACTIVE_COOLDOWN: {
                // 有旧冷却计时器 → 迁移到 COOLING
                long oldTimer = stack.getOrDefault(ATTACHMENT_COOLDOWN.get(), 0L);
                if (!active && oldTimer > 0) return STATE_COOLING;
                return STATE_READY; // 激活窗口中或就绪，下个 tick 修正
            }
            case ONE_SHOT_COOLDOWN:
                return active ? STATE_READY : STATE_COOLING;
            case FREE_TOGGLE:
                return active ? STATE_READY : STATE_DISABLED;
            default:
                return STATE_DISABLED;
        }
    }

    // ── 统一查询 ──────────────────────────────────────────────

    public boolean isActive(ItemStack stack) {
        int s = getState(stack);
        return s == STATE_READY || s == STATE_ACTIVE;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        int s = getState(stack);
        return s == STATE_READY || s == STATE_ACTIVE;
    }

    /** 是否处于激活窗口中（仅 ACTIVE_COOLDOWN 模式有意义） */
    public boolean isInActiveWindow(ItemStack stack) {
        return getControlPattern() == ControlPattern.ACTIVE_COOLDOWN
                && getState(stack) == STATE_ACTIVE;
    }

    // ── HUD 用 ────────────────────────────────────────────────

    public char getStateSymbol(ItemStack stack) {
        return switch (getState(stack)) {
            case STATE_READY -> '●';   // ●
            case STATE_ACTIVE -> '⚡';  // ⚡
            case STATE_COOLING -> '⌛'; // ⌛
            default -> '○';            // ○
        };
    }

    public int getStateColor(ItemStack stack) {
        return switch (getState(stack)) {
            case STATE_READY -> 0xFF55FF55;
            case STATE_ACTIVE -> 0xFF55FFFF;
            case STATE_COOLING -> 0xFFFFAA00;
            default -> 0xFF888888;
        };
    }

    // ── 生命周期：onEquip ─────────────────────────────────────

    @Override
    public void onEquip(ItemStack attachment, ItemStack host, LivingEntity entity) {
        if (attachment.has(ATTACHMENT_STATE.get())) return;
        if (attachment.has(PEDataComponents.ATTACHMENT_ACTIVE.get())) return; // 旧数据，等迁移

        switch (getControlPattern()) {
            case FREE_TOGGLE, ONE_SHOT_COOLDOWN, ACTIVE_COOLDOWN, ALWAYS_ON ->
                    setState(attachment, STATE_READY);
            default -> {}
        }
    }

    // ── 生命周期：onTick 计时器到期的状态转换 ──────────────────

    @Override
    public void onTick(ItemStack attachment, ItemStack host, LivingEntity entity, SlotType slot) {
        ControlPattern pattern = getControlPattern();
        if (pattern == ControlPattern.PASSIVE || pattern == ControlPattern.ALWAYS_ON) return;
        if (entity.level().isClientSide()) return;

        long now = entity.level().getGameTime();
        long timer = getTimer(attachment);
        int state = getState(attachment);

        // 损坏恢复：需要计时器但没有 → 回到就绪
        if (timer == 0) {
            if (state == STATE_ACTIVE || state == STATE_COOLING) {
                setState(attachment, STATE_READY);
            }
            return;
        }

        switch (state) {
            case STATE_ACTIVE:
                if (now >= timer) {
                    setState(attachment, STATE_COOLING);
                    setTimer(attachment, now + getCooldownDuration());
                    onStateExit(attachment, STATE_ACTIVE, entity);
                }
                break;

            case STATE_COOLING:
                if (now >= timer) {
                    setState(attachment, STATE_READY);
                    onStateEnter(attachment, STATE_READY, entity);
                }
                break;

            default:
                // READY/DISABLED 无计时器，忽略
                break;
        }
    }

    // ── 热键：统一 onActivatePress ────────────────────────────

    public void onActivatePress(ItemStack stack, ItemStack host, LivingEntity entity) {
        ControlPattern pattern = getControlPattern();
        if (pattern == ControlPattern.PASSIVE || pattern == ControlPattern.ALWAYS_ON) return;

        boolean isCreative = entity instanceof Player p && p.getAbilities().instabuild;
        long now = entity.level().getGameTime();
        int state = getState(stack);

        switch (pattern) {
            case FREE_TOGGLE -> {
                if (state == STATE_DISABLED) {
                    setState(stack, STATE_READY);
                    onStateEnter(stack, STATE_READY, entity);
                } else {
                    onStateExit(stack, STATE_READY, entity);
                    setState(stack, STATE_DISABLED);
                }
            }

            case ACTIVE_COOLDOWN -> {
                if (isCreative) {
                    // 创造模式：自由切换 READY ↔ ACTIVE
                    if (state == STATE_READY) {
                        setState(stack, STATE_ACTIVE);
                        onStateEnter(stack, STATE_ACTIVE, entity);
                    } else if (state == STATE_ACTIVE) {
                        onStateExit(stack, STATE_ACTIVE, entity);
                        setState(stack, STATE_READY);
                    }
                    return;
                }
                if (state != STATE_READY) return;
                setState(stack, STATE_ACTIVE);
                setTimer(stack, now + getActiveDuration());
                onStateEnter(stack, STATE_ACTIVE, entity);
            }

            case ONE_SHOT_COOLDOWN -> {
                if (isCreative) {
                    onActivateOnce(stack, host, entity);
                    return;
                }
                if (state != STATE_READY) return;
                onActivateOnce(stack, host, entity);
                setState(stack, STATE_COOLING);
                setTimer(stack, now + getCooldownDuration());
            }
        }
    }

    // ── 子类钩子 ──────────────────────────────────────────────

    /** ONE_SHOT_COOLDOWN 激活时的效果（子类覆写） */
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity entity) {}

    /** 进入某状态时调用（子类覆写） */
    protected void onStateEnter(ItemStack stack, int newState, LivingEntity entity) {}

    /** 离开某状态时调用（子类覆写） */
    protected void onStateExit(ItemStack stack, int oldState, LivingEntity entity) {}

    // ── 属性：声明统一应用 ────────────────────────────────────
    // 子类只需覆写 getAttributeBonuses() 声明属性，
    // 此处统一应用到宿主护甲（实际生效）。声明一次，tooltip 复用。

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        EquipmentSlotGroup group = resolveSlotGroup(event);
        // 修饰符 ID 追加宿主装备部件后缀（chest/legs/feet/head…）：
        // 同一附件装到不同护甲部件时 ID 唯一，可跨护甲堆叠（实体属性按 ID 去重）。
        String hostSuffix = group.getSerializedName();
        for (IAttachment.AttributeBonus bonus : getAttributeBonuses()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    bonus.id().getNamespace(), bonus.id().getPath() + "_" + hostSuffix);
            event.replaceModifier(bonus.attribute(),
                    new AttributeModifier(id, bonus.amount(), bonus.operation()),
                    group);
        }
    }

    /**
     * 属性生效的装备槽组：优先按宿主护甲的实际部件决定 —— 通用槽附件（LINING/装饰）
     * 装到胸甲→CHEST、护腿→LEGS、靴子→FEET、头盔→HEAD，精确跟随安装位置；
     * 非护甲宿主（武器附件等预留）回退到兼容槽位的静态推导。
     */
    private EquipmentSlotGroup resolveSlotGroup(ItemAttributeModifierEvent event) {
        if (event.getItemStack().getItem() instanceof ArmorItem armor) {
            return switch (armor.getEquipmentSlot()) {
                case HEAD -> EquipmentSlotGroup.HEAD;
                case CHEST -> EquipmentSlotGroup.CHEST;
                case LEGS -> EquipmentSlotGroup.LEGS;
                case FEET -> EquipmentSlotGroup.FEET;
                default -> EquipmentSlotGroup.ANY;
            };
        }
        return resolveSlotGroupFromSlots();
    }

    /** 按兼容槽位静态推导（非护甲宿主兜底）：同组 → 该组；跨组 / 未映射 → ANY */
    private EquipmentSlotGroup resolveSlotGroupFromSlots() {
        EquipmentSlotGroup result = null;
        for (SlotType slot : compatibleSlots) {
            EquipmentSlotGroup group = slot.equipmentSlotGroup();
            if (result == null) result = group;
            else if (result != group) return EquipmentSlotGroup.ANY;
        }
        return result != null ? result : EquipmentSlotGroup.ANY;
    }

    // ── Tooltip ────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.translatable("tooltip.protectionengineering.slot")
                .withStyle(ChatFormatting.GOLD));

        for (SlotType slot : compatibleSlots) {
            tooltip.add(Component.literal("  ")
                    .append(Component.translatable(slot.getTranslationKey()))
                    .withStyle(ChatFormatting.GRAY));
        }

        if (!immunities.isEmpty()) {
            MutableComponent line = Component.literal("  ");
            boolean first = true;
            for (Holder<MobEffect> effect : immunities) {
                if (!first) line.append(Component.literal(" "));
                first = false;
                line.append(Component.translatable(effect.value().getDescriptionId()));
            }
            tooltip.add(Component.translatable("tooltip.protectionengineering.immunities")
                    .withStyle(ChatFormatting.GOLD));
            tooltip.add(line.withStyle(ChatFormatting.AQUA));
        }

        String featureKey = getFeatureKey();
        if (featureKey != null) {
            tooltip.add(Component.translatable("tooltip.protectionengineering.feature")
                    .withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.literal("  ")
                    .append(Component.translatable(featureKey))
                    .withStyle(ChatFormatting.BLUE));
        }

        // 穿戴属性（位于 tooltip 最下方；"当作为部件安装时：+X 属性"）
        List<IAttachment.AttributeBonus> bonuses = getAttributeBonuses();
        if (!bonuses.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.protectionengineering.worn")
                    .withStyle(ChatFormatting.GOLD));
            for (IAttachment.AttributeBonus bonus : bonuses) {
                tooltip.add(Component.literal(" ").append(formatAttributeBonus(bonus)));
            }
        }
    }

    // ── 属性 tooltip 格式化（对齐原版 item.modifiers / attribute.modifier 风格） ──

    /** 渲染单条属性："+4 护甲" / "+20% 攻击伤害"，复用原版属性名与 +/- 翻译 key */
    private static Component formatAttributeBonus(IAttachment.AttributeBonus bonus) {
        AttributeModifier.Operation operation = bonus.operation();
        boolean positive = bonus.amount() >= 0;
        int opIndex = operation == AttributeModifier.Operation.ADD_VALUE ? 0 : 1;
        double display = operation == AttributeModifier.Operation.ADD_VALUE
                ? bonus.amount() : bonus.amount() * 100;
        String key = positive ? "attribute.modifier.plus." + opIndex
                              : "attribute.modifier.takes." + opIndex;
        // 原版格式是 "+%s %s"（plus.1 为 "+%s%% %s"）：数值与属性名都必须作为占位参数传入
        return Component.translatable(key,
                formatDecimal(Math.abs(display)),
                Component.translatable(bonus.attribute().value().getDescriptionId()))
                .withStyle(ChatFormatting.BLUE);
    }

    /** 去尾零数值：4.0 → "4"，0.4 → "0.4"，0.06 → "0.06" */
    private static String formatDecimal(double value) {
        return new DecimalFormat("#.##").format(value);
    }
}
