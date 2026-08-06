package com.chemiofitor.protection_engineering.registry;

import com.chemiofitor.protection_engineering.block.ArmorEmitterBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;

import static com.chemiofitor.protection_engineering.ProtectionEngineering.REGISTRATE;

/**
 * 护甲发射器注册 — 压力板式功能方块。
 * <p>
 * 方块本身是机械臂（Mechanical Arm）的交互点：机械臂递送的护甲/附件会被自动穿戴到
 * 站在上方的生物。不能穿戴时交互点 {@code insert} 返回原样，机械臂不选择该点。
 * <p>
 * tooltip 参考机械动力风格：summary（蓝色主描述）+ condition/behaviour（条件与行为）。
 */
public class PEArmorEmitter {
    public static final BlockEntry<ArmorEmitterBlock> ARMOR_EMITTER = REGISTRATE
            .block("armor_emitter", ArmorEmitterBlock::new)
            .properties(p -> p.strength(2.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                    prov.models().getExistingFile(ctx.getId())))
            .simpleItem()
            .register();

    public static void init() {}
}
