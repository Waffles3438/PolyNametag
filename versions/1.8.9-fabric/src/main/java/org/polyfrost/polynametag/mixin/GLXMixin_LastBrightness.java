package org.polyfrost.polynametag.mixin;

//#if FABRIC || MC > 1.12.2

import com.mojang.blaze3d.platform.GLX;
import org.polyfrost.polynametag.render.GlHelperHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GLX.class)
public class GLXMixin_LastBrightness {

    @Shadow public static int lightmapTextureUnit;

    @Inject(method = "gl13MultiTexCoord2f", at = @At("TAIL"))
    private static void setLastBrightness(int i, float f1, float f2, CallbackInfo ci) {
        if (i == lightmapTextureUnit) {
            GlHelperHook.INSTANCE.setLastBrightnessX(f1);
            GlHelperHook.INSTANCE.setLastBrightnessY(f2);
        }
    }
}
//#endif