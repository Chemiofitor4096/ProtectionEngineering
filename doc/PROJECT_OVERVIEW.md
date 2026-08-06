# Protection Engineering (1.21.1) -- Project Overview

## Purpose and Goals

**Protection Engineering** is a Minecraft NeoForge mod that introduces a complete engineer-themed armor system. Players craft four armor pieces (Hood, Chestplate, Leggings, Boots) that collectively provide **17 attachment slots**. Attachments are installable items that grant passive immunities, attribute bonuses, or active skills with a unified state machine.

The mod's design philosophy emphasizes:
- **Slot-based customization**: Choose which attachments to install in each slot
- **Unified state machine**: Every active attachment follows the same DISABLED/READY/ACTIVE/COOLING state pattern
- **Visual fidelity**: All four armor pieces and every attachment have custom 3D models rendered on the player
- **Create integration**: Create is a required dependency (materials in recipes, goggles system, diving boots, mechanical-arm armor emitter)
- **Configurable balance**: Server configs tune all active ability parameters

## Minecraft Version and Mod Loader

| Property | Value |
|---|---|
| **Minecraft** | 1.21.1 |
| **Mod Loader** | NeoForge 21.1.219 |
| **Java** | 21 |
| **Mappings** | Parchment 2024.11.17 (Minecraft 1.21.1) |
| **Mod Version** | 1.6.0 |
| **Mod ID** | `protectionengineering` |

## Architecture

The project is a single-module NeoForge mod using the NeoForge ModDev Gradle plugin.

```
C:/Projects/protectionengineering-1.21.1/
  build.gradle                          -- NeoForge MDG 2.0.141
  settings.gradle
  gradle.properties
  CLAUDE.md                             -- Comprehensive dev guide (authoritative)
  AGENTS.md                             -- Agent-oriented dev guide
  README.md                             -- Feature overview with attachment table

  src/main/java/com/chemiofitor/protection_engineering/
    ProtectionEngineering.java          -- Main class (Registrate, config, registrations)
    ProtectionEngineeringClient.java    -- Client init (shield blocking predicate)

    api/                                -- Public API layer (7 files)
      IAttachment.java                  -- Attachment contract + AttributeBonus + bonus() factory
      IAttachmentHost.java              -- Host contract (install/remove/canInstall defaults)
      AttachmentsData.java              -- Persisted attachment map (codec + stream codec)
      AttachmentUtil.java               -- Attachment iteration/lookup/reduce helpers
      SlotType.java / SlotTypes.java    -- Slot registry + equipment slot group side-table
      IGradedRepair.java                -- Graded repair contract (20 units = full durability)
    block/                              -- Workbench block + BE + Armor Emitter block
    client/                             -- Rendering, keybinds, HUD overlays
      layer/                            -- PEArmorLayer (3D armor+attachment rendering)
      model/                            -- 33 Blockbench-exported model classes
      renderer/                         -- Missile + rocket entity renderers
    compat/                             -- Detail Armor Bar, Iron's Spells, PlayerAnimator
    config/                             -- Client + server config specs
    data/                               -- Data generation (lang, recipes)
    entity/                             -- Missile, Rocket, Thrust entities
    event/                              -- Game events (damage, immunities, anvil, network)
    item/                               -- 38 item classes (4 armor + 30 attachments + missile/saw-sword/shield)
    menu/                               -- Workbench GUI container + screen
    mixin/                              -- 4 mixins (slip, shield angle, shield cooldown, diving boots)
    network/                            -- 2 custom payloads (toggle + thrust)
    registry/                           -- All deferred registers (9 classes)
```

### Build System

- **Plugin**: `net.neoforged.moddev` 2.0.141
- **Data Generation**: `src/generated/resources/` output, blockbench files excluded
- **Publishing**: maven-publish configured to `repo/` directory

### Key Dependencies

| Dependency | Version | Type |
|---|---|---|
| NeoForge | 21.1.219 | Required |
| Create | 6.0.10-280 | Required (implementation, transitive=false) |
| Ponder | 1.0.82+mc1.21.1 | Implementation |
| Flywheel API | 1.0.6 | CompileOnly + Runtime |
| Registrate | MC1.21-1.3.0+67 | Implementation |
| JEI API | 19.27.0.340 | CompileOnly + Runtime (optional compat) |
| Detail Armor Bar Reconstructed | 5.0.2 | CompileOnly + Runtime (guarded compat) |
| Iron's Spells 'n Spellbooks | 1.21.1-3.16.2 | CompileOnly + Runtime (guarded compat) |
| PlayerAnimator | 2.0.4+1.21.1-forge | CompileOnly + Runtime (guarded compat) |

