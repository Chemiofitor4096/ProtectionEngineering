# Protection Engineering (1.21.1) -- API Guide

## Package Overview

| Package | Purpose |
|---|---|
| `com.chemiofitor.protection_engineering` | Main mod class and client setup |
| `com.chemiofitor.protection_engineering.api` | Public API: attachment/host interfaces, slot types, data component |
| `com.chemiofitor.protection_engineering.block` | Workbench block and block entity |
| `com.chemiofitor.protection_engineering.client` | Keybindings, HUD overlays, model layers, entity renderers |
| `com.chemiofitor.protection_engineering.client.layer` | `PEArmorLayer` -- 3D armor + attachment rendering on player |
| `com.chemiofitor.protection_engineering.client.model` | 33 Blockbench-exported model classes |
| `com.chemiofitor.protection_engineering.client.renderer` | Missile and rocket entity renderers |
| `com.chemiofitor.protection_engineering.compat` | Detail Armor Bar Reconstructed compatibility |
| `com.chemiofitor.protection_engineering.config` | Client and server configuration specs |
| `com.chemiofitor.protection_engineering.data` | Data generation entry point and recipe providers |
| `com.chemiofitor.protection_engineering.entity` | Custom entity classes (missile, rocket, thrust) |
| `com.chemiofitor.protection_engineering.event` | Game event handlers and network event registration |
| `com.chemiofitor.protection_engineering.item` | All item classes (32 total) |
| `com.chemiofitor.protection_engineering.menu` | Workbench GUI container and screen |
| `com.chemiofitor.protection_engineering.mixin` | Damage cap, shield behavior, slip/snow walking |
| `com.chemiofitor.protection_engineering.network` | Custom network payloads (toggle, thrust) |
| `com.chemiofitor.protection_engineering.registry` | All deferred registers (data components, items, entities, sounds, armor materials) |

## Core Concepts

**Attachment** -- An item installable into an engineer armor slot, providing passive immunities, attribute bonuses, or active skills.

**Host** -- An armor item implementing `IAttachmentHost`, carrying attachment data.

**Slot** -- A specific position on an armor piece. Each armor piece has a fixed set of slots, each accepting one attachment.

**Control Pattern** -- The state machine behavior that governs how an attachment transitions between states.

## Slot Types

All slots are defined in `SlotTypes` and identified by `ResourceLocation`:

| Constant | ID | Armor Piece |
|---|---|---|
| `EYES` | `protectionengineering:eyes` | Hood |
| `MOUTH` | `protectionengineering:mouth` | Hood |
| `SHOULDER` | `protectionengineering:shoulder` | Chestplate |
| `CHESTPLATE` | `protectionengineering:chestplate` | Chestplate |
| `BACK` | `protectionengineering:back` | Chestplate |
| `ARM` | `protectionengineering:arm` | Chestplate |
| `LEG` | `protectionengineering:leg` | Leggings |
| `KNEE` | `protectionengineering:knee` | Leggings |
| `FOOT` | `protectionengineering:foot` | Boots |

### Custom Slots

New slots can be registered at runtime:

```java
public static final SlotType MY_SLOT = SlotType.register(
    ResourceLocation.fromNamespaceAndPath("yourmod", "my_slot"),
    SlotType.SlotCategory.ARMOR
);
```

---

## Creating Attachments

### Passive Attachment (Immunity Only)

Use `SimpleAttachmentItem` directly:

```java
// In your registration class
public static final ItemEntry<SimpleAttachmentItem> MY_GADGET = REGISTRATE
    .item("my_gadget", p -> new SimpleAttachmentItem(p,
        Set.of(MobEffects.POISON),       // immunities
        SlotTypes.MOUTH))                // compatible slot
    .properties(p -> p.stacksTo(1))
    .register();
```

### Passive With Attribute Modifiers

```java
public class MyPlateItem extends AttachmentItem {
    private static final UUID ARMOR_MODIFIER = UUID.fromString("...");

    public MyPlateItem(Properties p) { super(p, SlotTypes.CHESTPLATE); }

    @Override
    public ControlPattern getControlPattern() { return ControlPattern.PASSIVE; }

    @Override
    public void addAttributeModifiers(ItemStack stack, ItemStack armor,
                                       SlotType slot, LivingEntity entity,
                                       AttributeMap attributes) {
        attributes.addTransientAttributeModifier(
            Attributes.ARMOR,
            new AttributeModifier(ARMOR_MODIFIER, "My plate bonus", 2.0,
                AttributeModifier.Operation.ADD_VALUE)
        );
    }

    @Override
    public float getDamageReduction() { return 0.10f; }
}
```

