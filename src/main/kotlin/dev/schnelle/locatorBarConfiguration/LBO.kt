package dev.schnelle.locatorBarConfiguration

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class LBO {
    companion object {
        const val INCOMPATABILITY_MESSAGE =
            "Locator Bar Configuration AND RedSyven's Locator Bar Options enabled. Neither will work properly."

        fun isEnabled(): Boolean {
            Bukkit.getDatapackManager().refreshPacks()
            return !Bukkit.getDatapackManager()
                .enabledPacks
                .mapNotNull { it.description as? TextComponent }
                .none { it.content().startsWith("RedSyven's Locator Bar Options") }
        }

        fun registerIncompatibilityAlert(plugin: Plugin) {
            Bukkit.getPluginManager().registerEvents(IncompatibilityAlert(), plugin)
        }
    }

    class IncompatibilityAlert : Listener {
        @EventHandler
        fun onJoin(event: PlayerJoinEvent) {
            if (!event.player.isOp || !isEnabled()) {
                return
            }

            event.player.sendMessage(
                Component.text("Incompatibility Alert: ", NamedTextColor.RED)
                    .appendNewline()
                    .append(Component.text(INCOMPATABILITY_MESSAGE))
            )
        }
    }
}
