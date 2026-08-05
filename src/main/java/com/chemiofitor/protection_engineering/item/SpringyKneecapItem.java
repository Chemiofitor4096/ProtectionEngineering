package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.ProtectionEngineering;
import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * 弹跳助力膝 — 膝部附件，跳跃高度 +0.5。
 */
public class SpringyKneecapItem extends AttachmentItem {

    private static final ResourceLocation JUMP_ID =
            ProtectionEngineering.asResource("springy_kneecap_jump");

    public SpringyKneecapItem(Properties properties) {
        super(properties, SlotTypes.KNEE);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(new IAttachment.AttributeBonus(JUMP_ID, Attributes.JUMP_STRENGTH, 0.06,
                AttributeModifier.Operation.ADD_VALUE));
    }
}
