package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

/**
 * 护甲材质注册。
 * <p>
 * 工程师护甲 = 下界合金级别护甲值 + 150% 下界合金耐久。
 * 修补材料：黄铜板（弱）/ 坚固板（强），强度见 {@link PERepairMaterials}。
 */
public class PEArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> REGISTRY =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ProtectionEngineering.MODID);

    /** 耐久因子: 下界合金 37 × 1.5 = 55.5 → 56 */
    public static final int DURABILITY_FACTOR = 56;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ENGINEER_ARMOR =
            REGISTRY.register("engineer_armor", () -> new ArmorMaterial(
                    // 护甲值同下界合金
                    Map.of(
                            ArmorItem.Type.HELMET,      3,
                            ArmorItem.Type.CHESTPLATE,  8,
                            ArmorItem.Type.LEGGINGS,    6,
                            ArmorItem.Type.BOOTS,       3
                    ),
                    15,                                 // 附魔值同下界合金
                    PESounds.EQUIP_ENGINEER_ARMOR,       // 工程师护甲音效
                    PERepairMaterials::asIngredient,     // 黄铜板 / 坚固板（强度分级）
                    List.of(new ArmorMaterial.Layer(ProtectionEngineering.asResource("engineer"))),
                    3.0F,                               // 韧性同下界合金
                    0.1F                                // 击退抗性同下界合金
            ));
}
