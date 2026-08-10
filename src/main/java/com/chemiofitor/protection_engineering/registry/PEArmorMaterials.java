package com.chemiofitor.protection_engineering.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 护甲材质 — 1.20.1（本环境）的 ArmorMaterial 为接口。
 * <p>
 * 工程师护甲 = 下界合金级别护甲值 + 150% 下界合金耐久。
 * 修补材料：黄铜板（弱）/ 坚固板（强），强度见 {@link PERepairMaterials}。
 */
public final class PEArmorMaterials {

    private PEArmorMaterials() {}

    /** 耐久因子: 下界合金 37 × 1.5 = 55.5 → 56 */
    public static final int DURABILITY_FACTOR = 56;

    /** 各部件基础耐久倍率（现代护甲重做后的取值，与 1.21.1 版本一致） */
    private static int baseDurability(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 13;
            case CHESTPLATE -> 16;
            case LEGGINGS -> 15;
            case BOOTS -> 11;
        };
    }

    /** 部件耐久 = 基础倍率 × 耐久因子 */
    public static int typeDurability(ArmorItem.Type type) {
        return baseDurability(type) * DURABILITY_FACTOR;
    }

    public static final ArmorMaterial ENGINEER_ARMOR = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return typeDurability(type);
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 3;
                case CHESTPLATE -> 8;
                case LEGGINGS -> 6;
                case BOOTS -> 3;
            };
        }

        @Override
        public int getEnchantmentValue() {
            return 15; // 附魔值同下界合金
        }

        @Override
        public SoundEvent getEquipSound() {
            return PESounds.EQUIP_ENGINEER_ARMOR.get();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return PERepairMaterials.asIngredient();
        }

        @Override
        public String getName() {
            return "engineer";
        }

        @Override
        public float getToughness() {
            return 3.0F; // 韧性同下界合金
        }

        @Override
        public float getKnockbackResistance() {
            return 0.1F; // 击退抗性同下界合金
        }
    };
}
