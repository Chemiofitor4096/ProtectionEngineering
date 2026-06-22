# 工程防护 - 堡垒生存 Mod 实现文档

> 基于 Blockbench 4.12.5 模型分析 | Minecraft 1.17+ (Mojang mappings)

---

## 一、项目总览

本项目是一个 Minecraft Java Edition 模组，主题为**工程堡垒生存**。核心玩法是玩家穿戴一套可高度自定义的工程师护甲，通过在工作台上安装各种附件模块来获得不同的功能加成。

### 1.1 设计理念

| 层级 | 名称 | 定位 |
|------|------|------|
| 基础层 | 工程师护甲四件套 | 提供基础防护，作为附件的载体 |
| 头盔附件 (5种) | 眼部/嘴部装备 | 视觉增强、环境防护、战斗辅助 |
| 胸甲附件 (11种) | 背部/肩部/手部装备 | 机动、攻击、防御核心 |
| 腿甲附件 (4种) | 腿部/足部装备 | 移动增强、负重提升 |
| 武器系统 | 锯剑、盾牌 | 近战与防御 |

### 1.2 装备槽位系统

根据 GUI 贴图分析，工作台设计了 5 个附件槽位：

```
┌─────────────────────────┐
│   工作台 (Workbench)     │
│                         │
│  [眼部] [嘴部] [肩部]    │
│  [盔甲] [手部]          │
│                         │
│  [基础护甲放置区]        │
└─────────────────────────┘
```

---

## 二、模型文件结构

```
工程防护/
├── helmet/                    # 头盔及附件
│   ├── helmet.png / .bbmodel
│   ├── helmet_attach_air_filter.java / .png / .bbmodel
│   ├── helmet_attach_dive_device.java / .png
│   ├── helmet_attach_glass.java           # 工程师眼镜
│   ├── helmet_attach_night_glass.java     # 夜视眼镜
│   └── helmet_attach_hormone.java         # 激素注射器
│
├── chestplate/                # 胸甲及附件
│   ├── chestplate.java / .png / .bbmodel
│   ├── chestplate_rightarm.java / .bbmodel
│   ├── chestplate_leftarm.java / .bbmodel
│   ├── chestplate_attach_jetpack.java         # 喷气背包
│   ├── chestplate_attach_dodge_jetpack.java   # 闪避喷气
│   ├── chestplate_attach_momentum_jetpack.java # 动量喷气
│   ├── chestplate_attach_rocket_pack.java     # 火箭弹发射器
│   ├── chestplate_attach_missile_pack.java    # 导弹发射器
│   ├── chestplate_attach_extra_mechanical_arm.java  # 额外机械臂 (左右)
│   ├── chestplate_attach_mechanical_gauntlet.java   # 机械拳套 (左右)
│   ├── chestplate_attach_sturdy_plate.java    # 坚固防护板
│   ├── chestplate_attach_netherite_plate.java # 下界合金防护板
│   ├── chestplate_attach_aps.java             # 主动防御系统
│   └── chestplate_attach_adv_aps.java         # 高级主动防御系统
│
├── downbody/                  # 腿甲/鞋及附件
│   ├── downbody.bbmodel
│   ├── leggings_right.java / left.java
│   ├── boots_right.java / left.java
│   ├── leggings_attach_light_exo.java (左右)
│   └── leggings_attach_heavy_exo.java (左右)
│
├── sword/                     # 武器
│   ├── saw_sword.json / .png / .bbmodel
│
├── shield/                    # 盾牌
│   ├── shield.json / .png / .bbmodel
│   └── shield_blocking.json / .bbmodel
│
├── 杂项模型/
│   └── missile.java           # 导弹实体模型
│
├── 贴图、hud、gui/            # GUI 贴图资源
│   ├── 工作台gui.png
│   ├── 工作台槽位-*.png       # 各槽位图标
│   └── 无背景版/              # 去背图标
│
└── 音效/
    ├── 闪避喷气.ogg
    └── 火箭弹发射.ogg
```

---

## 三、装备系统详细设计

### 3.1 基础护甲

