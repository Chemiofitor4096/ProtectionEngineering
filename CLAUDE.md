# Protection Engineering - 开发指南

Minecraft NeoForge 1.21.1 模组。工程师护甲系统 — 护甲可安装附件，附件提供被动免疫/主动技能。

## 项目结构

```
src/main/java/com/chemiofitor/protection_engineering/
├── ProtectionEngineering.java       # 主类 (MODID, Registrate, 构造器组装)
├── api/                             # 接口层
│   ├── IAttachment.java             # 附件接口 (ControlPattern + state 常量 + 生命周期钩子 + AttributeBonus 属性声明 + bonus() 工厂)
│   ├── IAttachmentHost.java         # 宿主接口 (护甲实现)
│   ├── AttachmentsData.java         # 附件数据组件 (codec/stream codec/值比较)
│   ├── AttachmentUtil.java          # 附件遍历/查找/归约工具 (PEGameEvents/PEKeyBindings/HUD 共用)
│   ├── SlotType.java                # 槽位类型 record (含注册表 + 装备槽组旁路映射)
│   ├── SlotTypes.java               # 内置槽位常量 (9 专属 + LINING + 4 装饰 + 3 武器预留)
│   └── IGradedRepair.java           # 分级修补接口 (材料→强度, 20 单位=满耐久)
├── item/                            # 物品类 (核心 37 个: 4 护甲 + 30 附件 + 导弹/锯剑/盾)
│   ├── AttachmentItem.java          # 附件抽象基类 ★ 统一状态机 + 属性声明统一应用
│   ├── AttachmentHostArmorItem.java # 护甲基类 (IAttachmentHost + IGradedRepair, 不可附魔)
│   ├── EngineerHoodItem.java        # 兜帽 (EYES, MOUTH, LINING, HELMET_DECORATION)
│   ├── EngineerChestplateItem.java  # 胸甲 (SHOULDER, CHESTPLATE, BACK, ARM, LINING, CHESTPLATE_DECORATION) 可鞘翅
│   ├── EngineerLeggingsItem.java    # 护腿 (LEG, KNEE, LINING, LEGGINGS_DECORATION)
│   ├── EngineerBootsItem.java       # 靴子 (FOOT, LINING, BOOTS_DECORATION) 改良鞋底可细雪行走
│   ├── NightVisionGogglesItem.java  # 夜视眼镜 (EYES, FREE_TOGGLE, N 键)
│   ├── SpyglassItem.java            # 单筒望远镜 (EYES, FREE_TOGGLE, Z 键)
│   ├── EngineerGogglesItem.java     # 工程师眼镜 (EYES, Create 护目镜集成)
│   ├── HormoneInjectorItem.java     # 激素针 (EYES, ONE_SHOT_COOLDOWN, H 键)
│   ├── AirFilterItem.java           # 空气过滤器 (MOUTH, 免疫中毒/反胃/虚弱/挖掘疲劳)
│   ├── DivingDeviceItem.java        # 潜水设备 (MOUTH, 永久水下呼吸 II)
│   ├── ApsItem.java                 # 主动防御系统 (SHOULDER, ACTIVE_COOLDOWN, K 键)
│   ├── AdvancedApsItem.java         # 高级主动防御系统 (SHOULDER, 1.5× 激活时长)
│   ├── RocketLauncherItem.java      # 火箭发射器 (SHOULDER, ONE_SHOT_COOLDOWN, 霰弹, R 键)
│   ├── SturdyPlateItem.java         # 坚固防护板 (CHESTPLATE, 护甲+2, 减伤10%)
│   ├── NetheritePlateItem.java      # 下界合金防护板 (CHESTPLATE, 护甲+4, 减伤15%)
│   ├── PurityMarkItem.java          # 纯洁印记 (CHESTPLATE_DECORATION, 纯装饰)
│   ├── JetpackItem.java             # 喷气背包 (BACK, ALWAYS_ON, 鞘翅, J 键)
│   ├── MomentumJetpackItem.java     # 动量背包 (BACK, 继承 JetpackItem, 速度+20%)
│   ├── DodgeJetpackItem.java        # 应激反馈背包 (BACK, 事件触发 ONE_SHOT)
│   ├── MissilePackItem.java         # 导弹背包 (BACK, ONE_SHOT_COOLDOWN, G 键)
│   ├── MissileItem.java             # 制导导弹 (消耗品, 堆叠16)
│   ├── ExtraMechanicalArmItem.java  # 额外机械臂 (ARM, 触及+2)
│   ├── MechaKnuckleItem.java        # 机械拳套 (ARM, 近战+20%)
│   ├── LightExoskeletonItem.java    # 轻型外骨骼 (LEG, 速度+10%, 跳跃+0.5, 跨1格)
│   ├── HeavyExoskeletonItem.java    # 重型外骨骼 (LEG, 护甲+2, 跨1格, 摔落-20%)
│   ├── SpringyKneecapItem.java      # 弹跳助力膝 (KNEE, 跳跃+0.5)
│   ├── CushionedKneecapItem.java    # 缓冲护膝 (KNEE, 摔落-10%)
│   ├── LiningItem.java              # 通用内衬 (LINING, 参数化标签 — 防爆 IS_EXPLOSION / 防火 IS_FIRE)
│   ├── ImprovedSolesItem.java       # 改良鞋底 (FOOT, 防滑/免疫粘液/细雪行走)
│   ├── CushionedSolesItem.java      # 缓冲鞋底 (FOOT, 摔落-20%, 高度-1)
│   ├── FrostSolesItem.java          # 冰霜鞋底 (FOOT, 水上冻结为霜冰)
│   ├── InsulatedSolesItem.java      # 隔热鞋底 (FOOT, 免疫岩浆块伤害)
│   ├── SilentSolesItem.java         # 静音鞋底 (FOOT, 屏蔽 sculk 振动)
│   ├── DivingSolesItem.java         # 潜水配重鞋底 (FOOT, Create 潜水靴兼容)
│   ├── EngineerShieldItem.java      # 工程师盾牌 (3×耐久, 斧破防冷却减半, 防护+20°)
│   └── EngineerSawSwordItem.java    # 工程师链锯剑 (钻石伤害/下界合金耐久, 斧头动作)
├── entity/                          # 自定义实体
│   ├── MissileEntity.java           # 制导导弹 (导向+粒子+音效)
│   ├── RocketProjectile.java        # 火箭射弹 (直线+爆炸)
│   └── ThrustEntity.java            # 推力实体 (不可见, 挂载助推)
├── registry/
│   ├── PEDataComponents.java        # 数据组件 (ATTACHMENTS, STATE, COOLDOWN; ACTIVE 已废弃)
│   ├── PEArmorMaterials.java        # 工程师护甲材质 (下界合金级 3/8/6/3, 耐久因子 56)
│   ├── PERepairMaterials.java       # 分级修补材料表 (黄铜板 3 / 坚固板 10 单位)
│   ├── PEItems.java                 # 物品注册 (Registrate, 37 个)
│   ├── PEEntities.java              # 实体类型注册 (thrust/rocket_projectile/missile)
│   ├── PESounds.java                # 音效事件注册 (16 个)
│   ├── PEWorkbench.java             # 改装台方块 + BE + Menu 注册
│   ├── PEArmorEmitter.java          # 护甲发射器方块注册
│   └── PEArmInteractionPointTypes.java  # Create 机械臂交互点 (护甲发射器)
├── compat/
│   ├── DetailArmorBarCompat.java    # 细节护甲条 HUD 兼容
│   ├── PlayerAnimCompat.java        # PlayerAnimator 弯曲动画兼容（护甲层同步 body bend）
│   └── iron/                        # Iron's Spells 'n Spellbooks 兼容（守卫加载）
│       ├── IronCompat.java          # 加载守卫
│       ├── IronCompatItems.java     # 9 个学派内衬条件注册
│       └── SchoolLiningItem.java    # 学派内衬 (魔法减伤 10% + 最大法力 +50)
├── config/
│   ├── PEConfig.java                # 客户端配置 (HUD 位置)
│   └── PEServerConfig.java          # 服务端配置 (APS/导弹/闪避/激素/火箭参数)
├── event/
│   ├── PEGameEvents.java            # 伤害/摔落/效果免疫/静音鞋底/APS/应激背包 (LivingIncomingDamage 等)
│   ├── PEAnvilEvents.java           # 铁砧分级修补
│   ├── PENeoForgeEvents.java        # 属性修饰符 (ItemAttributeModifierEvent + 护甲值合并) + 装备变更钩子
│   └── PENetworkEvents.java         # 网络包处理 (附件开关+喷气推进)
├── network/
│   ├── ToggleAttachmentPayload.java # 附件开关包 (armorIndex + slotTypeId)
│   └── ThrustJetpackPayload.java    # 喷气推进包 (无字段)
├── client/
│   ├── PEKeyBindings.java           # 热键 (N/H/J/K/R/G/Z)
│   ├── PEClientExtensions.java      # 隐藏原版贴片护甲模型
│   ├── PEAttachmentModelSetup.java  # 附件→3D 模型映射集中注册 (client-only)
│   ├── PEAttachmentModelRegistry.java  # 附件模型注册表 (main/arm/leg)
│   ├── PEModelLayers.java           # 模型层定义注册 + PEArmorLayer 注入 (玩家/盔甲架/人形生物)
│   ├── PEEntityRenderers.java       # 实体渲染器注册
│   ├── layer/PEArmorLayer.java      # 护甲+附件渲染 (含对称肢体镜像/PlayerAnimator 兼容)
│   ├── PEHormoneOverlay.java        # 激素针 HUD+FOV+望远镜遮罩
│   ├── PECooldownOverlay.java       # 统一状态 HUD (●/⚡/⌛)
│   ├── renderer/                    # 实体渲染器 (Missile/RocketProjectile)
│   └── model/                       # Blockbench 导出模型 (33 个)
├── mixin/
│   ├── LivingEntityMixin.java       # 改良鞋底防滑 (getBlockSpeedFactor)
│   ├── ShieldAngleMixin.java        # 工程师盾牌防护范围 +20% (isDamageSourceBlocked)
│   ├── PlayerShieldMixin.java       # 工程师盾牌斧破防冷却减半 (disableShield)
│   └── DivingBootsMixin.java        # 潜水配重鞋底 Create 潜水靴兼容 (getWornItem)
├── block/                           # 改装台 + 护甲发射器方块
├── menu/                            # 改装台 GUI (护甲槽 1 + 附件槽 4, 翻页)
└── data/
    ├── PEDataGen.java               # 语言文件生成入口 (en_us/en_ud)
    ├── PERecipeProvider.java        # 工作台/锻造台配方 (护甲/武器/盾/导弹/印记/工作台)
    └── PEMechanicalCraftingRecipeGen.java  # 动力合成配方 (28 基础 + 9 ISS 条件配方)
```

