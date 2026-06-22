package com.chemiofitor.protection_engineering.api;

import com.mojang.serialization.Codec;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 槽位类型 —— 纯标识符，不硬编码它属于谁。
 * <p>
 * 用 {@link ResourceLocation} 作为唯一标识，{@link SlotCategory} 只是分组标签。
 * 通过内部注册表实现 {@link #byId(ResourceLocation)} 查找。
 * <p>
 * 新增槽位只需调用 {@link #register} 或在 {@link SlotTypes} 加一行 ——
 * 不需要改任何数据结构的 struct 定义。
 * <p>
 * 设计约束：
 * <ul>
 *   <li>record 自动提供 equals/hashCode，可作为 Map key</li>
 *   <li>CODEC 以 ResourceLocation 为持久化形式，反序列化时查注册表</li>
 *   <li>未知 id 反序列化时创建临时实例（category=UTILITY），不会丢数据</li>
 * </ul>
 */
public record SlotType(ResourceLocation id, SlotCategory category) {

    /** 槽位的粗略分组标签，不做权限判断 */
    public enum SlotCategory {
        ARMOR, WEAPON, TOOL, UTILITY
    }

    // ── 内部注册表 ──────────────────────────────────────────────

    private static final Map<ResourceLocation, SlotType> REGISTRY = new HashMap<>();

    /** 注册一个新槽位类型，返回注册后的实例 */
    public static synchronized SlotType register(ResourceLocation id, SlotCategory category) {
        if (REGISTRY.containsKey(id)) {
            throw new IllegalArgumentException("SlotType already registered: " + id);
        }
        var type = new SlotType(id, category);
        REGISTRY.put(id, type);
        return type;
    }

    /** 通过 ResourceLocation 查找已注册的槽位，未注册的返回临时实例(UTILITY) */
    public static SlotType byId(ResourceLocation id) {
        return REGISTRY.computeIfAbsent(id, k -> new SlotType(k, SlotCategory.UTILITY));
    }

    /** 通过字符串 id 查找，解析失败返回 null */
    public static SlotType byId(String id) {
        try {
            return byId(ResourceLocation.parse(id));
        } catch (ResourceLocationException e) {
            return null;
        }
    }

    /** 不可变视图 */
    public static Map<ResourceLocation, SlotType> registry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    // ── Codec ──────────────────────────────────────────────────

    /** 序列化为 ResourceLocation 字符串，反序列化通过 {@link #byId} 查找 */
    public static final Codec<SlotType> CODEC =
            ResourceLocation.CODEC.xmap(SlotType::byId, SlotType::id);

    // ── StreamCodec ────────────────────────────────────────────

    public static final StreamCodec<FriendlyByteBuf, SlotType> STREAM_CODEC =
            StreamCodec.of(
                    (buf, type) -> ResourceLocation.STREAM_CODEC.encode(buf, type.id),
                    buf -> byId(ResourceLocation.STREAM_CODEC.decode(buf))
            );

    // ── 便利方法 ────────────────────────────────────────────────

    /** 翻译 key：slot.<namespace>.<path> */
    public String getTranslationKey() {
        return "slot." + id.getNamespace() + "." + id.getPath();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
