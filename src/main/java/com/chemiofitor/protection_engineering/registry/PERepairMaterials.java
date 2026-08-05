package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.config.PEServerConfig;
import com.simibubi.create.AllItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import com.chemiofitor.protection_engineering.api.IGradedRepair;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * 工程师装备的分级修补材料表。
 * <p>
 * 强度以「单位」计量，{@link com.chemiofitor.protection_engineering.api.IGradedRepair#FULL_REPAIR_UNITS}
 * 个单位 = 满耐久（1 单位 = 5%）。原版铁锭等效 5 单位（25%）。
 * <p>
 * 材料引用延迟解析（Create 物品在注册冻结后才可用），强度值来自
 * {@link PEServerConfig}，运行时改配置即时生效。
 */
public class PERepairMaterials {

    /** 材料 → 强度（单位），保持插入顺序以便生成稳定的 Ingredient */
    private static final Map<Supplier<? extends ItemLike>, IntSupplier> ENTRIES = new LinkedHashMap<>();

    /** 延迟构建的查表缓存 */
    private static Map<Item, IntSupplier> resolved;

    /** 延迟构建的 Ingredient 缓存（成员固定，与配置无关） */
    private static Ingredient ingredient;

    static {
        register(() -> AllItems.BRASS_SHEET, PEServerConfig.REPAIR_UNITS_BRASS_SHEET::get);
        register(() -> AllItems.STURDY_SHEET, PEServerConfig.REPAIR_UNITS_STURDY_SHEET::get);
    }

    /**
     * 注册一种分级修补材料。需在注册阶段调用（不要传入已解析的 Item 实例）。
     *
     * @param material 材料延迟引用
     * @param units    强度（单位）供应器
     */
    public static void register(Supplier<? extends ItemLike> material, IntSupplier units) {
        ENTRIES.put(material, units);
        resolved = null;
        ingredient = null;
    }

    /** 便捷重载：固定强度 */
    public static void register(Supplier<? extends ItemLike> material, int units) {
        register(material, () -> units);
    }

    /**
     * 查询材料强度。
     *
     * @return 单位数，0 表示该材料未注册
     */
    public static int getUnits(ItemStack material) {
        if (material.isEmpty()) return 0;
        IntSupplier units = table().get(material.getItem());
        return units == null ? 0 : Math.max(0, units.getAsInt());
    }

    /** 材料是否登记在表内（不论强度是否被配置调为 0） */
    public static boolean contains(ItemStack material) {
        return !material.isEmpty() && table().containsKey(material.getItem());
    }

    /**
     * 一条修补材料记录，供 tooltip 展示。
     *
     * @param material 材料物品
     * @param units    强度（单位）
     */
    public record Entry(Item material, int units) {
        /** 恢复比例（0-1） */
        public float ratio() {
            return (float) units / IGradedRepair.FULL_REPAIR_UNITS;
        }
    }

    /**
     * 当前生效的修补材料清单（已过滤强度为 0 的项），按注册顺序。
     * <p>
     * 强度读自服务端配置；配置未就绪时（如主菜单）返回空列表。
     */
    public static List<Entry> listEntries() {
        if (!PEServerConfig.SPEC.isLoaded()) return List.of();

        List<Entry> list = new ArrayList<>();
        for (var entry : table().entrySet()) {
            int units = Math.max(0, entry.getValue().getAsInt());
            if (units > 0) list.add(new Entry(entry.getKey(), units));
        }
        return list;
    }

    /**
     * 所有登记材料构成的 Ingredient，供 {@code ArmorMaterial} 的修补材料使用。
     * <p>
     * 让原版 {@code isValidRepairItem} 认可这些材料（铁砧结果由
     * {@code PEAnvilEvents} 接管，此处仅用于合法性判定与配方书展示）。
     */
    public static Ingredient asIngredient() {
        Ingredient cached = ingredient;
        if (cached == null) {
            cached = Ingredient.of(ENTRIES.keySet().stream().map(s -> new ItemStack(s.get())));
            ingredient = cached;
        }
        return cached;
    }

    private static Map<Item, IntSupplier> table() {
        Map<Item, IntSupplier> map = resolved;
        if (map == null) {
            map = new LinkedHashMap<>();
            for (var entry : ENTRIES.entrySet()) {
                map.put(entry.getKey().get().asItem(), entry.getValue());
            }
            resolved = map;
        }
        return map;
    }

}