## 数据组件 (`PEDataComponents`)

| 组件 | 类型 | 用途 |
|------|------|------|
| `ATTACHMENTS` | `AttachmentsData` | 护甲上的附件列表 |
| `ATTACHMENT_STATE` | `Integer` | **统一状态** 0=DISABLED, 1=READY, 2=ACTIVE, 3=COOLING |
| `ATTACHMENT_COOLDOWN` | `Long` | 当前阶段结束 tick (0=无计时器) |
| `ATTACHMENT_ACTIVE` | `Boolean` | ⚠️ 已废弃，仅用于旧存档迁移 |

## 统一状态机

### 控制模式 (`ControlPattern`)

| 模式 | 状态流转 | 典型附件 |
|------|---------|---------|
| `FREE_TOGGLE` | DISABLED ↔ READY | 夜视眼镜, 单筒望远镜 |
| `ACTIVE_COOLDOWN` | READY → ACTIVE → COOLING → READY | APS, 高级APS |
| `ONE_SHOT_COOLDOWN` | READY → COOLING → READY | 激素针, 导弹, 火箭, 应激背包 |
| `ALWAYS_ON` | 始终 READY | 喷气背包, 动量背包 |
| `PASSIVE` | 无状态 | 防护板, 外骨骼等 |

### HUD 显示规则

