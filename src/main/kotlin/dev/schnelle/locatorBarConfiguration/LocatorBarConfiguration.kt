package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor.Companion.tryInitialize
import io.papermc.paper.datapack.Datapack
import org.bukkit.plugin.java.JavaPlugin

class LocatorBarConfiguration : JavaPlugin() {
    private var datapack: Datapack? = null

    override fun onLoad() {
        datapack = server.datapackManager.getPack(pluginMeta.name + "/provided")
        if (datapack != null) {
            if (datapack!!.isEnabled) {
                logger.info("Locator Bar Configuration Datapack loaded successfully")
            } else {
                logger.warning("Locator Bar Configuration Datapack failed to load.")
            }
        }
    }

    override fun onEnable() {
        if (!tryInitialize()) {
            logger.severe(
                "Unsupported minecraft version found. Please ensure that the plugin is compatible with" +
                    "the current minecraft version. LocatorBarConfiguration will not work properly.",
            )
            disableDatapack()
            return
        }

        LBO.registerPlayerListener(this)
        if (LBO.isEnabled()) {
            logger.warning(LBO.INCOMPATABILITY_MESSAGE)
        }
    }

    fun disableDatapack() {
        if (datapack?.isEnabled ?: false) {
            datapack?.isEnabled = false
        }
    }

    override fun onDisable() {}
}
