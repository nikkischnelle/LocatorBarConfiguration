package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

interface WaypointColorPort {
    fun getNamedWaypointColor(player: Player): Result<NamedTextColor?>

    fun setWaypointColor(
        player: Player,
        color: NamedTextColor,
    )
}