#### 工程师兜帽 (Engineer Hood)
- **模型**: `helmet/helmet.bbmodel`
- **纹理**: `helmet/helmet.png`
- **特点**: 覆盖头部的基础兜帽，带有护目镜挂载点和嘴部设备接口
- **纹理尺寸**: 64x64

#### 工程师胸甲 (Engineer Chestplate)
- **模型**: `chestplate/chestplate.bbmodel`
- **纹理**: `chestplate/chestplate.png`
- **子模型**: 胸甲主体 + 左右臂甲
- **特点**: 
  - 背部有统一的机械背包挂载点（所有背包类附件共用此接口）
  - 肩部有附加装甲挂载点
  - 手部有机械外骨骼挂载点
  - 胸前有勋章装饰
- **纹理尺寸**: 64x64

#### 工程师护腿 (Engineer Leggings)
- **模型**: `downbody/` 系列
- **子模型**: 左右腿各独立模型 + 左右脚独立模型
- **纹理尺寸**: 64x64

#### 工程师劳保靴 (Engineer Safety Boots)
- **模型**: `downbody/boots_*.java`
- **特点**: 带有鞋底改造接口

---

### 3.2 头盔附件

#### (1) 空气过滤器 (Air Filter)
- **文件**: `helmet_attach_air_filter.*`
- **槽位**: 嘴部
- **纹理尺寸**: 16x16
- **功能预期**: 
  - 免疫中毒、凋零等空气传播负面效果
  - 提供水下呼吸（有限时间）
  - 阻止细雪/粉末雪进入视野

#### (2) 潜水设备 (Dive Device)
- **文件**: `helmet_attach_dive_device.*`
- **槽位**: 嘴部
- **纹理尺寸**: 32x32
- **功能预期**: 
  - 提供长时间水下呼吸
  - 提升水下视野清晰度
  - 提升水下挖掘速度

#### (3) 工程师眼镜 (Engineer Goggles)
- **文件**: `helmet_attach_glass.*`
- **槽位**: 眼部
- **纹理尺寸**: 64x64
- **功能预期**: 
  - 显示方块信息（类似 HWYLA/Jade）
  - 显示红石信号强度
  - 缩放视野

#### (4) 夜视眼镜 (Night Vision Goggles)
- **文件**: `helmet_attach_night_glass.*`
- **槽位**: 眼部
- **纹理尺寸**: 64x64
- **功能预期**: 
  - 提供夜视效果
  - 可能带有高亮生物功能
  - 模型比普通眼镜更复杂，带有额外组件

#### (5) 激素注射器 (Hormone Injector)
- **文件**: `helmet_attach_hormone.*`
- **槽位**: 嘴部/头盔侧挂
- **纹理尺寸**: 32x32
- **功能预期**: 
  - 低血量时自动注射治疗/强化激素
  - 提供临时的力量、速度增益
  - GUI 中有叠加层显示

---

### 3.3 胸甲附件

#### 机动类

##### (1) 喷气背包 (Jetpack)
- **文件**: `chestplate_attach_jetpack.*`
- **纹理尺寸**: 64x64
- **结构**: MachineBackpack → MachineBackpackMain + MachineElytra
- **功能预期**: 
  - 按住跳跃键上升飞行
  - 消耗燃料（可能是煤炭/烈焰粉/自定义燃料）
  - Elytra 部件用于滑翔

##### (2) 闪避喷气包 (Dodge Jetpack)
- **文件**: `chestplate_attach_dodge_jetpack.*`
- **纹理尺寸**: 64x64
- **结构**: DodgePack → LeftWing(含LeftOuterWing) + RightWing(含RightOuterWing) + LeftLowerWing + RightLowerWing + MainPack
- **特点**: 拥有上下两对复杂机翼结构，共 6 组可动翼面
- **功能预期**: 
  - 双击方向键进行快速闪避突进
  - 闪避瞬间有无敌帧
  - 有冷却时间
  - 音效: `闪避喷气.ogg`

##### (3) 动量喷气包 (Momentum Jetpack)
- **文件**: `chestplate_attach_momentum_jetpack.*`
- **纹理尺寸**: 64x64
- **结构**: MachineBackpack → MachineBackpackMain + MachineElytra + Wings
- **特点**: 拥有大型固定翼（Wings），翼展约 40 像素宽
- **功能预期**: 
  - 水平飞行速度快
  - 适合长距离航行
  - 机动性较差但续航强

