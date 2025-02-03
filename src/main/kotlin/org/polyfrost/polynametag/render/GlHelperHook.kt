package org.polyfrost.polynametag.render

import net.minecraft.client.renderer.OpenGlHelper

object GlHelperHook {
    var lastBrightnessX
    //#if MC<=11202 && FORGE
        get() = OpenGlHelper.lastBrightnessX
        set(value) {
            OpenGlHelper.lastBrightnessX = value
        }
    //#else
    //$$ = 0F
    //#endif
    var lastBrightnessY
    //#if MC<=11202 && FORGE
        get() = OpenGlHelper.lastBrightnessY
        set(value) {
            OpenGlHelper.lastBrightnessY = value
        }
    //#else
    //$$ = 0F
    //#endif
}