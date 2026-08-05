package com.chemiofitor.protection_engineering.client;

import com.chemiofitor.protection_engineering.api.IAttachment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 客户端附件模型注册表 — 将附件物品类映射到其 3D 模型供应器。
 * <p>
 * 此类仅存在于客户端包中，服务端绝不会加载。
 * 所有条目在客户端初始化时通过 {@link PEAttachmentModelSetup} 注册。
 * <p>
 * 与直接在 IAttachment 上声明模型方法的设计不同，此注册表完全避免了
 * EntityModel/EntityModelSet 出现在任何会被服务端加载的类文件中。
 */
public final class PEAttachmentModelRegistry {

    private PEAttachmentModelRegistry() {}

    /** 主模型 + 纹理供应器（安装在头部/身体上的附件） */
    @FunctionalInterface
    public interface MainModelProvider {
        EntityModel<?> create(EntityModelSet modelSet);
    }

    /** 肢体模型 + 纹理供应器（用于手臂/腿部附件） */
    @FunctionalInterface
    public interface LimbModelProvider {
        /** 左肢模型 */
        EntityModel<?> createLeft(EntityModelSet modelSet);
        /** 右肢模型，返回 null 表示与左侧共用（由渲染层 X 轴镜像） */
        @Nullable
        default EntityModel<?> createRight(EntityModelSet modelSet) { return null; }
    }

    private static final Map<Class<?>, MainModelProvider> MAIN_MODELS   = new IdentityHashMap<>();
    private static final Map<Class<?>, ResourceLocation>  MAIN_TEXTURES = new IdentityHashMap<>();

    private static final Map<Class<?>, LimbModelProvider> ARM_MODELS    = new IdentityHashMap<>();
    private static final Map<Class<?>, ResourceLocation>  ARM_TEXTURES  = new IdentityHashMap<>();

    private static final Map<Class<?>, LimbModelProvider> LEG_MODELS    = new IdentityHashMap<>();
    private static final Map<Class<?>, ResourceLocation>  LEG_TEXTURES  = new IdentityHashMap<>();

    // ── 注册方法 ──────────────────────────────────────────────

    public static void registerMain(Class<?> itemClass, MainModelProvider provider, ResourceLocation texture) {
        MAIN_MODELS.put(itemClass, provider);
        MAIN_TEXTURES.put(itemClass, texture);
    }

    public static void registerArm(Class<?> itemClass, LimbModelProvider provider, ResourceLocation texture) {
        ARM_MODELS.put(itemClass, provider);
        ARM_TEXTURES.put(itemClass, texture);
    }

    public static void registerLeg(Class<?> itemClass, LimbModelProvider provider, ResourceLocation texture) {
        LEG_MODELS.put(itemClass, provider);
        LEG_TEXTURES.put(itemClass, texture);
    }

    // ── 查询方法（由 PEArmorLayer 调用）─────────────────────

    @Nullable
    public static EntityModel<?> createMainModel(IAttachment attachment, EntityModelSet modelSet) {
        var p = MAIN_MODELS.get(attachment.getClass());
        return p != null ? p.create(modelSet) : null;
    }

    @Nullable
    public static ResourceLocation getMainTexture(IAttachment attachment) {
        return MAIN_TEXTURES.get(attachment.getClass());
    }

    @Nullable
    public static EntityModel<?> createLeftArmModel(IAttachment attachment, EntityModelSet modelSet) {
        var p = ARM_MODELS.get(attachment.getClass());
        return p != null ? p.createLeft(modelSet) : null;
    }

    @Nullable
    public static EntityModel<?> createRightArmModel(IAttachment attachment, EntityModelSet modelSet) {
        var p = ARM_MODELS.get(attachment.getClass());
        if (p == null) return null;
        var right = p.createRight(modelSet);
        return right != null ? right : p.createLeft(modelSet); // fallback to left
    }

    @Nullable
    public static ResourceLocation getArmTexture(IAttachment attachment) {
        return ARM_TEXTURES.get(attachment.getClass());
    }

    @Nullable
    public static EntityModel<?> createLeftLegModel(IAttachment attachment, EntityModelSet modelSet) {
        var p = LEG_MODELS.get(attachment.getClass());
        return p != null ? p.createLeft(modelSet) : null;
    }

    /**
     * 右腿模型。返回 {@code null} 表示无专用右腿模型 —
     * 由渲染层沿用左腿模型并沿 X 轴镜像。
     * 注意：这里 <b>不能</b> fallback 到 {@code createLeft}，否则渲染层
     * 无法识别"应镜像"的语义，会把左腿几何原样画到右腿（渲染到同侧）。
     */
    @Nullable
    public static EntityModel<?> createRightLegModel(IAttachment attachment, EntityModelSet modelSet) {
        var p = LEG_MODELS.get(attachment.getClass());
        if (p == null) return null;
        return p.createRight(modelSet);
    }

    @Nullable
    public static ResourceLocation getLegTexture(IAttachment attachment) {
        return LEG_TEXTURES.get(attachment.getClass());
    }
}