##### (4) 火箭弹发射包 (Rocket Pack)
- **文件**: `chestplate_attach_rocket_pack.*`
- **纹理尺寸**: 64x64
- **结构**: MachineBackpack → MachineBackpackMain + Rocket (左右各一)
- **功能预期**: 
  - 发射爆炸性火箭弹
  - 范围伤害
  - 音效: `火箭弹发射.ogg`

##### (5) 导弹发射包 (Missile Pack)
- **文件**: `chestplate_attach_missile_pack.*`
- **纹理尺寸**: 64x64
- **结构**: MachineBackpack → MachineBackpackMain（带两个竖向导弹发射管）
- **独立导弹模型**: `杂项模型/missile.java` (32x32)
- **功能预期**: 
  - 发射追踪导弹
  - 高单发伤害
  - 导弹实体需要独立渲染和追踪逻辑

#### 战斗类

##### (6) 额外机械臂 (Extra Mechanical Arm)
- **文件**: `chestplate_attach_extra_mechanical_arm.*` (左右独立)
- **纹理尺寸**: 64x64
- **结构**: 从肩部延伸的多关节机械臂，带齿轮传动装置
- **功能预期**: 
  - 增加副手使用能力（如双持工具）
  - 可能增加触及距离
  - 自动采集/放置方块

##### (7) 机械拳套 (Mechanical Gauntlet)
- **文件**: `chestplate_attach_mechanical_gauntlet.*` (左右独立)
- **纹理尺寸**: 32x32
- **功能预期**: 
  - 大幅提升近战攻击伤害
  - 可能带有击退/击飞效果
  - 破坏方块速度提升

#### 防御类

##### (8) 坚固防护板 (Sturdy Plate)
- **文件**: `chestplate_attach_sturdy_plate.*`
- **纹理尺寸**: 16x16
- **功能预期**: 
  - 提供额外护甲值
  - 可能有伤害减免
  - 肩部附加装甲

##### (9) 下界合金防护板 (Netherite Plate)
- **文件**: `chestplate_attach_netherite_plate.*`
- **纹理尺寸**: 16x16
- **功能预期**: 
  - 提供最高级护甲加成
  - 击退抗性
  - 火焰/熔岩防护

##### (10) 主动防御系统 (APS)
- **文件**: `chestplate_attach_aps.*`
- **纹理尺寸**: 32x32
- **功能预期**: 
  - 自动拦截飞来的投射物（箭、火球等）
  - 有限拦截次数或冷却
  - 肩部安装的防御装置

##### (11) 高级主动防御系统 (Advanced APS)
- **文件**: `chestplate_attach_adv_aps.*`
- **纹理尺寸**: 32x32
- **纹理不同**: 更复杂的纹理，可能代表更强版本
- **功能预期**: 
  - 拦截范围更大
  - 可拦截更多类型（恶魂火球、潜影弹等）
  - 更短冷却时间

---

### 3.4 腿甲附件

#### (1) 轻型外骨骼辅助设备 (Light Exoskeleton)
- **文件**: `leggings_attach_light_exo.*` (左右独立)
- **纹理尺寸**: 32x32
- **结构**: 大腿外骨骼框架 + 机械足部支撑
- **功能预期**: 
  - 移动速度小幅提升
  - 跳跃高度提升
  - 冲刺消耗饱食度降低

#### (2) 重型外骨骼辅助设备 (Heavy Exoskeleton)
- **文件**: `leggings_attach_heavy_exo.*` (左右独立)
- **纹理尺寸**: 64x64
- **结构**: 完整的机械腿部框架：
  - MachineLeg → LeftMachineLeg → LeftMachineUpperLeg → LeftMachineLowerLeg → LeftMachineLowestLeg → LeftMachineFoot
  - 包含液压推杆 (LeftMachineLowerLegPush) 和减震器 (LeftMachineLowestLegRes)
- **功能预期**: 
  - 大幅提升负重能力
  - 免疫摔落伤害
  - 可践踏造成范围伤害
  - 移动速度不变或略降