### Toggleable Attachment (FREE_TOGGLE)

```java
public class MyToggleItem extends AttachmentItem {
    public MyToggleItem(Properties p) { super(p, SlotTypes.EYES); }

    @Override
    public ControlPattern getControlPattern() {
        return ControlPattern.FREE_TOGGLE;
    }

    @Override
    protected void onStateEnter(ItemStack stack, int newState, LivingEntity e) {
        if (newState == STATE_READY) {
            // Enable effect
            e.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,
                MobEffectInstance.INFINITE_DURATION, 0, false, false));
        }
    }

    @Override
    protected void onStateExit(ItemStack stack, int oldState, LivingEntity e) {
        if (oldState == STATE_READY) {
            // Disable effect
            e.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
```

### One-Shot With Cooldown (ONE_SHOT_COOLDOWN)

```java
public class MyGrenadeItem extends AttachmentItem {
    private static final long COOLDOWN = 600;  // 30 seconds

    public MyGrenadeItem(Properties p) { super(p, SlotTypes.ARM); }

    @Override
    public ControlPattern getControlPattern() {
        return ControlPattern.ONE_SHOT_COOLDOWN;
    }

    @Override
    public long getCooldownDuration() { return COOLDOWN; }

    @Override
    protected void onActivateOnce(ItemStack stack, ItemStack host, LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            // Spawn projectile, apply area effect, etc.
            entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(),
                3.0f, Level.ExplosionInteraction.NONE);
        }
    }
}
```

### Active Window With Cooldown (ACTIVE_COOLDOWN)

```java
public class MyBeamItem extends AttachmentItem {
    private static final long ACTIVE = 100;    // 5 seconds
    private static final long COOLDOWN = 400;  // 20 seconds

    public MyBeamItem(Properties p) { super(p, SlotTypes.SHOULDER); }

    @Override
    public ControlPattern getControlPattern() {
        return ControlPattern.ACTIVE_COOLDOWN;
    }

    @Override
    public long getActiveDuration() { return ACTIVE; }

    @Override
    public long getCooldownDuration() { return COOLDOWN; }

    @Override
    public void onTick(ItemStack stack, ItemStack host, LivingEntity entity, SlotType slot) {
        super.onTick(stack, host, entity, slot);  // MUST call super
        if (!isInActiveWindow(stack)) return;
        // Per-tick logic during active window only
        // Spawn particles, damage entities within range, etc.
    }
}
```

## Control Pattern Reference

| Pattern | State Flow | Key Press Behavior | Examples |
|---|---|---|---|
| `FREE_TOGGLE` | DISABLED <--> READY | Toggle on/off | Night Vision, Spyglass |
| `ACTIVE_COOLDOWN` | READY -> ACTIVE -> COOLING -> READY | Activate from READY | APS, Advanced APS |
| `ONE_SHOT_COOLDOWN` | READY -> COOLING -> READY | Fire once, then cooldown | Hormone, Missile, Rocket, Dodge |
| `ALWAYS_ON` | Always READY | No toggle action | Jetpack, Momentum Jetpack |
| `PASSIVE` | No state | No toggle action | Plates, Exoskeletons, Soles |

## State Machine

### State Constants

```
STATE_DISABLED  = 0  -- Not active, can be toggled on
STATE_READY     = 1  -- Active and ready for use
STATE_ACTIVE    = 2  -- Within the active ability window
STATE_COOLING   = 3  -- Cooling down after use
```

### Data Components

| Component | Type | Purpose |
|---|---|---|
| `ATTACHMENT_STATE` | `Integer` | Current state (0-3) |
| `ATTACHMENT_COOLDOWN` | `Long` | Tick at which current phase ends (0 = no timer) |
| `ATTACHMENTS` | `AttachmentsData` | Map of slot -> attachment on the armor |
| `ATTACHMENT_ACTIVE` | `Boolean` | Deprecated -- used only for legacy migration |

### Base Class Methods

