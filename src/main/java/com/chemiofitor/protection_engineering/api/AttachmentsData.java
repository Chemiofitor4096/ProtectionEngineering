package com.chemiofitor.protection_engineering.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 宿主物品上的附件数据。自定义 equals/hashCode 用 ItemStack.matches 做值比较。
 * <p>
 * 1.20.1 无 Data Component 系统，序列化改用 NBT：
 * 宿主 ItemStack 的 tag 内以 {@code attachments} 键存一张 {@code slotId -> ItemStack tag} 的表。
 */
public final class AttachmentsData {

    private final Map<SlotType, ItemStack> slots;

    public static final AttachmentsData EMPTY = new AttachmentsData(Map.of());

    public AttachmentsData(Map<SlotType, ItemStack> slots) {
        this.slots = Map.copyOf(slots);
    }

    public Map<SlotType, ItemStack> slots() { return slots; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttachmentsData other)) return false;
        if (slots.size() != other.slots.size()) return false;
        for (var entry : slots.entrySet()) {
            ItemStack otherStack = other.slots.get(entry.getKey());
            if (otherStack == null) return false;
            // 仅比较物品类型，忽略 tag 变化（防止状态更新触发装备音效）
            if (!ItemStack.matches(entry.getValue(), otherStack)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (var entry : slots.entrySet()) {
            h += Objects.hash(entry.getKey(), entry.getValue().hashCode());
        }
        return h;
    }

    // ── NBT 序列化 ──────────────────────────────────────────────
    // 序列化形式：{ slotId : { ItemStack tag } }

    /** 序列化为独立 CompoundTag（不含外层容器键） */
    public CompoundTag toTag() {
        CompoundTag data = new CompoundTag();
        for (var entry : slots.entrySet()) {
            data.put(entry.getKey().id().toString(), entry.getValue().save(new CompoundTag()));
        }
        return data;
    }

    /** 从序列化的 CompoundTag 反序列化 */
    public static AttachmentsData fromTag(CompoundTag data) {
        var map = new LinkedHashMap<SlotType, ItemStack>();
        for (String key : data.getAllKeys()) {
            SlotType slot = SlotType.byId(key);
            ItemStack stack = ItemStack.of(data.getCompound(key));
            if (!stack.isEmpty()) {
                map.put(slot, stack);
            }
        }
        return new AttachmentsData(map);
    }

    // ── 网络编解码 ──────────────────────────────────────────────

    /** 写入 FriendlyByteBuf（writeItem 注册表感知） */
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeVarInt(slots.size());
        for (var entry : slots.entrySet()) {
            SlotType.toNetwork(buf, entry.getKey());
            buf.writeItem(entry.getValue());
        }
    }

    /** 从 FriendlyByteBuf 解码 */
    public static AttachmentsData fromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        var map = new LinkedHashMap<SlotType, ItemStack>(size);
        for (int i = 0; i < size; i++) {
            SlotType slot = SlotType.fromNetwork(buf);
            ItemStack stack = buf.readItem();
            if (!stack.isEmpty()) {
                map.put(slot, stack);
            }
        }
        return new AttachmentsData(map);
    }

    // ── Accessors ──────────────────────────────────────────────

    public ItemStack get(SlotType slot) {
        return slots.getOrDefault(slot, ItemStack.EMPTY);
    }

    public AttachmentsData with(SlotType slot, ItemStack stack) {
        if (stack.isEmpty()) return without(slot);
        var copy = new LinkedHashMap<>(slots);
        copy.put(slot, stack);
        return new AttachmentsData(copy);
    }

    public AttachmentsData without(SlotType slot) {
        if (!slots.containsKey(slot)) return this;
        var copy = new LinkedHashMap<>(slots);
        copy.remove(slot);
        return new AttachmentsData(copy);
    }

    public boolean isEmpty() {
        return slots.isEmpty();
    }
}
