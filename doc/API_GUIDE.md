# Protection Engineering API 指南

## 核心概念

**附件 (Attachment)** — 可安装到工程师护甲槽位上的物品，提供被动免疫、属性加成或主动技能。

**宿主 (Host)** — 实现了 `IAttachmentHost` 接口的护甲物品，承载附件数据。

**槽位 (Slot)** — 附件安装的具体位置。每件护甲有固定槽位集合，每个槽位只能装一个附件。

---

## 一、槽位类型

所有槽位定义在 `SlotTypes`，用 `ResourceLocation` 唯一标识。

| 常量 | ID | 所属护甲 |
|------|------|------|
| `EYES` | `protectionengineering:eyes` | 兜帽 |
| `MOUTH` | `protectionengineering:mouth` | 兜帽 |
| `SHOULDER` | `protectionengineering:shoulder` | 胸甲 |
| `CHESTPLATE` | `protectionengineering:chestplate` | 胸甲 |
| `BACK` | `protectionengineering:back` | 胸甲 |
| `ARM` | `protectionengineering:arm` | 胸甲 |
| `LEG` | `protectionengineering:leg` | 护腿 |
| `KNEE` | `protectionengineering:knee` | 护腿 |
| `FOOT` | `protectionengineering:foot` | 靴子 |

新增槽位：调用 `SlotType.register(ResourceLocation, SlotCategory)`。

---

## 二、创建附件

### 2.1 被动附件（仅免疫/属性）

直接使用 `SimpleAttachmentItem`：

```java
// 注册
public static final ItemEntry<SimpleAttachmentItem> X = REGISTRATE
    .item("x", p -> new SimpleAttachmentItem(p,
        Set.of(MobEffects.POISON),  // 免疫效果（可选）
        SlotTypes.MOUTH))           // 槽位
    .properties(p -> p.stacksTo(1))
    .register();
```

### 2.2 带属性修饰的附件

```java
public class XItem extends AttachmentItem {
    public XItem(Properties p) { super(p, SlotTypes.ARM); }

    @Override
    public void addAttributeModifiers(ItemAttributeModifierEvent event) {
        event.replaceModifier(Attributes.ARMOR,
            new AttributeModifier(ARMOR_ID, 2.0, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.CHEST);
    }
}
```

### 2.3 可切换附件

必须指定 `ControlPattern` + 覆写钩子：

```java
public class XItem extends AttachmentItem {
    public XItem(Properties p) { super(p, SlotTypes.EYES); }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.FREE_TOGGLE; }

    @Override
    protected void onStateEnter(ItemStack stack, int newState, LivingEntity e) {
        if (newState == STATE_READY) {
            // 开启时执行
        }
    }

    @Override
    protected void onStateExit(ItemStack stack, int oldState, LivingEntity e) {
        if (oldState == STATE_READY) {
            // 关闭时执行
        }
    }
}
```

### 2.4 一次性激活 + 冷却

```java
public class XItem extends AttachmentItem {
    private static final int COOLDOWN = 600;

    public XItem(Properties p) { super(p, SlotTypes.BACK); }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ONE_SHOT_COOLDOWN; }

    @Override
    public long getCooldownDuration() { return COOLDOWN; }

    @Override
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity e) {
        // 一次性效果：生成实体、施加药水等
    }
}
```

### 2.5 激活窗口 + 冷却（如 APS）

```java
public class XItem extends AttachmentItem {
    private static final int ACTIVE = 200;
    private static final int COOLDOWN = 600;

    public XItem(Properties p) { super(p, SlotTypes.SHOULDER); }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.ACTIVE_COOLDOWN; }

    @Override
    public long getActiveDuration() { return ACTIVE; }

    @Override
    public long getCooldownDuration() { return COOLDOWN; }

    @Override
    public void onTick(ItemStack a, ItemStack h, LivingEntity e, SlotType s) {
        super.onTick(a, h, e, s); // 必须调用基类状态机
        if (!isInActiveWindow(a)) return; // 仅在激活窗口内执行
        // 每 tick 逻辑
    }
}
```

---

## 三、控制模式 (ControlPattern)

