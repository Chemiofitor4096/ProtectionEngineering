package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.block.ArmorEmitterBlock;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PEArmInteractionPointTypes {
    public static final DeferredRegister<ArmInteractionPointType> REGISTRY =
            DeferredRegister.create(CreateRegistries.ARM_INTERACTION_POINT_TYPE, ProtectionEngineering.MODID);

    public static final RegistryObject<ArmorEmitterPointType> ARMOR_EMITTER =
            REGISTRY.register("armor_emitter", ArmorEmitterPointType::new);

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }

    // ── 机械臂交互点 ──────────────────────────────────────────

    /** 交互点类型 — 识别护甲发射器方块 */
    public static class ArmorEmitterPointType extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.getBlock() instanceof ArmorEmitterBlock;
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmorEmitterPoint(this, level, pos, state);
        }
    }

    /** 交互点 — 只放不取；能穿戴时消耗 1 个物品并穿戴，否则返回原样（机械臂跳过） */
    public static class ArmorEmitterPoint extends ArmInteractionPoint {
        public ArmorEmitterPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        protected Vec3 getInteractionPositionVector() {
            // 机械臂指向方块顶部（生物站立处）
            return Vec3.atLowerCornerOf(pos).add(.5f, 1, .5f);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            LivingEntity target = ArmorEmitterBlock.findTarget(level, pos);
            if (target == null || !ArmorEmitterBlock.canEquip(target, stack)) {
                return stack; // 不能穿戴 / 无生物 → 机械臂不选择此点
            }

            // 消耗 1 个：剩余部分返回给机械臂
            ItemStack remainder = stack.copy();
            ItemStack toEquip = remainder.split(1);
            if (!simulate) {
                ArmorEmitterBlock.equip(level, pos, target, toEquip);
            }
            return remainder;
        }

        @Override
        public ItemStack extract(int slot, int amount, boolean simulate) {
            LivingEntity target = ArmorEmitterBlock.findTarget(level, pos);
            if (target == null) return ItemStack.EMPTY;

            // 按当前模式脱下装备 / 拆除附件（从头到脚），simulate 只查不取
            ArmorEmitterBlock.Mode mode = cachedState.getValue(ArmorEmitterBlock.MODE);
            ItemStack result = ArmorEmitterBlock.findRemovable(target, mode);
            if (result.isEmpty()) return ItemStack.EMPTY;

            if (!simulate) {
                ArmorEmitterBlock.removeOne(level, target, mode);
            }
            return result;
        }

        @Override
        public int getSlotCount() {
            return 1; // 单个"槽"：每次机械臂取一件脱下 / 拆除的物品
        }
    }
}
