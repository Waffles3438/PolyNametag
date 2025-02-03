package org.polyfrost.polynametag.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import org.polyfrost.polynametag.NametagRenderer;
import org.polyfrost.polynametag.PolyNametagConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public class Mixin_RendererLivingEntity_ReplaceRendering<T extends EntityLivingBase> {

    @WrapOperation(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    private int polynametag$switchTextRendering(FontRenderer instance, String text, int x, int y, int color, Operation<Integer> original) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return original.call(instance, text, x, y, color);
        }

        return NametagRenderer.drawStringWithoutZFighting(instance, text, x, y, color);
    }

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true)
    private void polynametag$checkInventory(T entity, double x, double y, double z, CallbackInfo ci) {
        if (PolyNametagConfig.INSTANCE.getEnabled() && !PolyNametagConfig.INSTANCE.getShowInInventory() && Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
            ci.cancel();
        }
    }

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private void polynametag$replaceDefaultBackgroundRendering(T entity, double x, double y, double z, CallbackInfo ci) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        NametagRenderer.drawBackground(entity);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    private void polynametag$cancelDefaultBackgroundRendering$begin(WorldRenderer instance, int glMode, VertexFormat format) {
        if (PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        instance.begin(glMode, format);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;pos(DDD)Lnet/minecraft/client/renderer/WorldRenderer;"))
    private WorldRenderer polynametag$cancelDefaultBackgroundRendering$pos(WorldRenderer instance, double x, double y, double z) {
        if (PolyNametagConfig.INSTANCE.getEnabled()) {
            return instance;
        }

        return instance.pos(x, y, z);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;color(FFFF)Lnet/minecraft/client/renderer/WorldRenderer;"))
    private WorldRenderer polynametag$cancelDefaultBackgroundRendering$color(WorldRenderer instance, float red, float green, float blue, float alpha) {
        if (PolyNametagConfig.INSTANCE.getEnabled()) {
            return instance;
        }

        return instance.color(red, green, blue, alpha);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/WorldRenderer;endVertex()V"))
    private void polynametag$cancelDefaultBackgroundRendering$endVertex(WorldRenderer instance) {
        if (PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        instance.endVertex();
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private void polynametag$cancelDefaultBackgroundRendering(Tessellator instance) {
        if (PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        instance.draw();
    }

    @ModifyArg(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0), index = 1)
    private float polynametag$modifyTranslateY(float y) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return y;
        }

        float newY = y + PolyNametagConfig.INSTANCE.getHeightOffset();
        System.out.println("New Y: " + newY);
        return newY;
    }

}
