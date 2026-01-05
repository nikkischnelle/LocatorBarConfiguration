package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

private val versionToAdapter =
    mapOf(
        "1.21.7" to ::WaypointColorAdapter1_21,
        "1.21.8" to ::WaypointColorAdapter1_21,
        "1.21.9" to ::WaypointColorAdapter1_21,
        "1.21.10" to ::WaypointColorAdapter1_21,
        "1.21.11" to ::WaypointColorAdapter1_21,
    )

/**
 * Singleton Wrapper for WaypointColorPorts.
 *
 *  Selects the correct Port based on the current Minecraft version and uses it to get and set waypoint colors.
 */
class WaypointColor {
    companion object {
        @Volatile private var instance: WaypointColorPort? = null

        /**
         * Get a players waypoint color.
         *
         * Returns null if the color hasn't been set.
         * Wraps possible NMS errors in a result for easier handling.
         *
         * @param player the player to get the color for.
         *
         * @return the color of the players waypoint marker, wrapped in a result
         */
        fun getWaypointColor(player: Player): Result<TextColor?> = runCatching { getInstance().getWaypointColor(player) }

        /**
         * Set a players waypoint color.
         *
         * @param player the player to set the color for.
         * @param color the color to set the waypoint marker color to.
         *
         * @return Result wrapping possible NMS errors
         */
        fun setWaypointColor(
            player: Player,
            color: TextColor,
        ): Result<Unit> = runCatching { getInstance().setWaypointColor(player, color) }

        /**
         * Try initializing the waypoint color port.
         *
         * Returns true if the instance has been initialized successfully or had been initialized before.
         * Returns false if initialization failed (e.g. when the minecraft version is unsupported).
         */
        fun tryInitialize(): Boolean =
            try {
                getInstance()
                true
            } catch (_: IllegalStateException) {
                false
            }

        /**
         * Get the WaypointColorPort instance.
         *
         * Initializes the instance if it hasn't been before.
         */
        private fun getInstance(): WaypointColorPort = instance ?: synchronized(this) { createInstance().also { instance = it } }

        /**
         * Create a new WaypointColorPort based on Minecraft version.
         */
        private fun createInstance(): WaypointColorPort =
            versionToAdapter
                .getOrElse(Bukkit.getServer().minecraftVersion) {
                    throw IllegalStateException("Unsupported Minecraft version")
                }.call()
    }
}
