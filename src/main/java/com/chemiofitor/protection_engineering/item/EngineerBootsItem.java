package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

public class EngineerBootsItem extends AttachmentHostArmorItem {

    private static final List<SlotType> SLOTS = List.of(SlotTypes.FOOT, SlotTypes.LINING, SlotTypes.DECORATION);

    public EngineerBootsItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.BOOTS, properties);
    }

    @Override
    public List<SlotType> supportedSlots() {
        return SLOTS;
    }
}
