package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.IAttachment;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.registry.PEAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

/**
 * 弹跳助力膝 — 膝部附件，跳跃高度 +0.5。
 * <p>
 * 1.20.1 无 vanilla 玩家跳跃属性（Attributes.JUMP_STRENGTH 是马的），
 * 用本模组注册的 jump_strength（默认 0.42 = 原版基准，mixin 消费）。
 */
public class SpringyKneecapItem extends AttachmentItem {

    public SpringyKneecapItem(Properties properties) {
        super(properties, SlotTypes.KNEE);
    }

    @Override
    public List<IAttachment.AttributeBonus> getAttributeBonuses() {
        return List.of(IAttachment.bonus("springy_kneecap_jump", PEAttributes.JUMP_STRENGTH.get(), 0.06,
                AttributeModifier.Operation.ADDITION));
    }
}
