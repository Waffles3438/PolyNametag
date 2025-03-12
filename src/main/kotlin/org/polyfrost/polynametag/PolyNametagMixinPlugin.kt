package org.polyfrost.polynametag

import org.objectweb.asm.tree.ClassNode
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

        //#if FABRIC && MC > 1.12.2
        mixins.add("GLXMixin_LastBrightness")
        //#endif

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