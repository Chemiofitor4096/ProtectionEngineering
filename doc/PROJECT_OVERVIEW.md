# Protection Engineering (1.21.1) -- Project Overview

## Purpose and Goals

**Protection Engineering** is a Minecraft NeoForge mod that introduces a complete engineer-themed armor system. Players craft four armor pieces (Hood, Chestplate, Leggings, Boots) that collectively provide 9 attachment slots. Attachments are installable items that grant passive immunities, attribute bonuses, or active skills with a unified state machine.

The mod's design philosophy emphasizes:
- **Slot-based customization**: Choose which attachments to install in each slot
- **Unified state machine**: Every active attachment follows the same DISABLED/READY/ACTIVE/COOLING state pattern
- **Visual fidelity**: All four armor pieces and every attachment have custom 3D models rendered on the player
- **Create integration**: Uses Create materials in recipes and integrates with Create's goggles system
- **Configurable balance**: Server configs tune all active ability parameters

## Minecraft Version and Mod Loader

| Property | Value |
|---|---|
| **Minecraft** | 1.21.1 |
| **Mod Loader** | NeoForge 21.1.219 |
| **Java** | 21 |
| **Mappings** | Parchment 2024.11.17 (Minecraft 1.21.1) |
| **Mod Version** | 1.1.0 |
| **Mod ID** | `protectionengineering` |

## Architecture

The project is a single-module NeoForge mod using the NeoForge ModDev Gradle plugin.

```
C:/Projects/protectionengineering-1.21.1/
  build.gradle                          -- NeoForge MDG 2.0.141
  settings.gradle
  gradle.properties
  CLAUDE.md                             -- Comprehensive dev guide (auto-generated)
  README.md                             -- Feature overview with attachment table

  src/main/java/com/chemiofitor/protection_engineering/
    ProtectionEngineering.java          -- Main class (Registrate, config, registrations)
    ProtectionEngineeringClient.java    -- Client init (shield blocking predicate)

    api/                                -- Public API layer (5 files)
    block/                              -- Workbench block + BE
    client/                             -- Rendering, keybinds, HUD overlays
      layer/                            -- PEArmorLayer (3D armor+attachment rendering)
      model/                            -- 33 Blockbench-exported model classes
      renderer/                         -- Missile + rocket entity renderers
    compat/                             -- Detail Armor Bar Reconstructed compat
    config/                             -- Client + server config specs
    data/                               -- Data generation (lang, recipes)
    entity/                             -- Missile, Rocket, Thrust entities
    event/                              -- Game events (damage, immunities) + network events
    item/                               -- 32 item classes (4 armor + 28 attachments + weapons)
    menu/                               -- Workbench GUI container + screen
    mixin/                              -- 4 mixins (damage cap, shield, ice/snow)
    network/                            -- 2 custom payloads (toggle + thrust)
    registry/                           -- All deferred registers (6 classes)
```

### Build System

- **Plugin**: `net.neoforged.moddev` 2.0.141
- **Data Generation**: `src/generated/resources/` output, blockbench files excluded
- **Publishing**: Kessoku Maven (credentials via environment variables)

### Key Dependencies

| Dependency | Version | Type |
|---|---|---|
| NeoForge | 21.1.219 | Required |
| Create | 6.0.10-280 | Implementation (transitive=false) |
| Ponder | 1.0.82+mc1.21.1 | Implementation |
| Flywheel API | 1.0.6 | CompileOnly |
| Flywheel | 1.0.6 | Runtime |
| Registrate | MC1.21-1.3.0+67 | Implementation |
| JEI API | 19.27.0.340 | CompileOnly + Runtime |
| Detail Armor Bar Reconstructed | 5.0.2 | Implementation (optional compat) |

## Key Features and Mechanics

### 1. Engineer Armor System

Four armor pieces crafted from Create materials:

| Piece | Slots | Slot Types |
|---|---|---|
| Engineer Hood | 2 | EYES, MOUTH |
| Engineer Chestplate | 4 | SHOULDER, CHESTPLATE, BACK, ARM |
| Engineer Leggings | 2 | LEG, KNEE |
| Engineer Boots | 1 | FOOT |

The armor uses netherite-tier protection values (3/8/6/3), toughness 3.0, and knockback resistance 0.1. Durability factor is 56 (1.5x netherite). Armor is not enchantable.

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
| `PASSIVE` | No state | Plates, Exoskeletons, Soles |

### 3. Attachments (28 total)

