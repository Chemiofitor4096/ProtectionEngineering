package com.chemiofitor.protection_engineering.api;

import net.minecraft.world.item.ItemStack;

import java.util.Set;

/**
 * 可安装附件的宿主物品接口。
 * <p>
 * 宿主物品（护甲、武器、工具）实现此接口来声明：
 * <ul>
 *   <li>它支持哪些附件槽位</li>
 *   <li>如何读写附件的持久化数据</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>{@code
 * public class EngineerChestplateItem extends ArmorItem implements IAttachmentHost {
 *     private static final Set<SlotType> SLOTS = Set.of(
 *         SlotTypes.SHOULDER, SlotTypes.CHESTPLATE,
 *         SlotTypes.BACK, SlotTypes.ARM
 *     );
 *
 *     public Set<SlotType> supportedSlots() { return SLOTS; }
 *
 *     public AttachmentsData getAttachments(ItemStack host) {
 *         return host.getOrDefault(ModDataComponents.ATTACHMENTS, AttachmentsData.EMPTY);
 *     }
 *
 *     public void setAttachments(ItemStack host, AttachmentsData data) {
 *         host.set(ModDataComponents.ATTACHMENTS, data);
 *     }
 * }
 * }</pre>
 */
public interface IAttachmentHost {

    /** 该宿主物品支持的附件槽位集合 */
    Set<SlotType> supportedSlots();

    /** 读取该宿主物品已安装的附件数据 */
    AttachmentsData getAttachments(ItemStack host);

    /** 写入该宿主物品的附件数据 */
    void setAttachments(ItemStack host, AttachmentsData data);

    // ── 默认方法 ────────────────────────────────────────────────

    /** 将附件安装到指定槽位 */
    default void install(ItemStack host, SlotType slot, ItemStack attachment) {
        setAttachments(host, getAttachments(host).with(slot, attachment));
    }

    /** 从指定槽位卸下附件，返回卸下的 ItemStack */
    default ItemStack remove(ItemStack host, SlotType slot) {
        var data = getAttachments(host);
        var removed = data.get(slot);
        setAttachments(host, data.without(slot));
        return removed;
    }

    /** 检查给定槽位是否可安装指定附件（默认只检查槽位是否被宿主支持） */
    default boolean canInstall(SlotType slot, ItemStack attachment) {
        return supportedSlots().contains(slot)
                && attachment.getItem() instanceof IAttachment att
                && att.compatibleSlots().contains(slot);
    }
}