All optional compats are guarded by `ModList.get().isLoaded(...)`; their classes are never referenced unguarded.

## Key Features and Mechanics

### 1. Engineer Armor System

Four armor pieces crafted from Create materials (netherite-tier stats, durability factor 56 = 1.5× netherite, enchant value 15 but **not enchantable**):

| Piece | Dedicated Slots | Generic Slots | Total |
|---|---|---|---|
| Engineer Hood | EYES, MOUTH | LINING, HELMET_DECORATION | 4 |
| Engineer Chestplate | SHOULDER, CHESTPLATE, BACK, ARM | LINING, CHESTPLATE_DECORATION | 6 |
| Engineer Leggings | LEG, KNEE | LINING, LEGGINGS_DECORATION | 4 |
| Engineer Boots | FOOT | LINING, BOOTS_DECORATION | 3 |

Base protection is 3/8/6/3 (netherite-tier), toughness 3.0, knockback resistance 0.1. The chestplate grants elytra flight when an active jetpack is installed; the boots allow walking on powdered snow when Improved Soles are installed.

### 2. Attachment State Machine

Every active attachment uses a unified state machine:

| State | Value | Symbol | Color (Survival/Creative) |
|---|---|---|---|
| DISABLED | 0 | circle (empty) | Gray / Purple |
| READY | 1 | filled circle | Green `#55FF55` / Purple `#FF55FF` |
| ACTIVE | 2 | lightning bolt + timer | Cyan `#55FFFF` / Purple |
| COOLING | 3 | hourglass + timer | Gray-to-Yellow-to-Red / Purple |

**Control Patterns** determine state transitions:

| Pattern | Flow | Examples |
|---|---|---|
| `FREE_TOGGLE` | DISABLED <--> READY | Night Vision, Spyglass |
| `ACTIVE_COOLDOWN` | READY -> ACTIVE -> COOLING -> READY | APS, Advanced APS |
| `ONE_SHOT_COOLDOWN` | READY -> COOLING -> READY | Hormone, Rockets, Missiles, Dodge |
| `ALWAYS_ON` | Always READY | Jetpack, Momentum Jetpack |
| `PASSIVE` | No state | Plates, Exoskeletons, Soles, Linings |

### 3. Attachments (30 total, plus 9 ISS school linings)

**Head — Eyes (4):**
- Night Vision Goggles: FREE_TOGGLE infinite night vision, darkness/blindness immunity
- Engineer Goggles: Create goggles overlay integration (block info / redstone / zoom)
- Hormone Injector: ONE_SHOT — Regen + Resistance 10s, Nausea 5s, 60s CD
- Spyglass: FREE_TOGGLE zoom telescope

**Head — Mouth (2):**
- Air Filter: immunity to poison, nausea, weakness, mining fatigue
- Diving Device: permanent Water Breathing II

**Shoulder (3):**
- APS: ACTIVE_COOLDOWN projectile interception + counter-explosion (10s window, 30s CD)
- Advanced APS: 1.5× active duration (15s)
- Rocket Launcher: ONE_SHOT scatter-shot rockets consuming fireworks (up to 12, 10s CD)

**Chestplate (2 + 1 decoration):**
- Sturdy Plate: +2 armor, 10% damage reduction
- Netherite Plate: +4 armor, 15% damage reduction
- Purity Mark: purely decorative (CHESTPLATE_DECORATION)

**Back (4):**
- Jetpack: ALWAYS_ON elytra flight + J-key thrust
- Momentum Jetpack: +20% horizontal speed, soul-fire particles
- Dodge Jetpack: ONE_SHOT melee immunity + evasive dodge (10s CD)
- Missile Pack: ONE_SHOT lock-on guided missile (consumes Missile ammo, 60s CD)

**Arm (2):**
- Extra Mechanical Arm: +2 reach (block + entity)
- Mechanical Gauntlet: +20% melee damage

**Leg / Knee (4):**
- Light Exoskeleton: +10% speed, +0.5 jump, +0.4 step height, slow immunity
- Heavy Exoskeleton: +2 armor, +0.4 step height, 20% fall reduction, slow immunity
- Springy Kneecap: +0.5 jump (JUMP_STRENGTH)
- Cushioned Kneecap: -10% fall damage

**Foot (6):**
- Improved Soles: No-slip (ice/slime/honey/soul sand immunity), walk on powdered snow
- Cushioned Soles: -20% fall damage, -1 fall distance
- Frost Soles: frost-walks water into frosted ice
- Insulated Soles: immune to ground heat (magma/hot floor) damage
- Silent Soles: cancels sculk vibrations (evades Warden)
- Diving Soles: recognized as Create diving boots (sink + speed underwater)