#### (3) 改良鞋底 (Improved Soles)
- **GUI 图标**: `改良鞋底.png`
- **功能预期**: 
  - 提升移动速度
  - 灵魂沙上行走不受影响
  - 细雪上可行走

#### (4) 缓冲鞋底 (Cushioned Soles)
- **GUI 图标**: `缓冲鞋底.png`
- **功能预期**: 
  - 完全免疫摔落伤害
  - 潜行时移动无声
  - 可能带有弹跳功能

---

### 3.5 武器系统

#### 锯剑 (Saw Sword)
- **文件**: `sword/saw_sword.*`
- **格式**: JSON 模型 (Bedrock/基岩版格式 `1.21.6`)
- **纹理尺寸**: 32x32
- **结构特点**: 
  - 长约 24 像素的链锯剑身
  - 带锯齿状刀片细节
  - 护手和握柄完整建模
- **功能预期**: 
  - 高攻击速度（链锯特性）
  - 可能带有多段伤害
  - 可破坏木质方块

#### 盾牌 (Shield)
- **文件**: `shield/shield.*`
- **格式**: JSON 模型 (基岩版格式)
- **纹理尺寸**: 64x64
- **结构特点**: 
  - 可展开/收缩设计（shield.json + shield_blocking.json）
  - 非对称多段折叠结构
  - 展开时覆盖面积大
- **功能预期**: 
  - 格挡范围大于原版盾牌
  - 可能带有尖刺反伤
  - 耐久度高

---

## 四、技术实现路线图

### Phase 1: 基础框架搭建

```
1. 创建 Mod 主类
   - 设置 MODID (建议: "engineer_protection" 或 "fortress_survival")
   - 注册创造模式物品栏
   - 配置客户端/服务端代理

2. 基础护甲注册
   - 创建 ArmorMaterial (工程师护甲材质)
     - 耐久: 高于铁低于钻石
     - 护甲值: 中等
     - 击退抗性: 0.1
   - 注册头盔/胸甲/护腿/靴子物品
   - 实现 ArmorItem 的基础渲染层

3. 模型层注册
   - 使用 EntityRenderersEvent.AddLayers 注册各部位的 ArmorLayer
   - 头盔: HelmetModel
   - 胸甲: ChestplateModel (含左右臂)
   - 护腿: LeggingsModel (含左右腿)
   - 靴子: BootsModel (含左右脚)
```

### Phase 2: 附件槽位系统

```
1. 设计 Capability 系统
   - IEngineerArmorData: 存储每个护甲部件上安装的附件
   - 使用 Capability 或 ItemStack NBT 存储
   - 实现同步到客户端的网络包

2. 工作台 GUI 实现
   - 创建 WorkbenchBlock / WorkbenchBlockEntity
   - 实现 AbstractContainerMenu (5个附件槽位 + 1个护甲槽位)
   - Screen 渲染: 使用 工作台gui.png
   - 附件槽位映射:
     [槽位0:眼部] [槽位1:嘴部] [槽位2:肩部]
     [槽位3:盔甲] [槽位4:手部]

3. 附件安装逻辑
   - 附件物品带有 NBT tag 标记其目标槽位
   - 只有对应槽位的附件才能放入
   - 安装后附件的模型层在玩家身上渲染
```

### Phase 3: 附件功能实现

#### 头盔附件

```
空气过滤器:
  - 监听 LivingUpdateEvent, 清除中毒/凋零效果
  - 给予 WaterBreathing 效果 (如果装备)
  - 实现 IForgeItem 的 canApplyAtEnchantingTable (不可附魔)

潜水设备:
  - 持续给予 ConduitPower 效果
  - 增加水下视野 (通过 FogRenderer 事件)
  - 水下挖掘速度 × 1.5

工程师眼镜:
  - 客户端: 按下快捷键显示目标方块信息 HUD
  - 显示红石信号等级
  - 可缩放 (类似 OptiFine 的 C 键)

夜视眼镜:
  - 持续给予 NightVision 效果
  - 可叠加 Glowing 效果高亮生物 (切换模式)
  - 需要消耗能量/耐久

激素注射器:
  - 监听 LivingHurtEvent, 血量低于 30% 自动触发
  - 给予: Strength II (8s), Speed II (8s), Resistance I (5s)
  - 冷却时间: 60s
  - HUD 叠加层显示冷却状态
```

