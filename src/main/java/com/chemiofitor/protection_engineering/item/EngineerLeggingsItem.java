package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

public class EngineerLeggingsItem extends AttachmentHostArmorItem {

    private static final List<SlotType> SLOTS = List.of(SlotTypes.LEG, SlotTypes.KNEE, SlotTypes.LINING, SlotTypes.LEGGINGS_DECORATION);

    public EngineerLeggingsItem(ArmorMaterial material, Properties properties) {
        super(material, Type.LEGGINGS, properties);
    }

    @Override
    public List<SlotType> supportedSlots() {
        return SLOTS;
    }
}
