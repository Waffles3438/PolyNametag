package org.polyfrost.polynametag

import gg.essential.universal.UMatrixStack
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.entity.Render
import net.minecraft.client.renderer.entity.RendererLivingEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.polynametag.mixin.MinecraftAccessor
import org.polyfrost.polynametag.mixin.RenderAccessor
import org.polyfrost.polynametag.render.EssentialBSManager

object NametagRenderer {

    interface NametagItem {

        val entity: Entity

    }

    data class LabelItem(val instance: Render<Entity>, override val entity: Entity, val str: String, val x: Double, val y: Double, val z: Double, val maxDistance: Int): NametagItem

    data class NameItem(val instance: RendererLivingEntity<EntityLivingBase>, override val entity: EntityLivingBase, val x: Double, val y: Double, val z: Double): NametagItem

    @JvmStatic
    val nametags = mutableListOf<NametagItem>()

    @JvmStatic
    var isCurrentlyDrawingWorld = false

    @JvmStatic
    var isCurrentlyDrawingInventory = false

    @JvmStatic
    var isCurrentlyDrawingPlayerName = false

    @JvmStatic
    var isDrawingIndicator = false

    @JvmStatic
    var isCurrentlyDrawingTags = false
        private set

    private val essentialBSManager = EssentialBSManager()

    @JvmStatic
    fun renderAll() {
        if (nametags.isEmpty()) {
            return
        }

        GlStateManager.pushMatrix()
        isCurrentlyDrawingTags = true
        mc.entityRenderer.enableLightmap()
        val partialTicks = (mc as MinecraftAccessor).timer.renderPartialTicks
        for (nametag in nametags) {
            val brightness = if (nametag.entity.isBurning) 15728880 else nametag.entity.getBrightnessForRender(partialTicks)
            val x = brightness % 65536
            val y = brightness / 65536

            OpenGlHelper.setLightmapTextureCoords(
                OpenGlHelper.lightmapTexUnit,
                x.toFloat() / 1.0f,
                y.toFloat() / 1.0f
            )

            when (nametag) {
                is LabelItem -> {
                    @Suppress("UNCHECKED_CAST")
                    (nametag.instance as RenderAccessor<Entity>).renderNametag(
                        nametag.entity,
                        nametag.str,
                        nametag.x,
                        nametag.y,
                        nametag.z,
                        nametag.maxDistance
                    )
                }

                is NameItem -> {
                    @Suppress("UNCHECKED_CAST")
                    nametag.instance.renderName(nametag.entity, nametag.x, nametag.y, nametag.z)
                }
            }
        }

        nametags.clear()
        isCurrentlyDrawingTags = false
        mc.entityRenderer.disableLightmap()
        GlStateManager.popMatrix()
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
