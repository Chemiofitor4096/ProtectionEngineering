package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachmentHost;
import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EngineerBootsItem extends AttachmentHostArmorItem {

    private static final List<SlotType> SLOTS = List.of(SlotTypes.FOOT, SlotTypes.LINING, SlotTypes.BOOTS_DECORATION);

    public EngineerBootsItem(ArmorMaterial material, Properties properties) {
        super(material, Type.BOOTS, properties);
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        if (stack.getItem() instanceof IAttachmentHost host) {
            var data = host.getAttachments(stack);
            if (data.get(SlotTypes.FOOT).getItem() instanceof ImprovedSolesItem) {
                return true;
            }
        }
        return super.canWalkOnPowderedSnow(stack, wearer);
    }

    @Override
    public List<SlotType> supportedSlots() {
        return SLOTS;
    }
}
