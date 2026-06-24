package com.chemiofitor.protection_engineering.api;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * 宿主物品上的附件数据。自定义 equals/hashCode 用 ItemStack.matches 做值比较。
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
            // 仅比较物品类型，忽略组件变化（防止状态更新触发装备音效）
            if (!ItemStack.matches(entry.getValue(), otherStack)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (var entry : slots.entrySet()) {
            h += Objects.hash(entry.getKey(), ItemStack.hashItemAndComponents(entry.getValue()));
        }
        return h;
    }

    // ── Codec ──────────────────────────────────────────────────
    // Map 的 key 以 ResourceLocation 序列化，反序列化时通过 SlotType.byId 查找

    private static final Codec<Map<SlotType, ItemStack>> SLOTS_CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, ItemStack.OPTIONAL_CODEC)
                    .xmap(
                            raw ->
                                    raw.entrySet().stream()
                                            .filter(e -> !e.getValue().isEmpty())
                                            .collect(
                                                    LinkedHashMap::new,
                                                    (m, e) -> {
                                                        SlotType slot = SlotType.byId(e.getKey());
                                                        if (slot != null) m.put(slot, e.getValue());
                                                    },
                                                    LinkedHashMap::putAll
                                            ),
                            slots ->
                                    slots.entrySet().stream()
                                            .collect(
                                                    LinkedHashMap::new,
                                                    (m, e) -> m.put(e.getKey().id(), e.getValue()),
                                                    LinkedHashMap::putAll
                                            )
                    );

    public static final Codec<AttachmentsData> CODEC =
            SLOTS_CODEC.xmap(AttachmentsData::new, AttachmentsData::slots);

    // ── StreamCodec ────────────────────────────────────────────

    public static final StreamCodec<RegistryFriendlyByteBuf, AttachmentsData> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public AttachmentsData decode(RegistryFriendlyByteBuf buf) {
                    int size = buf.readVarInt();
                    var map = new LinkedHashMap<SlotType, ItemStack>(size);
                    for (int i = 0; i < size; i++) {
                        ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buf);
                        ItemStack stack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                        SlotType slot = SlotType.byId(id);
                        if (slot != null && !stack.isEmpty()) {
                            map.put(slot, stack);
                        }
                    }
                    return new AttachmentsData(map);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, AttachmentsData data) {
                    buf.writeVarInt(data.slots.size());
                    for (var entry : data.slots.entrySet()) {
                        ResourceLocation.STREAM_CODEC.encode(buf, entry.getKey().id());
                        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, entry.getValue());
                    }
                }
            };

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
