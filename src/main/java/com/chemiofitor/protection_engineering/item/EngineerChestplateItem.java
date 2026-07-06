package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.AttachmentsData;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EngineerChestplateItem extends AttachmentHostArmorItem {

    private static final List<SlotType> SLOTS = List.of(
            SlotTypes.SHOULDER, SlotTypes.CHESTPLATE, SlotTypes.BACK, SlotTypes.ARM, SlotTypes.LINING, SlotTypes.CHESTPLATE_DECORATION
    );

    public EngineerChestplateItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.CHESTPLATE, properties);
    }

    @Override
    public List<SlotType> supportedSlots() {
        return SLOTS;
    }

    /** 如果安装了激活的喷气背包，则允许鞘翅飞行 */
    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return hasActiveJetpack(stack);
    }

    /** 维持鞘翅飞行 —— 必须重写，IItemExtension 默认返回 false */
    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return hasActiveJetpack(stack);
    }

    private boolean hasActiveJetpack(ItemStack stack) {
        AttachmentsData data = getAttachments(stack);
        for (var entry : data.slots().entrySet()) {
            if (entry.getValue().getItem() instanceof JetpackItem jetpack
                    && jetpack.isActive(entry.getValue())) {
                return true;
            }
        }
        return false;
    }
}
