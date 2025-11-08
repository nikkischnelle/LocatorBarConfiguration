package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

interface WaypointColorPort {
    /**
     * Get the color of a players' waypoint marker.
     * Returns null if the waypoint color has not been previously set.
     *
     * @param player the player to get the color for.
     *
     * @throws all sorts of exceptions that could occur for faulty NMS interactions in case of version mismatches.
     */
    fun getWaypointColor(player: Player): TextColor?

    /**
     * Set the color of a players' waypoint.
     *
     * @param player the player to set the color for.
     * @param color the color to set the waypoint marker color to
     *
     * @throws all sorts of exceptions that could occur for faulty NMS interactions in case of version mismatches.
     */
    fun setWaypointColor(
        player: Player,
        color: TextColor,
    )
}
