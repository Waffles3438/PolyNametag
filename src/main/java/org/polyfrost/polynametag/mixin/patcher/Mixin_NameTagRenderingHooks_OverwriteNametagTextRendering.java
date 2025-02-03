package org.polyfrost.polynametag.mixin.patcher;

import net.minecraft.client.gui.FontRenderer;
import org.polyfrost.polynametag.NametagRenderer;
import org.polyfrost.polynametag.PolyNametagConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "club.sk1er.patcher.hooks.NameTagRenderingHooks")
public abstract class Mixin_NameTagRenderingHooks_OverwriteNametagTextRendering {

    @Dynamic("Patcher")
    @Inject(
        method = "drawNametagText",
        at = @At("HEAD"),
        remap = false,
        cancellable = true
    )
    private static void polyNametag$overwritePatcherDrawString(FontRenderer fontRenderer, String text, int x, int y, int color, CallbackInfoReturnable<Integer> cir) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        cir.setReturnValue(NametagRenderer.drawStringWithoutZFighting(fontRenderer, text, x, y, color));
    }

}
