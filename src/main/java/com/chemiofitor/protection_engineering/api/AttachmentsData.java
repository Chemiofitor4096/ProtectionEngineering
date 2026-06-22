package com.chemiofitor.protection_engineering.api;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 宿主物品上的附件数据。
 * <p>
 * 用 {@link Map}{@code <SlotType, ItemStack>} 存储，只存非空值。
 * 不同宿主物品（头盔、胸甲、锯剑）各自只存自己支持的槽位子集，
 * 没有"定长 N"的限制。
 * <p>
 * 不可变 —— {@link #with} / {@link #without} 返回新实例。
 */
public record AttachmentsData(Map<SlotType, ItemStack> slots) {

    public static final AttachmentsData EMPTY = new AttachmentsData(Map.of());

    /** 防御性拷贝，确保不可变 */
    public AttachmentsData {
        slots = Map.copyOf(slots);
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
