package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.SlotType;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;

import java.util.Set;

public class EngineerBootsItem extends AttachmentHostArmorItem {

    private static final Set<SlotType> SLOTS = Set.of(SlotTypes.FOOT);

    public EngineerBootsItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.BOOTS, properties);
    }

    @Override
    public Set<SlotType> supportedSlots() {
        return SLOTS;
    }
}
