package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.client.model.NetheritePlateAttachmentModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import javax.annotation.Nullable;

import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

/**
 * 下界合金防护板 —— 胸甲板附件，盔甲值 +3。
 */
public class NetheritePlateItem extends AttachmentItem {

    private static final ResourceLocation ARMOR_ID =
            ProtectionEngineering.asResource("netherite_plate_armor");
    private static final ResourceLocation TEX =
            ProtectionEngineering.asResource("textures/models/armor/netherite_plate.png");

    public NetheritePlateItem(Properties properties) {
        super(properties, SlotTypes.CHESTPLATE);
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.netherite_plate";
    }

    @Override
    public float getDamageReduction() { return 0.15f; }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ARMOR,
                new AttributeModifier(ARMOR_ID, 4.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.CHEST);
    }

    // ── 3D 渲染 ──────────────────────────────────────────────

    @Override
    public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
        return new NetheritePlateAttachmentModel<>(
                modelSet.bakeLayer(NetheritePlateAttachmentModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getAttachmentTexture() {
        return TEX;
    }
}