**Head Attachments (6):**
- Air Filter: Immunity to poison, weakness, hunger, mining fatigue
- Diving Device: Permanent Water Breathing II
- Engineer Goggles: Create goggles overlay integration
- Night Vision Goggles: FREE_TOGGLE infinite night vision, darkness/blindness immunity
- Hormone Injector: ONE_SHOT: regeneration + resistance (10s), confusion (5s)
- Spyglass: FREE_TOGGLE zoom telescope

**Shoulder Attachments (3):**
- APS: ACTIVE_COOLDOWN projectile interception system (5-block range)
- Advanced APS: 1.5x active duration
- Rocket Launcher: ONE_SHOT scatter-shot rockets

**Chestplate Attachments (2):**
- Sturdy Plate: +2 armor, 10% damage reduction
- Netherite Plate: +4 armor, 15% damage reduction

**Back Attachments (5):**
- Jetpack: ALWAYS_ON, elytra flight with thrust boost
- Momentum Jetpack: +20% horizontal speed, soul fire particles
- Dodge Jetpack: ONE_SHOT, melee immunity + evasive dodge
- Missile Pack: ONE_SHOT, lock-on guided missile (consumes Missile ammo)
- Missile: Consumable ammo item

**Arm Attachments (2):**
- Extra Mechanical Arm: +2 reach
- Mecha Knuckle: +20% melee damage

**Leg Attachments (3):**
- Light Exoskeleton: +10% speed, +0.5 jump, +0.4 step height
- Heavy Exoskeleton: +2 armor, +0.4 step, 20% fall reduction, slowness immunity
- Cushioned Kneecap: -10% fall damage

**Foot Attachments (3):**
- Improved Soles: No-slip (ice/slime/soul sand immunity), walk on powdered snow
- Cushioned Soles: -20% fall damage, -1 fall distance
- Insulated Soles: Magma block immunity (no hot floor damage)

### 4. Weapons

- **Engineer Saw Sword**: Diamond-tier damage, netherite-tier durability, can strip/scrape/wax-off like an axe
- **Engineer Shield**: 3x durability (1008), half axe cooldown (50 ticks), wider block angle (216 degrees)

### 5. Active Ability Entities

- **ThrustEntity**: Invisible flying entity that applies elytra thrust boost formula
- **RocketProjectile**: Simple linear projectile, 6 damage explosion, 2-block radius
- **MissileEntity**: Guided missile with launch phase, steering logic, target-tracking, APS-interceptable

### 6. Workbench

A block with a GUI for installing and removing attachments from engineer armor:
- 1 armor slot (input)
- 4 attachment slots (input)
- Checks slot-type compatibility via `IAttachment.compatibleSlots()`

### 7. Data Generation

Server-side data generation provides:
- English localization for all items, slots, tooltips, keybinds, subtitles, HUD
- Vanilla shaped and smithing recipes
- Create Mechanical Crafting recipes for 18 items

## Dependencies and Relationships

- **Create**: Required dependency. Materials used in recipes (Sturdy Sheets, Brass Sheets, Precision Mechanisms, etc.). Engineer Goggles integrate with Create's goggle overlay system.
- **Detail Armor Bar Reconstructed**: Optional. Compat registered via `DetailArmorBarCompat` at startup.
- **JEI**: Optional. Recipe viewing support (compileOnly + runtime).
- **Ponder/Flywheel**: Create dependencies included for build support.

## Data Component System

The mod uses NeoForge's Data Component system (1.20.5+) instead of NBT:

| Component | Type | Purpose |
|---|---|---|
| `ATTACHMENTS` | `AttachmentsData` | Map of slot type -> attachment ItemStack |
| `ATTACHMENT_STATE` | `Integer` | Unified state 0-3 |
| `ATTACHMENT_COOLDOWN` | `Long` | Tick when current phase ends |

## Sound System

17 custom sound events for attachment equips, activation, and entity effects. All stored as OGG files with a `sounds.json` definitions file.

## Configuration

- **Client config** (`protectionengineering-client.toml`): HUD overlay X/Y offset
- **Server config** (`protectionengineering-server.toml`): All active ability parameters (APS range/duration/cooldown, missile speed/range/lifetime, rocket cooldown, hormone cooldown, dodge strength/cooldown)

## Translation System

- English: Generated via `ProviderType.LANG` in data generation
- Chinese: Manually maintained in `assets/protectionengineering/lang/zh_cn.json`
- Key pattern: `item.protectionengineering.<name>`, `slot.protectionengineering.<name>`, `tooltip.protectionengineering.<name>`, etc.
