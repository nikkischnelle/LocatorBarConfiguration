package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor.Companion.tryInitialize
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.datapack.Datapack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

private const val RELOAD_PERMISSION = "lbc.reload"

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

        Config.init(this)

        val command =
            Commands
                .literal("lbc")
                .requires { it.sender.hasPermission(RELOAD_PERMISSION) }
                .then(
                    Commands.literal("reload").executes {
                        Config.getInstance().reload()
                        it.source.sender.sendMessage(
                            Component.text("Reloaded Locator Bar Configuration config").color(NamedTextColor.GOLD),
                        )
                        1
                    },
                ).build()

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, { builder ->
            builder.registrar().register(command)
        })
    }

    fun disableDatapack() {
        if (datapack?.isEnabled ?: false) {
            datapack?.isEnabled = false
        }
    }

    override fun onDisable() {}
}