```java
// State read/write
int getState(ItemStack)
void setState(ItemStack, int)

// Timer
long getTimer(ItemStack)
void setTimer(ItemStack, long)

// Queries
boolean isActive(ItemStack)          // READY or ACTIVE
boolean isInActiveWindow(ItemStack)  // Only during ACTIVE state

// Hooks (override in subclasses)
void onActivateOnce(ItemStack, ItemStack, LivingEntity)  // One-shot trigger
void onStateEnter(ItemStack, int newState, LivingEntity) // Entering state
void onStateExit(ItemStack, int oldState, LivingEntity)  // Exiting state
```

## Damage Reduction and Immunity System

### Override Points

```java
@Override
public float getDamageReduction() { return 0.10f; }        // General damage reduction

@Override
public float getFallDamageReduction() { return 0.20f; }    // Fall damage multiplier

@Override
public float getFallDistanceReduction() { return 1.0f; }   // Fall distance reduction (blocks)

@Override
public Set<Holder<MobEffect>> getImmunities() {
    return Set.of(MobEffects.POISON, MobEffects.WITHER);
}
```

### Stacking Rules

All damage reductions and distance reductions from all attachments stack additively with no upper limit. The final damage is clamped to 100% reduction (preventing negative damage). Processing is done in `PEGameEvents` and `LivingEntityMixin`.

## HUD Overlay System

The HUD overlay is handled by `PECooldownOverlay`. It reads `ATTACHMENT_STATE` and `ATTACHMENT_COOLDOWN` from each attachment and displays:

| State | Display | Color |
|---|---|---|
| READY | Solid circle | Green `#55FF55` |
| ACTIVE | Lightning bolt + countdown | Cyan `#55FFFF` |
| COOLING | Hourglass + countdown | Gray -> Yellow -> Red |
| DISABLED | Empty circle | Gray |
| Creative mode | All symbols | Purple |

## Key Bindings

Seven key mappings are defined in `PEKeyBindings`:

| Key | Constant | Target |
|---|---|---|
| N | `TOGGLE_NIGHT_VISION` | NightVisionGogglesItem |
| H | `ACTIVATE_HORMONE` | HormoneInjectorItem |
| J | `THRUST_JETPACK` | JetpackItem / MomentumJetpackItem |
| K | `TOGGLE_APS` | ApsItem |
| R | `ACTIVATE_ROCKET_LAUNCHER` | RocketLauncherItem |
| G | `ACTIVATE_MISSILE` | MissilePackItem |
| Z | `TOGGLE_SPYGLASS` | SpyglassItem |

Usage pattern:

```java
// Add new keybinding
public static final KeyMapping MY_KEY = new KeyMapping(
    "key.protectionengineering.my_key",
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM,
    InputConstants.KEY_M,
    "key.categories.protectionengineering"
);

// In client tick handler
while (MY_KEY.consumeClick()) {
    tryActivateAttachment(MyAttachmentClass.class);
}
```

## Configuration Reference

### Client Config (`protectionengineering-client.toml`)

| Key | Default | Range | Description |
|---|---|---|---|
| `hudOffsetX` | 4 | 0-500 | HUD overlay X offset from right edge |
| `hudOffsetY` | 4 | 0-500 | HUD overlay Y offset from top |

### Server Config (`protectionengineering-server.toml`)

| Category | Key | Default | Range | Description |
|---|---|---|---|---|
| aps | interceptRange | 5.0 | 1-32 | APS projectile interception range |
| aps | activeDuration | 200 | 20-1200 | APS active window in ticks |
| aps | cooldownTicks | 600 | 20-3600 | APS cooldown in ticks |
| missile | targetRange | 512 | 20-1024 | Missile lock-on range |
| missile | flightSpeed | 3.0 | 1-10 | Missile flight speed |
| missile | closeSpeed | 4.5 | 1-15 | Missile terminal approach speed |
| missile | maxLife | 200 | 40-600 | Missile maximum lifetime in ticks |
| rocket | cooldownTicks | 200 | 20-3600 | Rocket launcher cooldown |
| hormone | cooldownTicks | 1200 | 20-7200 | Hormone injector cooldown |
| dodge | dodgeStrength | 1.0 | 0.2-10 | Dodge jetpack push strength |
| dodge | cooldownTicks | 200 | 20-3600 | Dodge jetpack cooldown |

## 3D Model Integration

### Model Registration

Each attachment with a 3D model must:

1. Export a Java model from Blockbench and place it in `client/model/`
2. Override the model/texture provider methods in the item class
3. Register the model layer in `PEModelLayers`

### Model Provider Methods