| 状态 | 符号 | 生存颜色 | 创造颜色 |
|------|------|------|------|
| READY | ● | 绿 `#55FF55` | 紫 `#FF55FF` |
| ACTIVE | ⚡ + 剩余时间 | 青 `#55FFFF` | 紫 |
| COOLING | ⌛ + 剩余时间 | 灰→黄→红 | 紫 |
| DISABLED | ○ | 灰 | 紫 |

### 创建新附件 (按模式)

```java
// FREE_TOGGLE — 自由开关
public class XItem extends AttachmentItem {
    public XItem(Properties p) { super(p, SlotTypes.EYES); }
    @Override public ControlPattern getControlPattern() { return ControlPattern.FREE_TOGGLE; }
    @Override protected void onStateEnter(ItemStack s, int state, LivingEntity e) {
        if (state == STATE_READY) { /* 开启时 */ }
    }
    @Override protected void onStateExit(ItemStack s, int state, LivingEntity e) {
        if (state == STATE_READY) { /* 关闭时 */ }
    }
}

// ACTIVE_COOLDOWN — 激活窗口+冷却
public class XItem extends AttachmentItem {
    @Override public ControlPattern getControlPattern() { return ControlPattern.ACTIVE_COOLDOWN; }
    @Override public long getActiveDuration() { return 200; }
    @Override public long getCooldownDuration() { return 600; }
    @Override public void onTick(ItemStack a, ItemStack h, LivingEntity e, SlotType s) {
        super.onTick(a, h, e, s);
        if (!isInActiveWindow(a)) return;
        // 仅激活窗口内执行
    }
}

// ONE_SHOT_COOLDOWN — 一次性触发+冷却
public class XItem extends AttachmentItem {
    @Override public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }
    @Override public long getCooldownDuration() { return 1200; }
    @Override protected void onActivateOnce(ItemStack s, ItemStack h, LivingEntity e) {
        // 一次性效果
    }
}
```

