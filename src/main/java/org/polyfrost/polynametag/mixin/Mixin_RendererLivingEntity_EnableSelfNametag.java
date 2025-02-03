package org.polyfrost.polynametag.mixin;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import org.polyfrost.polynametag.PolyNametagConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public class Mixin_RendererLivingEntity_EnableSelfNametag {

    @Redirect(method = "canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/RenderManager;livingPlayer:Lnet/minecraft/entity/Entity;"))
    private Entity polynametag$enableSelfNametag(RenderManager instance) {
        if (!PolyNametagConfig.INSTANCE.getEnabled() || !PolyNametagConfig.INSTANCE.getShowOwnNametag()) {
            return instance.livingPlayer;
        }

        return null;
    }

}
