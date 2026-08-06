package com.chemiofitor.protection_engineering.item;

import com.chemiofitor.protection_engineering.api.AttachmentUtil;
import com.chemiofitor.protection_engineering.api.SlotTypes;
import com.chemiofitor.protection_engineering.registry.PEItems;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * 工程师眼镜 —— 眼部附件，安装在兜帽的眼部槽位。
 * 集成 Create 护目镜系统：装备时显示方块信息、红石信号、视野缩放。
 */
public class EngineerGogglesItem extends AttachmentItem {

    public EngineerGogglesItem(Properties properties) {
        super(properties, Set.of(MobEffects.BLINDNESS), SlotTypes.EYES);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    @Nullable
    public String getFeatureKey() {
        return "tooltip.protectionengineering.feature.engineer_goggles";
    }

    // ── Create 护目镜集成 ────────────────────────────────────

    /** 注册护目镜谓词：玩家头盔槽装备兜帽且眼部附件为本物品时生效 */
    public static void registerGogglesPredicate() {
        GogglesItem.addIsWearingPredicate(EngineerGogglesItem::hasGogglesEquipped);
    }

    private static boolean hasGogglesEquipped(Player player) {
        return AttachmentUtil.get(player, EquipmentSlot.HEAD, SlotTypes.EYES).getItem()
                == PEItems.ENGINEER_GOGGLES.get();
    }
}