### 状态机钩子

| 钩子 | 用途 |
|------|------|
| `onActivateOnce(stack, host, entity)` | ONE_SHOT 触发时 |
| `onStateEnter(stack, newState, entity)` | 进入状态时 (播音效) |
| `onStateExit(stack, oldState, entity)` | 离开状态时 (清除效果) |

**不要**覆写 `onEquip` / `onActivatePress` / `onTick` 做状态管理 — 基类 `AttachmentItem` 已统一处理。必须覆写 `onTick` 时先调 `super.onTick(...)`。

## 属性声明机制 (`AttributeBonus`)

带属性的附件覆写 `getAttributeBonuses()` 声明修饰符，`AttachmentItem` 统一两处应用：

```java
@Override
public List<IAttachment.AttributeBonus> getAttributeBonuses() {
    return List.of(new IAttachment.AttributeBonus(
            ProtectionEngineering.asResource("xxx_bonus"),
            Attributes.ARMOR, 4.0,
            AttributeModifier.Operation.ADD_VALUE));   // 装备槽组自动推导，无需手写
}
```

- **生效**：`addAttributeModifiers`（`ItemAttributeModifierEvent`）遍历声明 `replaceModifier`
- **展示**：附件 tooltip 底部"当作为部件安装时：+X 属性"（原版格式，数值与属性名作为占位参数传入 `attribute.modifier.plus.*`）
- **装备槽组**：按宿主护甲的实际部件动态选择（通用槽附件装到胸甲→CHEST、护腿→LEGS 等，精确跟随安装位置）；非护甲宿主回退槽位映射
- **修饰符 ID**：应用时追加宿主部件后缀（`xxx_chest` / `xxx_legs`…），同一附件装到不同护甲部件时 ID 唯一 → 可跨护甲堆叠（含同种附件）；若附件自身需区分不同来源，基础 ID 用注册名派生（如学派内衬）

已接入：坚固/下界合金板（护甲）、外骨骼（护甲/跨越）、机械臂（触及）、机械拳套（近战）、弹跳膝（跳跃）、学校内衬（最大法力）。

## 槽位类型 (`SlotTypes`)

| 护甲 | 槽位 (各1个) | 每件小计 |
|------|------|------|
| 兜帽 | `EYES`, `MOUTH`, `LINING`, `HELMET_DECORATION` | 4 |
| 胸甲 | `SHOULDER`, `CHESTPLATE`, `BACK`, `ARM`, `LINING`, `CHESTPLATE_DECORATION` | 6 |
| 护腿 | `LEG`, `KNEE`, `LINING`, `LEGGINGS_DECORATION` | 4 |
| 靴子 | `FOOT`, `LINING`, `BOOTS_DECORATION` | 3 |
| 武器(预留) | `BLADE`, `HILT`, `GUARD` | — |

