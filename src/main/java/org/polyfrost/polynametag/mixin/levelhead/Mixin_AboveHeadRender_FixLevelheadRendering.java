package org.polyfrost.polynametag.mixin.levelhead;

import club.sk1er.mods.levelhead.display.LevelheadTag;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.EntityPlayer;
import org.polyfrost.polynametag.NametagRenderer;
import org.polyfrost.polynametag.PolyNametagConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Taken from Patcher under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * https://github.com/Sk1erLLC/Patcher/blob/master/LICENSE.md
 */
@Pseudo
@Mixin(targets = "club.sk1er.mods.levelhead.render.AboveHeadRender", priority = 1001, remap = false)
public abstract class Mixin_AboveHeadRender_FixLevelheadRendering {

    @Dynamic("Levelhead")
    @WrapOperation(
        method = "render(Lnet/minecraft/client/gui/FontRenderer;Lclub/sk1er/mods/levelhead/display/LevelheadTag$LevelheadComponent;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I",
            remap = true
        )
    )
    private int polynametag$fixStringRendering(@Coerce Object instance, Operation<Integer> original, String text, int x, int y, int color) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return original.call(instance);
        }

        return NametagRenderer.drawNametagString(text, x, y, color);
    }

    @Dynamic("Levelhead")
    @Inject(method = "renderName", at = @At(value = "INVOKE", target = "Lgg/essential/universal/UGraphics;drawDirect()V", shift = At.Shift.AFTER))
    private void polynametag$fixBackgroundRendering(LevelheadTag tag, EntityPlayer entityIn, double x, double y, double z, CallbackInfo ci) {
        // TODO
//        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
//            return;
//        }
//
//        int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tag.getString()) / 2;
//        NametagRenderingKt.drawFrontBackground(-stringWidth - 2, stringWidth + 1, PolyNametagConfig.INSTANCE.getBackgroundColor().red(), PolyNametagConfig.INSTANCE.getBackgroundColor().green(), PolyNametagConfig.INSTANCE.getBackgroundColor().blue(), NametagRenderingKt.getBackBackgroundAlpha(), entityIn);
//        GlStateManager.enableDepth();
//        NametagRenderingKt.setDrawingWithDepth(true);
//        NametagRenderingKt.drawFrontBackground(-stringWidth - 2, stringWidth + 1, entityIn);
//        GlStateManager.depthMask(true);
    }

}