#### 胸甲附件 - 机动类

```
喷气背包:
  - 按住空格上升, 消耗燃料值
  - 燃料条 (HUD 渲染)
  - 燃料: 煤炭/烈焰粉通过工作台充能
  - Elytra 滑翔: 空中按潜行键切换滑翔模式

闪避喷气:
  - 双击方向键触发, 向该方向快速突进 6 格
  - 突进期间无敌 (0.3s)
  - 冷却: 3s
  - 播放音效: 闪避喷气.ogg
  - 粒子效果: 白色烟雾

动量喷气:
  - 水平飞行速度 × 2.0
  - 垂直上升速度 × 0.5 (不适合垂直飞行)
  - 滑翔时缓慢下降
  - 适合长距离探索

火箭弹发射:
  - 右键发射火箭弹实体
  - 爆炸伤害: 6 (半径 3 格)
  - 不破坏方块 (griefing 可控)
  - 冷却: 2s
  - 弹药: 火药 + 铁锭
  - 音效: 火箭弹发射.ogg

导弹发射:
  - 右键锁定目标后发射
  - 追踪最近实体 (20 格内)
  - 伤害: 10 (半径 4 格)
  - 冷却: 5s
  - 导弹实体: 使用 missile.java 模型
```

#### 胸甲附件 - 战斗类

```
额外机械臂:
  - 允许同时使用主手和副手工具 (如双镐挖掘)
  - 触及距离 + 2
  - 自动采集: 右键方块时机械臂自动挖掘

机械拳套:
  - 近战伤害 × 2.5
  - 攻击附带击退 III
  - 可破坏 1.5 硬度以下方块 (右键)
```

#### 胸甲附件 - 防御类

```
坚固防护板:
  - 护甲值 + 4
  - 伤害减免: 受到的伤害 -10%

下界合金防护板:
  - 护甲值 + 6
  - 击退抗性 + 0.5
  - 火焰伤害减免 50%

APS (主动防御):
  - 自动摧毁 4 格内飞来的投射物
  - 每 3 秒可拦截 1 次
  - 消耗能量

高级APS:
  - 拦截范围 6 格
  - 每 1.5 秒可拦截 1 次
  - 可拦截爆炸性投射物 (火球等)
```

#### 腿甲附件

```
轻型外骨骼:
  - 移动速度 + 15%
  - 跳跃高度 + 0.5 格
  - 冲刺消耗 - 30%

重型外骨骼:
  - 移动速度 - 10%
  - 免疫摔落伤害
  - 潜行时践踏: 对脚下 2 格内生物造成 4 点伤害
  - 最大负重: 可携带额外 27 格物品 (内置存储)

改良鞋底:
  - 移动速度 + 10%
  - 灵魂沙/细雪上正常行走

缓冲鞋底:
  - 完全免疫摔落伤害
  - 潜行时静音移动
  - 跳跃后落地无减速
```

### Phase 4: 渲染系统

```
1. 模型层叠加渲染
   - 每个附件作为独立的 RenderLayer 叠加在玩家模型上
   - 使用 HumanoidArmorLayer 的模式
   - 需要处理附件模型的旋转和动画

2. 动画系统
   - 飞行时喷气背包火焰粒子
   - 机械臂随玩家手臂动画同步
   - 外骨骼腿部与玩家腿部动画同步
   - 闪避时的翅膀展开/收回动画

3. HUD 渲染
   - 能量/燃料条 (ForgeHudEvent)
   - 激素注射器冷却叠加层
   - APS 状态指示器
   - 夜视眼镜切换指示

4. 粒子效果
   - 喷气背包: 火焰/烟雾粒子
   - 闪避: 白色烟雾轨迹
   - 火箭弹: 火焰尾迹
   - 导弹: 烟雾尾迹
   - APS 拦截: 小型爆炸粒子
```

### Phase 5: 音效与本地化

```
音效:
  闪避喷气.ogg → dodge_jetpack 事件
  火箭弹发射.ogg → rocket_launch 事件
  需要补充:
    - 喷气背包飞行循环音效
    - 机械臂运转音效
    - APS 拦截音效
    - 护甲装备音效
    - 机械拳套攻击音效

本地化:
  zh_CN.json / en_US.json
  所有物品名称、GUI 文本、提示信息
```

