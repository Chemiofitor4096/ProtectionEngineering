package com.chemiofitor.protection_engineering.block;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.item.AttachmentItem;
import com.chemiofitor.protection_engineering.registry.PESounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 护甲发射器 — 压力板式方块。
 * <p>
 * 有可佩戴装备的生物站在上方时，机械动力机械臂（Mechanical Arm）会把它当作交互点：
 * 递送的护甲自动装备到对应槽位，本模组附件自动安装到宿主护甲；机械臂也可按模式
 * 从头到脚依次脱下装备 / 拆除附件。
 * <p>
 * Shift + 右键循环切换模式（{@link Mode}）：脱装备（默认，不拆附件）/ 拆附件 / 全部移除。
 * 三个模式都支持自动穿戴与自动安装附件。
 */
public class ArmorEmitterBlock extends Block {

    /** 碰撞箱 (0, -0.5, 0) ~ (16, 1.5, 16)，单位为 1/16 格 */
    private static final VoxelShape SHAPE = Block.box(0, -0.5, 0, 16, 1.5, 16);

    /** 操作模式：脱装备（默认）/ 拆附件 / 全部移除 */
    public enum Mode implements StringRepresentable {
        EQUIP("equip"),
        ATTACH("attach"),
        BOTH("both");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public Mode next() {
            return switch (this) {
                case EQUIP -> ATTACH;
                case ATTACH -> BOTH;
                case BOTH -> EQUIP;
            };
        }
    }

    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    private static final EquipmentSlot[] HEAD_TO_FEET = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public ArmorEmitterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(MODE, Mode.EQUIP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MODE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    // ── 玩家交互：Shift + 右键切换模式 ─────────────────────────

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        Mode next = state.getValue(MODE).next();
        level.setBlock(pos, state.setValue(MODE, next), 3);
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
        player.displayClientMessage(
                Component.translatable("message.protectionengineering.armor_emitter.mode",
                        Component.translatable("mode.protectionengineering.armor_emitter." + next.getSerializedName())),
                true);
        return InteractionResult.SUCCESS;
    }

    // ── 机械臂交互点共用的穿戴逻辑 ─────────────────────────────

    /** 找方块内 / 上方第一个可穿戴装备的生物 */
    public static LivingEntity findTarget(Level level, BlockPos pos) {
        AABB aabb = new AABB(pos).expandTowards(0, 1, 0);
        return level.getEntitiesOfClass(LivingEntity.class, aabb).stream().findFirst().orElse(null);
    }

    /** 该生物能否穿戴 / 安装该物品 */
    public static boolean canEquip(LivingEntity target, ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armor) {
            return target.getItemBySlot(armor.getEquipmentSlot()).isEmpty();
        }
        if (stack.getItem() instanceof AttachmentItem) {
            for (EquipmentSlot armorSlot : HEAD_TO_FEET) {
                ItemStack armorStack = target.getItemBySlot(armorSlot);
                if (!(armorStack.getItem() instanceof IAttachmentHost host)) continue;
                for (SlotType slot : host.supportedSlots()) {
                    if (host.getAttachments(armorStack).get(slot).isEmpty()
                            && host.canInstall(slot, stack)) return true;
                }
            }
        }
        return false;
    }

    /** 执行穿戴 / 安装（stack 为单个物品，应用后归属目标） */
    public static void equip(Level level, BlockPos pos, LivingEntity target, ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armor) {
            EquipmentSlot slot = armor.getEquipmentSlot();
            target.setItemSlot(slot, stack);
            if (armor.getEquipSound() != null) {
                level.playSound(null, target, armor.getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return;
        }
        if (stack.getItem() instanceof AttachmentItem) {
            for (EquipmentSlot armorSlot : HEAD_TO_FEET) {
                ItemStack armorStack = target.getItemBySlot(armorSlot);
                if (!(armorStack.getItem() instanceof IAttachmentHost host)) continue;
                for (SlotType slot : host.supportedSlots()) {
                    if (host.getAttachments(armorStack).get(slot).isEmpty()
                            && host.canInstall(slot, stack)) {
                        host.install(armorStack, slot, stack);
                        if (stack.getItem() instanceof IAttachment att) {
                            att.onEquip(stack, armorStack, target);
                        }
                        level.playSound(null, pos, PESounds.ATTACH_1.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
                        return;
                    }
                }
            }
        }
    }

    // ── 脱下 / 拆除逻辑（机械臂 extract 调用）────────────────

    /** 按模式找第一个可脱下的物品（装备或附件，从头到脚），不实际移除 */
    public static ItemStack findRemovable(LivingEntity target, Mode mode) {
        if (mode == Mode.EQUIP || mode == Mode.BOTH) {
            for (EquipmentSlot slot : HEAD_TO_FEET) {
                if (!target.getItemBySlot(slot).isEmpty()) {
                    return target.getItemBySlot(slot).copy();
                }
            }
        }
        if (mode == Mode.ATTACH || mode == Mode.BOTH) {
            for (EquipmentSlot armorSlot : HEAD_TO_FEET) {
                ItemStack armorStack = target.getItemBySlot(armorSlot);
                if (!(armorStack.getItem() instanceof IAttachmentHost host)) continue;
                for (SlotType slotType : host.supportedSlots()) {
                    ItemStack att = host.getAttachments(armorStack).get(slotType);
                    if (!att.isEmpty()) return att.copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /** 按模式实际脱下 / 拆下第一个物品（从头到脚），返回被移除的物品 */
    public static ItemStack removeOne(Level level, LivingEntity target, Mode mode) {
        if (mode == Mode.EQUIP || mode == Mode.BOTH) {
            for (EquipmentSlot slot : HEAD_TO_FEET) {
                ItemStack stack = target.getItemBySlot(slot);
                if (stack.isEmpty()) continue;
                ItemStack removed = stack.copy();
                target.setItemSlot(slot, ItemStack.EMPTY);
                if (stack.getItem() instanceof ArmorItem armor && armor.getEquipSound() != null) {
                    level.playSound(null, target, armor.getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return removed;
            }
        }
        if (mode == Mode.ATTACH || mode == Mode.BOTH) {
            for (EquipmentSlot armorSlot : HEAD_TO_FEET) {
                ItemStack armorStack = target.getItemBySlot(armorSlot);
                if (!(armorStack.getItem() instanceof IAttachmentHost host)) continue;
                for (SlotType slotType : host.supportedSlots()) {
                    if (host.getAttachments(armorStack).get(slotType).isEmpty()) continue;
                    ItemStack removed = host.remove(armorStack, slotType);
                    if (removed.getItem() instanceof IAttachment att) {
                        att.onUnequip(removed, armorStack, target);
                    }
                    level.playSound(null, target.blockPosition(), PESounds.ATTACH_1.get(),
                            SoundSource.BLOCKS, 0.8F, 1.0F);
                    return removed;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
