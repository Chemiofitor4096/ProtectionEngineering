# Protection Engineering 更新日志

## [1.6.0]

### 新增

- **护甲发射器 (`armor_emitter`)** — 压力板式功能方块，接受机械臂递送的护甲/附件并自动穿戴。碰撞箱 (0, -0.5, 0)~(16, 1.5, 16)，半透明渲染（`minecraft:translucent`）。Shift + 右键切换三种模式：脱装备（保留附件）/ 拆附件 / 全部移除（从头部到脚部依次脱下），机械臂 TAKE 按模式提取。
- **潜水配重鞋底 (`DivingSolesItem`)** — FOOT 槽附件，被 Create 识别为潜水靴：水下主动下沉（配重）+ 水平移动加速。
- **改良鞋底功能增强** — 防滑、免疫粘液/蜂蜜/灵魂沙减速、细雪行走。

### 优化

- **`AttachmentUtil` 全项目统一** — `PECooldownOverlay`、`PEHormoneOverlay`（两处）、`MissileEntity`、`PEGameEvents` APS 拦截块、`EngineerGogglesItem` 全部改用工具方法；`AttachmentUtil` 内部抽 `forAll` 消除 5 个方法各自的重复双层遍历。
- **`IAttachment.bonus()` 工厂** — 新增静态工厂方法，8 个附件类消灭 `X_ID` 字段 + 声明样板，每类从 ~15 行缩到 1-3 行。
- **`LiningItem` 参数化** — 防爆内衬（`BlastLiningItem`）与防火内衬（`FireLiningItem`）合并为 `LiningItem`，注册时传入标签（`IS_EXPLOSION`/`IS_FIRE`）和 feature key。
- **HormoneInjector 时长常量共享** — `EFFECT_DURATION` 从 `PEHormoneOverlay` 硬编码 200 改为引用 `HormoneInjectorItem.EFFECT_DURATION`。
- **改装甲台 `ATTACH_POS` 共享** — 从 `WorkbenchScreen` 和 `WorkbenchMenu` 各一份改为 `WorkbenchMenu` 单一定义。
- **`isFoil` → `isActive`** — 重复方法体合并。
- **`ApsItem` 脆弱的 `instanceof AdvancedApsItem`** → 子类覆写 `durationMultiplier()`。
- **消息系统落实** — 15 条孤儿 message key 全部接入实际逻辑（激素/闪避/APS/火箭/导弹的激活、冷却、就绪消息），`AttachmentItem` 新增 `sendMessage` / `cooldownSeconds` 统一辅助方法。action bar 挤占问题已修复（激活只发确认，冷却倒计时由 HUD 展示）。

### 修复

- **Mixin 配置从未加载** — `neoforge.mods.toml` 的 `config` 字段写错位置（在 `[[mods]]` 块而非 `[[mixins]]` 块），导致项目所有 mixin（`LivingEntityMixin`防滑、`PlayerShieldMixin`等）在 NeoForge 1.21 上从未生效。改为标准 `[[mixins]]` 块。
- **人形生物（僵尸/骷髅等）穿工程师套装不渲染** — `PEModelLayers.addLayers` 只在玩家和盔甲架上注册 `PEArmorLayer`，现遍历 `BuiltInRegistries.ENTITY_TYPE` 为所有 `HumanoidModel` 渲染器注入层。
- **`build.gradle`** — 空 `repositories` 块整合、sourceSet 缓存排除路径修正、可选兼容依赖统一 `compileOnly` + `runtimeOnly` 模式。

### 死代码清理

- 删除 `SimpleAttachmentItem`（无引用）
- 删除 `ApsItem` 未用 `advanced` 构造器
- 删除 `MissileEntity` 的 `shooterUUID` / `getTargetEntityUUID()` / `updateRotationFromVelocity()`（死字段/方法）
- 删除 `WorkbenchBlockEntity.SLOT_ATTACH`（未用）
- 删除 `WorkbenchBlock` 空覆写 `onRemove`
- 删除 `PEKeyBindings` 重复 import
- 删除 `PEDataGen` 孤儿 `slot...decoration` key

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
