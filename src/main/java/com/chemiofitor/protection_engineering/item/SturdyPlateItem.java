package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.PlateAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import javax.annotation.Nullable;

import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * 坚固防护板 —— 胸甲板附件，盔甲值 +2。
 */
public class SturdyPlateItem extends AttachmentItem {

    private static final ResourceLocation ARMOR_ID =
            ProtectionEngineering.asResource("sturdy_plate_armor");
    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/sturdy_plate.png");

    public SturdyPlateItem(Properties properties) {
        super(properties, SlotTypes.CHESTPLATE);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.sturdy_plate";
    }

    @Override
    public float getDamageReduction() { return 0.10f; }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ARMOR,
                new AttributeModifier(ARMOR_ID, 2.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.CHEST);
    }

    // ── 3D 渲染 ──────────────────────────────────────────────

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new PlateAttachmentModel<>(
                modelSet.bakeLayer(PlateAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEX;
    }
}