**Lining (2 generic):**
- Blast Lining: 10% reduction on `DamageTypeTags.IS_EXPLOSION`
- Fire Lining: 10% reduction on `DamageTypeTags.IS_FIRE`

**Iron's Spells 'n Spellbooks linings (9, conditional):** Fire/Ice/Lightning/Holy/Ender/Blood/Evocation/Eldritch/Nature magic — each 10% reduction on its school's damage type + +50 max mana.

### 4. Weapons

- **Engineer Saw Sword**: Diamond-tier damage (6.0) & attack speed, netherite-tier durability (2031), works as an axe (strip/scrape/wax-off), sword+axe enchant compatibility, only graded-material repair
- **Engineer Shield**: 3× durability (1008), axe-break cooldown halved (50 ticks), wider block angle (216°), only graded-material repair

### 5. Active Ability Entities

- **ThrustEntity**: Invisible mounted entity applying the firework thrust formula to elytra flight
- **RocketProjectile**: Simple linear projectile, 6 damage explosion, 2-block radius, 5s lifetime
- **MissileEntity**: Guided missile with launch phase, steering logic, target-tracking, APS-interceptable, 8-block radius 40-damage explosion

### 6. Workbench

A block with a GUI for installing and removing attachments from engineer armor:
- 1 armor slot (input) + 4 attachment slots, **paginated** over the host's supported slots
- Slot-type compatibility checked via `IAttachment.compatibleSlots()`

### 7. Armor Emitter (Create mechanical-arm integration)

A pressure-plate-style block: when a creature that can wear equipment stands on it, Create Mechanical Arms treat it as an interaction point — delivered armor is auto-equipped and PE attachments are auto-installed; TAKE extracts equipment/attachments head-to-toe by mode (EQUIP / ATTACH / BOTH, shifted-right-click to cycle).

### 8. Data Generation

Server-side data generation provides:
- English localization for all items, slots, tooltips, keybinds, subtitles, HUD (`en_us.json` / `en_ud.json`)
- Vanilla shaped recipes (armor, saw sword, shield, missile ×4, purity mark, workbench) and a smithing upgrade (netherite plate)
- Create Mechanical Crafting recipes for **28 items** (+9 Iron's Spells linings gated by `whenModLoaded`)

## Dependencies and Relationships

- **Create**: Required dependency. Materials used in recipes (Sturdy Sheets, Brass Sheets, Precision Mechanisms, etc.). Engineer Goggles integrate with Create's goggle overlay; Diving Soles integrate with Create's diving boots; the Armor Emitter is a Create mechanical-arm interaction point.
- **Iron's Spells 'n Spellbooks**: Optional. 9 school-lining items registered only when the mod is present.
- **PlayerAnimator**: Optional. Armor layer stays attached during bend animations.
- **Detail Armor Bar Reconstructed**: Optional. Custom engineer armor bar HUD.
- **JEI**: Optional. Recipe viewing support (compileOnly + runtime).

## Data Component System

The mod uses NeoForge's Data Component system (1.20.5+) instead of NBT:

| Component | Type | Purpose |
|---|---|---|
| `ATTACHMENTS` | `AttachmentsData` | Map of slot type -> attachment ItemStack |
| `ATTACHMENT_STATE` | `Integer` | Unified state 0-3 |
| `ATTACHMENT_COOLDOWN` | `Long` | Tick when current phase ends |
| `ATTACHMENT_ACTIVE` | `Boolean` | Deprecated — migration only, never written |

## Sound System

16 sound events: attach_1, attach_2, equip_engineer_armor, night_vision_on/off, hormone_inject, thrust_jetpack, aps_activate, missile_launch/lock/flight/warning, rocket_launch, launch_fail, dodge_warning, dodge_jet. 15 events use custom OGG files; `equip_engineer_armor` reuses vanilla netherite equip sounds.

## Configuration

- **Client config** (`protectionengineering-client.toml`): HUD overlay X/Y offset
- **Server config** (`protectionengineering-server.toml`): APS range/duration/cooldown, missile speed/range/lifetime, rocket cooldown, hormone cooldown, dodge strength/cooldown, plus graded-repair material units (brass sheet 3 / sturdy sheet 10)

## Translation System

- English: Generated via `ProviderType.LANG` in data generation
- Chinese: Manually maintained in `assets/protectionengineering/lang/zh_cn.json`
- Key pattern: `item.protectionengineering.<name>`, `slot.protectionengineering.<name>`, `tooltip.protectionengineering.<name>`, etc.

## Credits

策划/美术：Rhodes_Koei · 代码：Chemiofitor