全套装共 **17 个附件槽位**（9 专属 + 4 LINING + 4 装饰）；武器 3 槽为预留。

每个槽位映射一个 `EquipmentSlotGroup`（附件属性修饰符的生效范围，见 `SlotType.equipmentSlotGroup()`）：

| 槽位 | 装备槽组 |
|------|---------|
| `EYES` / `MOUTH` | `HEAD` |
| `SHOULDER` / `CHESTPLATE` / `BACK` / `ARM` | `CHEST` |
| `LEG` / `KNEE` | `LEGS` |
| `FOOT` | `FEET` |
| `LINING` / `*_DECORATION` | `ANY` |
| `BLADE` / `HILT` / `GUARD` | `HAND` |

附件声明属性时**不再手写装备槽组** —— `AttachmentItem` 动态决定：
- **护甲宿主**：按 `ArmorItem.getEquipmentSlot()` 跟随实际部件（通用槽附件装胸甲→`CHEST`、护腿→`LEGS`…），同一附件装到不同护甲时属性跟随迁移
- **非护甲宿主**（武器附件预留）：回退到上表的静态映射（单组 → 该组；跨组 → `ANY`）

映射存 `SlotType` 旁路表（不参与 record equals/hashCode，避免 `byId` 反序列化实例失配）。

## 改装台 (`PEWorkbench`)

- 方块 `workbench`（强度 2.0，需正确工具，金属音效）+ BE（仅创建菜单，不存物品）
- GUI：1 个护甲槽 + 4 个附件槽，按 `supportedSlots()` **翻页**展示（胸甲 6 槽分两页）
- 槽位兼容判定：`IAttachmentHost.canInstall(slot, attachment)` → 宿主支持 + `attachment.compatibleSlots().contains(slot)`
- 安装/拆卸在关闭菜单时写入护甲数据组件；播放 `attach_1` / `attach_2` 音效

## 护甲发射器 (`PEArmorEmitter`)

- 方块 `armor_emitter`（压力板式，半透明）：有可穿戴装备的生物站在上方时，Create **机械臂**可把它当交互点
- 自动穿戴：护甲 → 对应装备槽；本模组附件 → 第一个可装的空槽位；不能穿戴时 `insert` 原样返回（机械臂跳过）
- Shift + 右键切换模式：`EQUIP` 脱装备（保留附件）/ `ATTACH` 拆附件 / `BOTH` 全部移除；机械臂 TAKE 按模式从头到脚提取
- 交互点类型注册于 `PEArmInteractionPointTypes`（Create `ArmInteractionPointType`），主类已 `register(modEventBus)`

## 配置系统 (`PEServerConfig`)

生成到 `protectionengineering-server.toml`，运行时修改即时生效。

| 分类 | 配置项 | 默认 | 范围 |
|------|------|------|------|
| aps | `interceptRange` | 5.0 | 1-32 |
| | `activeDuration` | 200 | 20-1200 |
| | `cooldownTicks` | 600 | 20-3600 |
| missile | `targetRange` | 512 | 20-1024 |
| | `flightSpeed` | 3.0 | 1-10 |
| | `closeSpeed` | 4.5 | 1-15 |
| | `maxLife` | 200 | 40-600 |
| rocket | `cooldownTicks` | 200 | 20-3600 |
| hormone | `cooldownTicks` | 1200 | 20-7200 |
| dodge | `dodgeStrength` | 1.0 | 0.2-10 |
| | `cooldownTicks` | 200 | 20-3600 |
| repair | `brassSheetUnits` | 3 | 0-20 |
| | `sturdySheetUnits` | 10 | 0-20 |

## 分级修补系统

铁砧按材料等级恢复不同耐久（原版固定 25%，无法分级）。
标度：**20 单位 = 满耐久**（1 单位 = 5%），原版铁锭等效 5 单位。

