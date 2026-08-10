package com.chemiofitor.protection_engineering.registry;

/**
 * 附件状态存储 —— 1.20.1 无 Data Component 系统，全部改用 ItemStack NBT。
 * <p>
 * 状态键直接写入附件 ItemStack 自身的 tag：
 * <ul>
 *   <li>{@code attachment_state}    Integer — 统一状态 (0=DISABLED, 1=READY, 2=ACTIVE, 3=COOLING)</li>
 *   <li>{@code attachment_cooldown} Long — 当前阶段结束的 tick（0 = 无计时）</li>
 *   <li>{@code attachment_active}   Boolean — 已废弃，仅用于旧存档迁移，永不写入</li>
 * </ul>
 * 宿主护甲的 tag 内以 {@code attachments} 键存附件表（见 {@link com.chemiofitor.protection_engineering.api.AttachmentsData}）。
 */
public final class PEDataComponents {

    private PEDataComponents() {}

    /** 宿主护甲 tag 中附件表的键 */
    public static final String ATTACHMENTS = "attachments";

    /** 附件状态机的键 */
    public static final String ATTACHMENT_STATE = "attachment_state";

    /** 附件冷却结束 tick 的键 */
    public static final String ATTACHMENT_COOLDOWN = "attachment_cooldown";

    /** 已废弃 —— 旧存档迁移用 */
    public static final String ATTACHMENT_ACTIVE = "attachment_active";
}
