package org.polyfrost.polynametag.mixin.essential;

import gg.essential.universal.UMatrixStack;
import org.polyfrost.polynametag.NametagRenderer;
import org.polyfrost.polynametag.PolyNametagConfig;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Pseudo
@Mixin(targets = "gg.essential.handlers.OnlineIndicator")
public class Mixin_OnlineIndicator_EssentialIndicatorCompatibility {

    @Dynamic("Essential")
    @ModifyArgs(
        method = "drawNametagIndicator",
        at = @At(
            remap = false,
            value = "INVOKE",
            target = "Lgg/essential/render/TextRenderTypeVertexConsumer;color(IIII)Lgg/essential/render/TextRenderTypeVertexConsumer;"
        )
    )
    private static void polyNametag$modifyNametagColor(Args args) {
        if (!PolyNametagConfig.INSTANCE.getEnabled()) {
            return;
        }

        args.set(3, 0);
    }

    @Dynamic("Essential")
    @Inject(method = "drawNametagIndicator", at = @At("HEAD"), cancellable = true)
    private static void skip(UMatrixStack matrixStack, Entity entity, String str, int light, CallbackInfo ci) {
        if (!PolyNametagConfig.INSTANCE.getEnabled() || !PolyNametag.INSTANCE.getDrawingIndicator()) {
            return;
        }

        ci.cancel();
    }

}
