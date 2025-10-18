package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private val versionToAdapter =
    mapOf(
        "1.21.7" to ::WaypointColorAdapter1_21,
        "1.21.8" to ::WaypointColorAdapter1_21,
        "1.21.9" to ::WaypointColorAdapter1_21,
        "1.21.10" to ::WaypointColorAdapter1_21,
    )

class WaypointColor {
    companion object {
        @Volatile private var instance: WaypointColorPort? = null

        fun getNamedWaypointColor(player: Player): Result<NamedTextColor?> {
            return getInstance().getNamedWaypointColor(player)
        }

        fun setWaypointColor(player: Player, color: NamedTextColor) {
            getInstance().setWaypointColor(player, color)
        }

        fun tryInitialize(): Boolean {
            return try {
                getInstance()
                true
            } catch (_: IllegalStateException) {
                false
            }
        }

        fun getInstance(): WaypointColorPort {
            return instance ?: synchronized(this) { createInstance().also { instance = it } }
        }

        private fun createInstance(): WaypointColorPort {
            return versionToAdapter
                .getOrElse(Bukkit.getServer().minecraftVersion) {
                    throw IllegalStateException("Unsupported Minecraft version")
                }
                .call()
        }
    }
}