| 组件 | 作用 |
|------|------|
| `api/IGradedRepair.java` | 物品接口，`getRepairUnits(toRepair, material)` 返回强度；`appendRepairTooltip` 共用 tooltip |
| `registry/PERepairMaterials.java` | 材料→强度表（延迟解析 + 配置驱动），`asIngredient()` 供 ArmorMaterial |
| `event/PEAnvilEvents.java` | `AnvilUpdateEvent` 接管铁砧结果计算 |

已接入：4 件工程师护甲（`AttachmentHostArmorItem`）、工程师盾牌、工程师链锯剑。
三者**只**接受分级材料 —— 盾牌不吃木板，链锯剑不吃钻石
（`EngineerSawSwordItem.TIER.getRepairIngredient()` 也同步改为分级材料）。

新增材料：`PERepairMaterials.register(() -> AllItems.XXX, units)`。
让新物品支持分级修补：实现 `IGradedRepair` + 覆写 `isValidRepairItem` + 在 `appendHoverText`
调 `appendRepairTooltip(stack, tooltip)`。未登记的材料组合不写 output，回退原版逻辑。

前置工作惩罚（`REPAIR_COST` 递增）由 `increasesRepairCost` 控制，默认跟随
`Item#isEnchantable`：链锯剑递增（防无限附魔叠加），护甲/盾牌不递增
（不可附魔，递增只会让装备修几次后彻底无法修复）。

Tooltip key：`tooltip.protectionengineering.repair_materials`，表头 + 每材料一行
（缩进两空格，与附件清单一致）：

```
修补：
  黄铜板 15%
  坚固板 50%
```

比例由配置实时换算。服务端配置未就绪（主菜单）或该物品不接受任何已登记材料时，
连表头一起不显示。

## 热键

| 键 | 常量 | 目标 |
|------|------|------|
| N | `TOGGLE_NIGHT_VISION` | NightVisionGogglesItem |
| H | `ACTIVATE_HORMONE` | HormoneInjectorItem |
| J | `THRUST_JETPACK` | JetpackItem / MomentumJetpackItem |
| K | `TOGGLE_APS` | ApsItem |
| R | `ACTIVATE_ROCKET_LAUNCHER` | RocketLauncherItem |
| G | `ACTIVATE_MISSILE` | MissilePackItem |
| Z | `TOGGLE_SPYGLASS` | SpyglassItem |

添加热键：在 `PEKeyBindings` 加 KeyMapping + `registerKeys` + `onClientTick` 中 `tryActivateAttachment(YourClass.class)`。

## 减伤系统

无上限叠加，最终 clamp 到 100%（防负伤害）。
乘性叠加：`LivingIncomingDamageEvent` 在 `hurt` 最早阶段触发，优先于原版护甲 / 抗性提升 / 附魔保护结算
（模组减伤 10% + 抗性提升 I 级 20% = 总减免 28%，非加性）。

| 位置 | 事件 | 类型 |
|------|------|------|
| `PEGameEvents.onLivingIncomingDamage` | `LivingIncomingDamageEvent` 通用减伤 | `getDamageReduction()`（受 protected types/tags 过滤） |
| `PEGameEvents.onLivingFall` | 摔落距离减免 | `getFallDistanceReduction()` |
| `PEGameEvents.onLivingFall` | 摔落伤害倍率 | `getFallDamageReduction()` |

伤害类型过滤（`IAttachment`）：
- `getProtectedDamageTypes()`：枚举具体 `ResourceKey<DamageType>`
- `getProtectedDamageTypeTags()`：`TagKey<DamageType>` 标签 —— 推荐，覆盖同源全部类型并兼容模组追加
- 防爆/防火内衬用原版 `DamageTypeTags`（`IS_EXPLOSION` / `IS_FIRE`），与原版对应保护附魔判断同源

## 配方系统

| 生成器 | 配方类型 | 文件 |
|------|------|------|
| `PERecipeProvider` | 工作台 (Shaped) + 锻造台 (SmithingTransform) | `data/PERecipeProvider.java` |
| `PEMechanicalCraftingRecipeGen` | Create 动力合成 (MechanicalCrafting) | `data/PEMechanicalCraftingRecipeGen.java` |

护甲四件 / 链锯剑 / 盾牌 / 导弹(×4) / 纯洁印记 / 改装台走工作台配方；下界合金防护板是锻造台升级（坚固板 + 下界合金锭）。其余附件走动力合成（共 28 条，另有 9 条 Iron's Spells 学派内衬配方 `whenModLoaded("irons_spellbooks")` 守卫）。

