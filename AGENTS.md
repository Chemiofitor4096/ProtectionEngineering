# AGENTS.md

NeoForge 1.21.1 mod (Java 21 toolchain). Engineers' armor with installable attachments (passive immunities / attribute bonuses / active skills). **Create is a required dependency**; Registrate is the registration lib. All UI/communication in Chinese; code/comments/identifiers in English. Public methods need JavaDoc.

Read `CLAUDE.md` first — it is the authoritative, detailed dev guide (state machine, slots, repair, config, recipes, keys). `doc/API_GUIDE.md` and `doc/PROJECT_OVERVIEW.md` are secondary.

## Commands (no test suite exists)

- `gradlew runData` — regenerate `src/generated/resources` (recipes, models, en_us/en_ud lang). **Run after any recipe/model/lang change.**
- `gradlew runClient` — dev client.
- `gradlew build` — compile+package; the only "verify it compiles" step.
- `gradlew runServer`, `gradlew runGameTestServer` exist but no gametests are registered.

No `src/test` — do not invent a test framework.

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

A mixin must be added in **two** places or it silently never loads:
1. `src/main/resources/protectionengineering.mixins.json`
2. `[[mixins]]` block in `src/main/templates/META-INF/neoforge.mods.toml`

## Optional compats (guard before referencing)

Create is required, but these are optional and **guarded** by `ModList.get().isLoaded(...)`: `detailab` (DetailArmorBarCompat), `irons_spellbooks` (IronCompat + SchoolLiningItem), playeranimator (PlayerAnimCompat), JEI. Never reference their classes unguarded; build.gradle declares them `compileOnly` + `runtimeOnly`.

## Repair system

Engineer armor, shield, and saw-sword accept **only** graded materials (20 units = full durability). New repairable items: implement `IGradedRepair` + override `isValidRepairItem` + call `appendRepairTooltip`. Register material→units in `PERepairMaterials` (may be config-driven).

## Other gotchas

- `ProtectionEngineering.REGISTRATE.defaultCreativeTab(...)` must be registered **before** `PEItems.init()`.
- Data components: `ATTACHMENT_STATE` (Integer, 0-3), `ATTACHMENT_COOLDOWN` (Long). `ATTACHMENT_ACTIVE` is deprecated — migration only, never write it.
- Strict client/server split: HUD, keybindings (`client/PEKeyBindings.java`), overlays are `@OnlyIn` client. Config: `PEConfig` (client) / `PEServerConfig` (server), both runtime-reloadable.
- Damage reduction is multiplicative and clamped to 100%; use `DamageTypeTags` (`IS_EXPLOSION`/`IS_FIRE`) for linings, not hardcoded damage type lists.
