package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

public class EngineerHoodItem extends AttachmentHostArmorItem {

    private static final List<SlotType> SLOTS = List.of(SlotTypes.EYES, SlotTypes.MOUTH, SlotTypes.LINING, SlotTypes.DECORATION);

    public EngineerHoodItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.HELMET, properties);
    }

    @Override
    public List<SlotType> supportedSlots() {
        return SLOTS;
    }
}