Create 物品引用：`AllItems.STURDY_SHEET`, `AllItems.BRASS_SHEET`, `AllItems.IRON_SHEET`, `AllItems.COPPER_SHEET`, `AllItems.PRECISION_MECHANISM`, `AllItems.POWDERED_OBSIDIAN`, `AllItems.ELECTRON_TUBE`, `AllItems.GOGGLES`, `AllItems.ZINC_INGOT`, `AllItems.ANDESITE_ALLOY`, `AllItems.CARDBOARD`, `AllBlocks.MECHANICAL_ARM`, `AllBlocks.DISPLAY_BOARD`, `AllBlocks.DEPOT`, `AllBlocks.BRASS_CASING`, `AllBlocks.SHAFT`, `AllBlocks.ENCASED_FAN`, `AllBlocks.STEAM_WHISTLE`, `AllBlocks.REDSTONE_CONTACT`, `AllPaletteBlocks.FRAMED_GLASS_PANE`

## 翻译 key 规范

- 物品: `item.protectionengineering.<name>`
- 槽位: `slot.protectionengineering.<name>`
- Tooltip: `tooltip.protectionengineering.<name>`
- 功能: `tooltip.protectionengineering.feature.<name>`
- 消息: `message.protectionengineering.<name>`
- 按键: `key.protectionengineering.<name>`
- 字幕: `subtitles.protectionengineering.<name>`
- HUD: `hud.protectionengineering.<name>`

中文翻译手动维护在 `src/main/resources/assets/protectionengineering/lang/zh_cn.json`。
英文翻译通过 `PEDataGen.java` 的 `ProviderType.LANG` 生成。运行 `runData` 更新。

## Mixin

| Mixin | 目标 | 用途 |
|-------|------|------|
| `LivingEntityMixin` | `LivingEntity` | `getBlockSpeedFactor` 改良鞋底防滑（通用减伤在 `PEGameEvents`，不在 mixin） |
| `ShieldAngleMixin` | `LivingEntity` | `isDamageSourceBlocked` 工程师盾牌防护范围 180°→216° |
| `PlayerShieldMixin` | `Player` | `disableShield` 工程师盾牌斧破防禁用 100→50 tick |
| `DivingBootsMixin` | Create `DivingBootsItem` | `getWornItem` 潜水配重鞋底被识别为潜水靴 |

> ⚠️ 新增 mixin 必须同时登记两处（否则静默不加载）：`src/main/resources/protectionengineering.mixins.json` 的 `mixins` 列表 + `src/main/templates/META-INF/neoforge.mods.toml` 的 `[[mixins]]` 块。

## 事件系统

| 处理器 | 事件 | 用途 |
|--------|------|------|
| `PEGameEvents` | `LivingIncomingDamageEvent` | 应激反馈背包 (近战) / APS 拦截 (投射物) / 隔热鞋底 / 通用减伤 |
| `PEGameEvents` | `LivingFallEvent` | 摔落距离减免 + 伤害倍率减免 |
| `PEGameEvents` | `MobEffectEvent.Applicable` / `Added` | 状态效果免疫 |
| `PEGameEvents` | `VanillaGameEvent` | 静音鞋底屏蔽 sculk 振动 |
| `PENeoForgeEvents` | `ItemAttributeModifierEvent` | 附件属性修饰符（`getAttributeBonuses` 声明应用 + 护甲值/韧性合并） |
| `PENeoForgeEvents` | `LivingEquipmentChangeEvent` | 附件装配/卸下钩子（`onEquip` / `onUnequip`） |
| `PEAnvilEvents` | `AnvilUpdateEvent` | 铁砧分级修补接管 |
| `PENetworkEvents` | `RegisterPayloadHandlersEvent` | `toggle_attachment` / `thrust_jetpack` 两个 playToServer 包 |

## 文档

| 文档 | 路径 |
|------|------|
| 项目总览 | `doc/PROJECT_OVERVIEW.md` |
| API 指南 | `doc/API_GUIDE.md` |
| 毕业搭配 | `doc/BEST_LAYOUTS.md` |
