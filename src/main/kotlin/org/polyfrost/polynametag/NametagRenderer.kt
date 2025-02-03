package org.polyfrost.polynametag

import gg.essential.universal.UMatrixStack
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.polynametag.mixin.Accessor_FontRenderer_DrawString
import org.polyfrost.polynametag.render.EssentialBSManager
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.universal.UGraphics
import kotlin.math.cos
import kotlin.math.sin

object NametagRenderer {

    private val points = arrayOf(Vec2(1f, 1f), Vec2(1f, -1f), Vec2(-1f, -1f), Vec2(-1f, 1f))
    private val translate = arrayOf(Vec2(1f, 0f), Vec2(0f, -1f), Vec2(-1f, 0f), Vec2(0f, 1f))

    var isDrawingIndicator = false
    private val essentialBSManager = EssentialBSManager()

    @JvmStatic
    fun drawStringWithoutZFighting(fontRenderer: FontRenderer, text: String, x: Float, y: Float, color: Int): Int {
        if (fontRenderer !is Accessor_FontRenderer_DrawString) {
            return 0
        }

        UGraphics.GL.pushMatrix()
        UGraphics.GL.translate(0f, 0f, -0.01f)
        return when (PolyNametagConfig.textType) {
            0 -> fontRenderer.invokeRenderString(text, x, y, color, false)
            1 -> fontRenderer.invokeRenderString(text, x, y, color, true)
            else -> 0
        }.apply {
            UGraphics.GL.popMatrix()
        }
    }

    @JvmStatic
    fun drawStringWithoutZFighting(text: String, x: Float, y: Float, color: Int): Int {
        return drawStringWithoutZFighting(mc.fontRendererObj, text, x, y, color)
    }

    @JvmStatic
    fun drawBackground(x1: Double, x2: Double, entity: Entity) {
        if (!PolyNametagConfig.background) {
            return
        }

        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        UGraphics.disableTexture2D()
        GL11.glPushMatrix()
        val realX1 = x1 - if (canDrawEssentialIndicator(entity)) 10 else 0
        GL11.glTranslated((realX1 + x2) / 2f, 3.5, 0.01)
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        with(PolyNametagConfig.backgroundColor) {
            GL11.glColor4f(r / 255f, g / 255f, b / 255f, a.coerceAtMost(63) / 255f)
        }

        val halfWidth = (x2 - realX1) / 2f + PolyNametagConfig.paddingX
        val radius = if (PolyNametagConfig.rounded) PolyNametagConfig.cornerRadius.coerceAtMost(4.5f + PolyNametagConfig.paddingY).coerceAtMost(halfWidth.toFloat()) else 0f
        val width = halfWidth - radius
        val distanceFromPlayer = entity.getDistanceToEntity(mc.thePlayer)
        val quality = ((distanceFromPlayer * 4 + 10).coerceAtMost(350f) / 4f).toInt()
        for (a in 0..3) {
            val (transX, transY) = translate[a]
            val (pointX, pointY) = points[a]
            val x = pointX * width
            val y = pointY * (4.5 + PolyNametagConfig.paddingY - radius)
            if (PolyNametagConfig.rounded) {
                for (b in 0 until 90 / quality) {
                    val angle = Math.toRadians((a * 90 + b * quality).toDouble())
                    GL11.glVertex2d(x + sin(angle) * radius, y + cos(angle) * radius)
                }
            } else {
                GL11.glVertex2d(x, y)
            }
        }

        GL11.glEnd()
        GL11.glPopMatrix()
        UGraphics.enableTexture2D()
        GL11.glColor4f(1f, 1f, 1f, 1f)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
    }

    @JvmStatic
    fun drawBackground(entity: Entity) {
        val halfWidth = mc.fontRendererObj.getStringWidth(entity.displayName.formattedText) / 2 + 1.0
        drawBackground(-halfWidth, halfWidth, entity)
    }

    fun drawIndicator(entity: Entity, string: String, light: Int) {
        if (entity !is AbstractClientPlayer) return
        isDrawingIndicator = true
        essentialBSManager.drawIndicator(UMatrixStack(), entity, string, light)
        isDrawingIndicator = false
    }

    @JvmStatic
    fun canDrawEssentialIndicator(entity: Entity): Boolean {
        return essentialBSManager.canDrawIndicator(entity)
    }

}
