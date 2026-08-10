package com.chemiofitor.protection_engineering.api;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * 附件遍历工具 — 消除 PEGameEvents / PEKeyBindings / PECooldownOverlay 等
 * 文件中重复的"遍历护甲→检查Host→迭代附件"样板代码。
 */
public final class AttachmentUtil {
    private AttachmentUtil() {}

    // ── 核心遍历（其他方法复用）───────────────────────────────

    /** 遍历玩家所有已安装的附件（含盔甲槽位 / 附件槽位上下文） */
    private static void forAll(Player player, Consumer<Match<?>> consumer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (!(armor.getItem() instanceof IAttachmentHost host)) continue;
            for (var entry : host.getAttachments(armor).slots().entrySet()) {
                ItemStack stack = entry.getValue();
                if (stack.getItem() instanceof IAttachment att) {
                    consumer.accept(new Match<>(slot, entry.getKey(), stack, att));
                }
            }
        }
    }

    // ── 按类查找 ──────────────────────────────────────────────

    /** 在所有护甲上查找第一个指定类型的附件。 */
    public static <T extends IAttachment> Optional<Match<T>> findFirst(Player player, Class<T> type) {
        final Optional<Match<T>>[] result = new Optional[]{Optional.empty()};
        forAll(player, match -> {
            if (result[0].isPresent()) return;
            if (type.isInstance(match.attachment())) {
                result[0] = Optional.of(new Match<>(match.armorSlot(), match.attachSlot(),
                        match.stack(), type.cast(match.attachment())));
            }
        });
        return result[0];
    }

    /** 检查玩家是否安装了指定类型的附件。 */
    public static boolean has(Player player, Class<? extends IAttachment> type) {
        return findFirst(player, type).isPresent();
    }

    /** 检查任一护甲槽位上是否有满足条件的附件（遍历全部护甲）。 */
    public static boolean any(Player player, Predicate<ItemStack> pred) {
        final boolean[] found = {false};
        forAll(player, match -> {
            if (!found[0] && pred.test(match.stack())) found[0] = true;
        });
        return found[0];
    }

    /** 检查指定护甲槽位上是否有满足条件的附件。 */
    public static boolean anyOnSlot(Player player, EquipmentSlot armorSlot, Predicate<ItemStack> pred) {
        ItemStack armor = player.getItemBySlot(armorSlot);
        if (!(armor.getItem() instanceof IAttachmentHost host)) return false;
        for (var entry : host.getAttachments(armor).slots().entrySet()) {
            if (pred.test(entry.getValue())) return true;
        }
        return false;
    }

    // ── 直接槽位访问 ──────────────────────────────────────────

    /** 获取指定护甲槽位 + 附件槽位上的 ItemStack。 */
    public static ItemStack get(Player player, EquipmentSlot armorSlot, SlotType attachSlot) {
        ItemStack armor = player.getItemBySlot(armorSlot);
        if (!(armor.getItem() instanceof IAttachmentHost host)) return ItemStack.EMPTY;
        return host.getAttachments(armor).get(attachSlot);
    }

    // ── 遍历 / 归约 ────────────────────────────────────────────

    /** 遍历玩家所有已安装的附件。 */
    public static void forEach(Player player, BiConsumer<Match<?>, IAttachment> consumer) {
        forAll(player, match -> consumer.accept(match, match.attachment()));
    }

    /** 遍历指定护甲槽位的所有附件。 */
    public static void forEachOnSlot(Player player, EquipmentSlot armorSlot, BiConsumer<SlotType, ItemStack> consumer) {
        ItemStack armor = player.getItemBySlot(armorSlot);
        if (!(armor.getItem() instanceof IAttachmentHost host)) return;
        for (var entry : host.getAttachments(armor).slots().entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    /** 归约 — 对所有附件累加浮点值（如减伤、摔落减免）。 */
    public static double reduce(Player player, ToDoubleFunction<IAttachment> mapper) {
        final double[] total = {0};
        forAll(player, match -> total[0] += mapper.applyAsDouble(match.attachment()));
        return total[0];
    }

    // ── 结果类型 ──────────────────────────────────────────────

    /** 一次查找匹配的结果，包含上下文信息。 */
    public record Match<T extends IAttachment>(
            EquipmentSlot armorSlot,
            SlotType attachSlot,
            ItemStack stack,
            T attachment
    ) {}
}
