package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

/**
 * 弹跳助力膝 — 膝部附件，跳跃高度 +0.5。
 */
public class SpringyKneecapItem extends AttachmentItem {

    public SpringyKneecapItem(Properties properties) {
        super(properties, SlotTypes.KNEE);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(IAttachment.bonus("springy_kneecap_jump", Attributes.JUMP_STRENGTH, 0.06,
                AttributeModifier.Operation.ADD_VALUE));
    }
}
