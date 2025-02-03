package org.polyfrost.polynametag

import org.objectweb.asm.tree.ClassNode
import org.polyfrost.oneconfig.api.platform.v1.LoaderPlatform.Loaders
import org.polyfrost.oneconfig.api.platform.v1.Platform
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo


class PolyNametagMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {}
    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: Set<String>?, otherTargets: Set<String>?) {}
    override fun getMixins(): List<String> {
        val mixins: MutableList<String> = ArrayList()
        val loader = Platform.loader().loader
        val version = Platform.loader().minecraftVersion
        if (loader == Loaders.FABRIC || version > 11202) { //todo idk what happens for this after 1.12 lol
            mixins.add("GLXMixin_LastBrightness")
        }
        return mixins
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }
}