```java
@Override
public ModelPart createAttachmentModel() {
    return modelSet.bakeLayer(MyModel.LAYER_LOCATION);
}

@Override
public ResourceLocation getAttachmentTexture() {
    return ResourceLocation.fromNamespaceAndPath(
        "protectionengineering",
        "textures/models/armor/my_attachment.png"
    );
}

// For arm/leg attachments, override the left/right variants:
@Override
public ModelPart createLeftArmModel() { ... }
@Override
public ModelPart createRightArmModel() { ... }
@Override
public ResourceLocation getLeftArmTexture() { ... }
@Override
public ResourceLocation getRightArmTexture() { ... }
```

### Layer Registration

```java
// In PEModelLayers
public static final ModelLayerLocation MY_GADGET = new ModelLayerLocation(
    ResourceLocation.fromNamespaceAndPath(MOD_ID, "my_gadget"), "main"
);

public static void registerLayerDefinitions(RegisterLayerDefinitionsEvent event) {
    event.registerLayerDefinition(MY_GADGET, MyModel::createBodyLayer);
}
```

## Recipe Registration

### Vanilla Shaped Recipe

```java
// In PERecipeProvider
ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, PEItems.MY_GADGET.get())
    .pattern("ABA")
    .pattern("CDC")
    .define('A', Items.IRON_INGOT)
    .define('B', AllItems.STURDY_SHEET.get())
    .define('C', AllItems.BRASS_SHEET.get())
    .define('D', AllItems.PRECISION_MECHANISM.get())
    .unlockedBy("has_sturdy", has(AllItems.STURDY_SHEET.get()))
    .save(output);
```

### Create Mechanical Crafting Recipe

```java
// In PEMechanicalCraftingRecipeGen
MechanicalCraftingRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
    .patternLine("AAA")
    .patternLine("BCD")
    .patternLine("EFG")
    .key('A', ingredient)
    .key('B', ingredient)
    .build(output);
```

## Network System

### Toggle Payload (Client -> Server)

```java
// Record with armorIndex and slotTypeId
public record ToggleAttachmentPayload(int armorIndex, ResourceLocation slotTypeId) {
    public static final CustomPacketPayload.Type<ToggleAttachmentPayload> TYPE =
        new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "toggle_attachment")
        );

    public static final StreamCodec<FriendlyByteBuf, ToggleAttachmentPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ToggleAttachmentPayload::armorIndex,
            ResourceLocation.STREAM_CODEC, ToggleAttachmentPayload::slotTypeId,
            ToggleAttachmentPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
```

### Server Handling

```java
// In PENetworkEvents
@SubscribeEvent
public static void registerPayloads(RegisterPayloadHandlersEvent event) {
    event.registrar("1.0")
        .playToServer(
            ToggleAttachmentPayload.TYPE,
            ToggleAttachmentPayload.STREAM_CODEC,
            (payload, context) -> {
                // Look up armor, find attachment, call onActivatePress()
            }
        );
}
```

## Integration Checklist

When adding a new attachment or feature:

- [ ] **Item class**: Extend `AttachmentItem` (or `SimpleAttachmentItem` for passives)
- [ ] **Control pattern**: Implement `getControlPattern()` and any duration methods
- [ ] **Lifecycle hooks**: Override `onActivateOnce`, `onStateEnter`, `onStateExit`
- [ ] **Per-tick logic**: Override `onTick()` with `super.onTick()` call
- [ ] **Immunities**: Pass `Set.of(...)` in constructor for effect immunities
- [ ] **Combat modifiers**: Override `getDamageReduction()`, `getFallDamageReduction()`, `getFallDistanceReduction()`
- [ ] **Attributes**: Override `addAttributeModifiers()` for attribute bonuses
- [ ] **Registration**: Add to `PEItems` via Registrate
- [ ] **3D model**: Create Blockbench model, override model/texture providers
- [ ] **Model layer**: Register in `PEModelLayers`
- [ ] **Translations**: Add English in `PEDataGen.java`, Chinese in `zh_cn.json`
- [ ] **Recipes**: Add to `PERecipeProvider` or `PEMechanicalCraftingRecipeGen`
- [ ] **Server config**: Add entries to `PEServerConfig` for tunable parameters
- [ ] **Game events**: Add handler in `PEGameEvents` if custom damage/interaction needed
- [ ] **Sound**: Add sound event in `PESounds` and OGG file if custom audio needed
- [ ] **Keybind**: Register key mapping in `PEKeyBindings` if active ability
