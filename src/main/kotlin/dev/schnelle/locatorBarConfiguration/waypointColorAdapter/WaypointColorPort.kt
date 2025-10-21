package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

interface WaypointColorPort {
    fun getWaypointColor(player: Player): Result<TextColor?>

    fun setWaypointColor(
        player: Player,
        color: TextColor,
    )
}