| 模式 | 状态流转 | 按键行为 | 典型附件 |
|------|---------|---------|---------|
| `FREE_TOGGLE` | DISABLED ↔ READY | 切换开关 | 夜视眼镜 |
| `ACTIVE_COOLDOWN` | READY → ACTIVE → COOLING → READY | 从 READY 激活 | APS |
| `ONE_SHOT_COOLDOWN` | READY → COOLING → READY | 触发后进入冷却 | 激素针/导弹/火箭 |
| `ALWAYS_ON` | 始终 READY | 无 | 喷气背包 |
| `PASSIVE` | 无状态 | 无 | 防护板/外骨骼 |

---

## 四、状态机

### 4.1 状态常量

```
STATE_DISABLED  = 0   // 关闭
STATE_READY     = 1   // 就绪/开启
STATE_ACTIVE    = 2   // 激活窗口中
STATE_COOLING   = 3   // 冷却中
```

### 4.2 数据组件

| 组件 | 类型 | 用途 |
|------|------|------|
| `ATTACHMENT_STATE` | Integer | 当前状态 (0-3) |
| `ATTACHMENT_COOLDOWN` | Long | 当前阶段结束 tick (0=无计时器) |
| `ATTACHMENTS` | AttachmentsData | 护甲上所有附件映射 |

### 4.3 基类方法

```java
// 状态读写
int getState(ItemStack)
void setState(ItemStack, int)

// 计时器
long getTimer(ItemStack)
void setTimer(ItemStack, long)

// 查询
boolean isActive(ItemStack)        // READY 或 ACTIVE
boolean isInActiveWindow(ItemStack) // 仅在 ACTIVE 窗口内

// 钩子（子类覆写）
void onActivateOnce(ItemStack, ItemStack, LivingEntity)  // 一次性激活
void onStateEnter(ItemStack, int newState, LivingEntity) // 进入状态
void onStateExit(ItemStack, int oldState, LivingEntity)  // 离开状态
```

---

## 五、减伤与免疫

### 5.1 覆写方法

```java
@Override public float getDamageReduction() { return 0.10f; }      // 通用减伤 10%
@Override public float getFallDamageReduction() { return 0.20f; }  // 摔落减伤 20%
@Override public float getFallDistanceReduction() { return 1.0f; }  // 摔落高度递减 1 格
@Override public Set<Holder<MobEffect>> getImmunities() {           // 状态效果免疫
    return Set.of(MobEffects.POISON, MobEffects.WITHER);
}
```

### 5.2 无上限叠加

所有减伤和距离减免累加，无上限（`PEGameEvents` + `LivingEntityMixin`）。

---

## 六、HUD 覆盖层

基类自动处理。`PECooldownOverlay` 读取 `ATTACHMENT_STATE` + `ATTACHMENT_COOLDOWN`：

| 状态 | 显示 |
|------|------|
| READY | 绿色 ● |
| ACTIVE | 青色 ⚡ + 倒计时 |
| COOLING | ⌛ + 倒计时（灰→黄→红） |
| 创造模式 | 全部紫色 |

---

## 七、热键绑定

```java
// PEKeyBindings.java
while (TOGGLE_APS.consumeClick()) {
    tryActivateAttachment(ApsItem.class);
}
```

`tryActivateAttachment(Class)` — 遍历四件护甲，找到指定类的附件，发包到服务端触发 `onActivatePress`。

---

## 八、配置项 (PEServerConfig)

生成在 `protectionengineering-server.toml`：

```toml
[aps]
interceptRange = 5.0
activeDuration = 200
cooldownTicks = 600

[missile]
targetRange = 512.0
flightSpeed = 3.0
closeSpeed = 4.5
maxLife = 200

[rocket]
cooldownTicks = 200

[hormone]
cooldownTicks = 1200

[dodge]
dodgeStrength = 1.0
cooldownTicks = 200
```

---

## 九、3D 模型

```java
@Override
public EntityModel<?> createAttachmentModel(EntityModelSet modelSet) {
    return new XModel<>(modelSet.bakeLayer(XModel.LAYER_LOCATION));
}

@Override
public ResourceLocation getAttachmentTexture() {
    return ResourceLocation.fromNamespaceAndPath("protectionengineering", "textures/models/armor/x.png");
}
```

模型层在 `PEModelLayers.registerLayerDefinitions()` 注册。
