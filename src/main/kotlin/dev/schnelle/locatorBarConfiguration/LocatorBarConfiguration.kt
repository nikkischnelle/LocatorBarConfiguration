package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor.Companion.tryInitialize
import org.bukkit.plugin.java.JavaPlugin

class LocatorBarConfiguration : JavaPlugin() {

    override fun onLoad() {
        val pack = server.datapackManager.getPack(pluginMeta.name + "/provided")
        if (pack != null) {
            if (pack.isEnabled) {
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
                    "the current minecraft version. LocatorBarConfiguration will not work properly."
            )
            return
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