---

## 五、关键数据定义

### 5.1 护甲材质参数

```java
// 工程师护甲基础材质
ArmorMaterial ENGINEER_ARMOR = new ArmorMaterial(
    BASE_DURABILITY: { 275, 400, 375, 325 }, // 头/胸/腿/脚
    PROTECTION: { 2, 6, 5, 2 },              // 护甲值
    ENCHANTABILITY: 18,
    TOUGHNESS: 1.0f,
    KNOCKBACK_RESISTANCE: 0.05f,
    REPAIR_INGREDIENT: () -> Items.IRON_INGOT
);
```

### 5.2 附件能力接口

```java
public interface IAttachment {
    EquipmentSlot getTargetSlot();      // 目标装备槽
    AttachmentSlotType getSlotType();   // 眼部/嘴部/肩部/手部/盔甲
    void onEquip(ItemStack stack, LivingEntity entity);
    void onUnequip(ItemStack stack, LivingEntity entity);
    void onTick(ItemStack stack, LivingEntity entity);  // 每 tick 执行
    boolean canInstallOn(ItemStack armor);  // 是否可安装到该护甲
}
```

### 5.3 槽位类型枚举

```java
public enum AttachmentSlotType {
    EYES("眼部", 0),
    MOUTH("嘴部", 1),
    SHOULDER("肩部", 2),
    ARMOR("盔甲", 3),
    HAND("手部", 4);
}
```

---

## 六、当前进度评估

### 已完成 ✅
- [x] 所有 3D 模型 (Blockbench .bbmodel / .java)
- [x] 所有纹理贴图 (.png)
- [x] 基础 GUI 设计 (工作台 + 槽位图标)
- [x] 部分音效 (闪避喷气、火箭弹发射)
- [x] 武器模型 (锯剑 JSON、盾牌 JSON)
- [x] 导弹实体模型

### 待完成 ❌
- [ ] Mod 主类和注册系统
- [ ] 工作台方块与 GUI 交互逻辑
- [ ] 附件安装/卸载 Capability 系统
- [ ] 所有附件的功能逻辑代码
- [ ] 渲染层注册与模型动画
- [ ] HUD 渲染系统
- [ ] 粒子效果系统
- [ ] 网络数据包同步
- [ ] 燃料/能量系统
- [ ] 合成配方
- [ ] 音效注册与播放
- [ ] 本地化文件
- [ ] 平衡性测试

---

## 七、推荐开发顺序

1. **Week 1-2**: 基础框架 + 护甲渲染
2. **Week 3-4**: 工作台 GUI + 附件槽位系统
3. **Week 5-6**: 头盔附件 (最简单的附件组)
4. **Week 7-8**: 胸甲防御类附件
5. **Week 9-10**: 胸甲机动类附件 (喷气背包系)
6. **Week 11-12**: 胸甲战斗类附件 + 武器
7. **Week 13**: 腿甲附件
8. **Week 14**: 音效、粒子、HUD、平衡调整
9. **Week 15**: 本地化、Bug 修复、发布准备

---

## 八、技术要点提醒

1. **模型类名问题**: Blockbench 导出的 Java 类多为 `unknown`，需要根据实际附件重命名，并正确设置 `LAYER_LOCATION` 的 ResourceLocation。

2. **JSON 模型**: `saw_sword.json` 和 `shield.json` 是基岩版格式 (`format_version: 1.21.6`)，Java 版需要转换为 `.java` EntityModel 类，或使用 GeckoLib 等动画库加载。

3. **纹理路径**: Java 类中的 `texOffs` 已在建模时确定，纹理文件需放在 `assets/<modid>/textures/` 对应路径下。

4. **左右镜像**: 大部分附件有独立的左右模型 (mirrored)，确保注册时区分左右侧。

5. **性能考虑**: 
   - 每个附件的 `onTick` 应控制执行频率
   - 粒子效果需控制数量上限
   - HUD 渲染避免每帧创建对象

---

*文档生成日期: 2026-06-14 | 基于 Blockbench 4.12.5 导出模型分析*
