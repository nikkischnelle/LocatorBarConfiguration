package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.menu.submenus.range.MAX_RANGE
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class LBO {
    companion object {
        const val INCOMPATABILITY_MESSAGE =
            "Locator Bar Configuration AND RedSyven's Locator Bar Options enabled. Neither will work properly."

        private val lboKeyOff = NamespacedKey("lbo", "off")
        private val lboKeyNormalize = NamespacedKey("lbo", "normalize")
        private val lboKeyCustomize = NamespacedKey("lbo", "customize")

        fun isEnabled(): Boolean {
            Bukkit.getDatapackManager().refreshPacks()
            return !Bukkit
                .getDatapackManager()
                .enabledPacks
                .mapNotNull { it.description as? TextComponent }
                .none { it.content().startsWith("RedSyven's Locator Bar Options") }
        }

        fun registerPlayerListener(plugin: Plugin) {
            Bukkit.getPluginManager().registerEvents(PlayerListener(plugin), plugin)
        }

        fun migratePlayer(
            plugin: Plugin,
            player: Player,
        ) {
            val attributes =
                listOf(Attribute.WAYPOINT_TRANSMIT_RANGE, Attribute.WAYPOINT_RECEIVE_RANGE)

            for (attribute in attributes) {
                val instance = player.getAttribute(attribute)!!
                if (
                    instance.baseValue != MAX_RANGE || !AttributeAdapter.isLocatorBarEnabled(player)
                ) {
                    plugin.logger.fine(
                        "Player ${player.name} has already customized their range with the plugin.",
                    )
                    return
                }
            }

            plugin.logger.finer("Trying to migrate LBO to LBC for player ${player.name}")

            var migratedData = false
            var disableBar = false

            for (attribute in attributes) {
                val instance = player.getAttribute(attribute)!!

                val hasOff = AttributeAdapter.hasAttributeModifier(instance, lboKeyOff)
                val hasNormalize = AttributeAdapter.hasAttributeModifier(instance, lboKeyNormalize)
                val hasCustomize = AttributeAdapter.hasAttributeModifier(instance, lboKeyCustomize)

                if (hasOff) {
                    // Remove off to get values before disabling
                    AttributeAdapter.removeModifier(instance, lboKeyOff)
                    disableBar = true
                    migratedData = true
                }

                if (hasNormalize && hasCustomize) {
                    val value = AttributeAdapter.getAttributeValue(player, attribute)!!
                    AttributeAdapter.setAttributeBaseValue(player, attribute, value)
                }
                if (hasNormalize) {
                    AttributeAdapter.removeModifier(instance, lboKeyNormalize)
                    migratedData = true
                }
                if (hasCustomize) {
                    AttributeAdapter.removeModifier(instance, lboKeyCustomize)
                    migratedData = true
                }
            }

            if (disableBar) {
                AttributeAdapter.disableLocatorBar(player)
            }

            if (!migratedData) {
                return
            }

            val migrationMessageString = Config.getInstance().getLBOMigrationUserNotification()
            if (migrationMessageString.isBlank()) {
                return
            }
            val message = MiniMessage.miniMessage()

            player
                .sendMessage(
                    message.deserialize(migrationMessageString),
                )
        }
    }

    class PlayerListener(
        private val plugin: Plugin,
    ) : Listener {
        @EventHandler
        fun onJoin(event: PlayerJoinEvent) {
            Scheduler(plugin).runAsyncTask("Error during LBO listener. Probably the migration.", 1) {
                val player = event.player
                if (isEnabled()) {
                    if (!player.isOp) {
                        return@runAsyncTask
                    }

                    player.sendMessage(
                        Component
                            .text("Incompatibility Alert: ", NamedTextColor.RED)
                            .appendNewline()
                            .append(Component.text(INCOMPATABILITY_MESSAGE)),
                    )
                    return@runAsyncTask
                }

                if (Config.getInstance().getEnableLBOMigration()) {
                    migratePlayer(plugin, player)
                }
            }
        }
    }
}
