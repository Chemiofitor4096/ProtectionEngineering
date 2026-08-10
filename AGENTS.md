# AGENTS.md

Forge 1.20.1 mod (Java 17 toolchain), built with **ModDevGradle LegacyForge** (`net.neoforged.moddev.legacyforge` — see `build.gradle`). Engineers' armor with installable attachments (passive immunities / attribute bonuses / active skills). **Create is a required dependency**; Registrate is the registration lib. All UI/communication in Chinese; code/comments/identifiers in English. Public methods need JavaDoc.

Read `CLAUDE.md` first — it is the authoritative, detailed dev guide (state machine, slots, repair, config, recipes, keys). `doc/API_GUIDE.md` and `doc/PROJECT_OVERVIEW.md` are secondary.

## Commands (no test suite exists)

- `gradlew runData` — regenerate `src/generated/resources` (recipes, models, en_us/en_ud lang). **Run after any recipe/model/lang change.**
- `gradlew runClient` — dev client.
- `gradlew build` — compile+package (produces the reobf'd production jar in `build/libs/`; the dev jar stays in `build/devlibs/`); the only "verify it compiles" step.
- `gradlew runServer`, `gradlew runGameTestServer` exist but no gametests are registered.

No `src/test` — do not invent a test framework.

## Build system (ModDevGradle LegacyForge)

- `build.gradle` uses the `net.neoforged.moddev.legacyforge` plugin; `legacyForge {}` holds version/runs/mods. Third-party mod jars go in `modImplementation`/`modRuntimeOnly`/`modCompileOnly` (auto-remapped, **non-transitive** — never plain `implementation` for mod jars).
- `META-INF/mods.toml` lives at `src/main/templates/META-INF/mods.toml` and is expanded by the `generateModMetadata` task (never edit `src/main/resources/META-INF/mods.toml` — it no longer exists).
- `reobfJar` is automatic: upload `build/libs/*.jar` to mod hosts, not `build/devlibs/`.

## Generated resources

`src/generated/resources` is baked into the build (`build.gradle`) **and committed**. Edit generators (`data/PEDataGen.java`, `PERecipeProvider`, `PEMechanicalCraftingRecipeGen`, Registrate providers), then run `runData`. Never hand-edit generated JSON except `zh_cn.json`.

## Lang duality

- **English**: keys live in `PEDataGen.java`, generated to `en_us.json`/`en_ud.json` via `runData`.
- **Chinese**: manually maintained in `src/main/resources/assets/protectionengineering/lang/zh_cn.json`. Adding an item = add EN key + `runData` **and** hand-write the `zh_cn` entry.

## Attachment state machine (most common mistake)

New attachments subclass `AttachmentItem` and return a `ControlPattern` (`FREE_TOGGLE` / `ACTIVE_COOLDOWN` / `ONE_SHOT_COOLDOWN` / `ALWAYS_ON` / `PASSIVE`).

- **Do NOT** override `onEquip` / `onActivatePress` / `onTick` for state transitions — the base class owns the state machine.
- Override lifecycle hooks instead: `onActivateOnce`, `onStateEnter`, `onStateExit`.
- If you must override `onTick`, call `super.onTick(...)` first.
- Attributes: declare via `getAttributeBonuses()` (use `IAttachment.bonus()` factory). Equipment slot groups are **auto-derived** from the actual host armor piece — never hand-write them.

## Mixin registration (previously broken in this repo)

A mixin is registered in **one** place: the `mixins` list in `src/main/resources/protectionengineering.mixins.json`.
- Production loading: jar manifest `MixinConfigs` attribute (set in `build.gradle` `jar { manifest }` — Forge 1.20.1 does not read `[[mixins]]` from mods.toml).
- Dev runs: ModDevGradle auto-injects `--mixin.config protectionengineering.mixins.json` and wires refmap generation (top-level `mixin {}` block in `build.gradle`; no MixinGradle plugin needed).

## Optional compats (guard before referencing)

Create is required, but these are optional and **guarded** by `ModList.get().isLoaded(...)`: `irons_spellbooks` (IronCompat + SchoolLiningItem), `playeranimator` (PlayerAnimCompat). Never reference their classes unguarded; build.gradle declares them `modCompileOnly` + `modRuntimeOnly` (mod* configs auto-remap SRG→official; they are non-transitive, so transitive deps like curios/geckolib/irons-lib are declared explicitly at runtime only).

## Repair system

Engineer armor, shield, and saw-sword accept **only** graded materials (20 units = full durability). New repairable items: implement `IGradedRepair` + override `isValidRepairItem` + call `appendRepairTooltip`. Register material→units in `PERepairMaterials` (may be config-driven).

## Other gotchas

- `ProtectionEngineering.REGISTRATE.defaultCreativeTab(...)` must be registered **before** `PEItems.init()`.
- Data storage is NBT-based (no Data Component system in 1.20.1): keys in `PEDataComponents` — `attachment_state` (Integer, 0-3), `attachment_cooldown` (Long), `attachments` (slot table on the host armor tag). `attachment_active` is deprecated — migration only, never write it.
- Strict client/server split: HUD, keybindings (`client/PEKeyBindings.java`), overlays are `@OnlyIn` client. Config: `PEConfig` (client) / `PEServerConfig` (server), both runtime-reloadable.
- Damage reduction is multiplicative and clamped to 100%; use `DamageTypeTags` (`IS_EXPLOSION`/`IS_FIRE`) for linings, not hardcoded damage type lists.
- **Jump strength**: 1.20.1 has **no** vanilla player jump attribute (`Attributes.JUMP_STRENGTH` is the horse's, and `LivingEntity#getJumpPower` hardcodes 0.42F). We register our own `protectionengineering:jump_strength` (`PEAttributes`, default 0.42, `setSyncable(true)`, attached to players via `EntityAttributeModificationEvent`) and consume it in `LivingEntityMixin` with `@ModifyConstant(getJumpPower, 0.42F)` — **not** a HEAD injection, because `Entity#getBlockJumpFactor` is `protected` in 1.20.1. Null-check the attribute instance so horses stay vanilla.
- **Armor texture path**: Forge 1.20.1's `HumanoidArmorLayer` always resolves + loads the material texture (even when the model is hidden) and defaults to the `minecraft:` namespace → WARN spam. Override `getArmorTexture` on the armor item (`IForgeItem`) to point at `protectionengineering:textures/models/armor/engineer_layer_*.png` (transparent placeholders; actual 3D model is `PEArmorLayer`).
