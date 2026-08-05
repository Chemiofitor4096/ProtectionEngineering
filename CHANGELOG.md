# Protection Engineering 更新日志

## [1.5.0]

### 新增

- **属性声明机制 (`IAttachment.AttributeBonus`)** — 附件只需覆写 `getAttributeBonuses()` 声明属性修饰符，基类 `AttachmentItem` 统一应用到宿主护甲（`ItemAttributeModifierEvent`）并在 tooltip 底部以原版风格展示「当作为部件安装时：+X 属性」。声明一次，两处复用。
- **装备槽组自动推导** — 附件声明属性时不再手写 `EquipmentSlotGroup`，由 `SlotType.equipmentSlotGroup()` 按兼容槽位映射，`AttachmentItem` 按宿主护甲的实际部件动态选择（通用槽附件装胸甲→CHEST、护腿→LEGS 等）。装备槽组映射存 `SlotType` 旁路表（不参与 record equals/hashCode）。
- **防爆内衬改用 `DamageTypeTags.IS_EXPLOSION`** — 覆盖范围从原先枚举的 2 种（explosion、player_explosion）扩展到与**原版爆炸保护附魔同源的 4 种**（+fireworks、+bad_respawn_point），模组追加的爆炸类型自动生效。
- **SchoolLiningItem（学派内衬）属性接入** — 法力上限 +50，参与「当作为部件安装时」tooltip 显示。修饰符 ID 按物品注册名派生 + 基类追加宿主部件后缀，跨护甲、跨学派均可堆叠。
- **PlayerAnimator 兼容 (`PlayerAnimCompat`)** — 护甲层在渲染上半身前补上 body 弯曲变换（`IBendHelper.rotateMatrixStack`），护甲在弯曲动画下跟随身体不再乱飘；`rotateAtPart` 数学变换重写为 `T(动画后枢轴)·R·T(-原始枢轴)`，确保 PlayerAnimator 位置关键帧（pivot 移动）下护甲精确贴合。
- **`build.gradle`** — playeranimator 改为 `compileOnly` + `runtimeOnly`；可选兼容依赖（Iron's Spells、detail-armor-bar）统一为此模式，不传播给下游消费者。

### 优化

- **Tooltip 护甲值合并** — `PENeoForgeEvents` 新增 `mergeArmorModifiers`，将同槽位护甲默认值 + 附件附加护甲值（`ARMOR`/`ARMOR_TOUGHNESS`）合并为一条总和显示，避免两行「+X 护甲」。不影响数值，仅展示归一。
- **属性 Tooltip 格式对齐原版** — 用原版 key `attribute.modifier.plus.X` 渲染，数值与属性名作为两个占位参数传入，彻底解决自定义属性显示 `+%s %sXXX` 的双占位符未替换问题。
- **Feature 文本精简** — 纯属性附件（机械臂、拳套、轻型外骨骼、弹跳膝）删除 Feature 描述，属性改由「当作为部件安装时」区块展示；含非属性效果的附件（防护板、外骨骼）精简为纯效果描述。
- **示例代码模型重构** — `build.gradle` 清理空 `repositories` 块、修正 sourceSet exclude 路径格式、更新依赖注释。

### 修复

- **同种内衬跨护甲法力不堆叠** — SchoolLiningItem 修饰符 ID 由静态常量改为按物品注册名派生，基类再追加宿主部件后缀（`xxx_chest`/`xxx_legs`…），同一附件装到不同护甲部件时 ID 唯一，可跨护甲堆叠（含同种附件）。
- **数据库源代码集缓存排除无效** — `exclude("src/generated/**/.cache")` 路径格式错误，修复为 `exclude("**/.cache")`。
