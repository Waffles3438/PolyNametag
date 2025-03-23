package org.polyfrost.polynametag.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import cc.polyfrost.oneconfig.utils.Notifications
import club.sk1er.patcher.config.OldPatcherConfig
import club.sk1er.patcher.config.PatcherConfig
import org.polyfrost.polynametag.PolyNametag
import org.polyfrost.polynametag.render.NametagPreview
import java.lang.reflect.Field

object ModConfig : Config(Mod("Nametags", ModType.UTIL_QOL, "/polynametag.svg"), "${PolyNametag.MODID}.json") {

    @Slider(name = "Height offset", min = -0.5f, max = 0.5f, description = "How much to offset the nametag vertically")
    var heightOffset = 0f
        get() = field.coerceIn(-0.5f, 0.5f)

    @Slider(name = "Scale", min = 0f, max = 1f, description = "How much to scale the nametag")
    var scale = 1f
        get() = field.coerceIn(0f, 1f)

    @Switch(name = "Rounded Corners")
    var rounded = false

    @Slider(name = "Corner Radius", min = 0f, max = 10f)
    var cornerRadius = 0f
        get() = field.coerceIn(0f, 10f)

    @Slider(name = "Padding X", min = 0f, max = 10f)
    var paddingX = 0f
        get() = field.coerceIn(0f, 10f)

    @Slider(name = "Padding Y", min = 0f, max = 10f)
    var paddingY = 0f
        get() = field.coerceIn(0f, 10f)

    @Dropdown(name = "Text Type", options = ["No Shadow", "Shadow", "Full Shadow"], description = "The type of shadow to render")
    var textType = 0

    @Info(
        type = InfoType.WARNING,
        text = "Using Full Shadow may cause performance issues on low-end devices"
    )
    var info1 = 0

    @Switch(name = "Show own nametag", description = "Whether to show your own nametag")
    var showOwnNametag = true

    @Switch(name = "Show in inventory")
    var showInInventory = false

    @Switch(name = "Background", description = "Whether to render a background behind the nametag")
    var background = true

    @Color(name = "Background color", description = "The color of the background")
    var backgroundColor = OneColor(0, 0, 0, 63)

    @Switch(name = "Show nametags through walls", description = "Backports nametag rendering from modern versions where nametags render through walls")
    var nametagsThroughWalls = false

    @Switch(name = "Offset Essential Indicator", description = "Offset nametag to center if the player has essential indicator drawn")
    var essentialOffset = true

    @CustomOption
    @Transient
    val nametagPreview = NametagPreview(category = "General")

    var hasMigratedPatcher = false

    init {
        initialize()
        addDependency("backgroundColor", "background")
        addDependency("background", "Patcher's Disable Nametag Boxes. Please turn it off to use this feature.") {
            !PolyNametag.isPatcher || !PatcherConfig.disableNametagBoxes
        }
        addDependency("showOwnNametag", "Patcher's Show Own Nametag. Please turn it off to use this feature.") {
            !PolyNametag.isPatcher || !PatcherConfig.showOwnNametag
        }
        addDependency("cornerRadius", "rounded")
        addDependency("showInInventory", "showOwnNametag")
        hideIf("essentialOffset") { !PolyNametag.isEssential }

        if (!hasMigratedPatcher) {
            try {
                Class.forName("club.sk1er.patcher.config.OldPatcherConfig")
                var didAnything = false
                if (OldPatcherConfig.shadowedNametagText) {
                    textType = 1
                    didAnything = true
                }
                if (OldPatcherConfig.disableNametagBoxes) {
                    background = false
                    didAnything = true
                }
                if (OldPatcherConfig.showOwnNametag) {
                    showOwnNametag = true
                    didAnything = true
                }

                hasMigratedPatcher = true
                save()

                if (didAnything) {
                    Notifications.INSTANCE.send("PolyNametag", "Migrated Patcher settings replaced by PolyNametag. Please check PolyNametag's settings to make sure they are correct.")
                }
            } catch (_: ClassNotFoundException) {

            }
        }
    }

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean,
    ): BasicOption = nametagPreview.also {
        ConfigUtils.getSubCategory(page, it.category, it.subcategory).options.add(it)
    }
}
