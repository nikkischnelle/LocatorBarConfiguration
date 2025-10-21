package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import net.kyori.adventure.text.format.TextColor
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@Suppress("ClassName")
class WaypointColorAdapter1_21 : WaypointColorPort {
    override fun getWaypointColor(player: Player): Result<TextColor?> =
        runCatching {
            val icon = (player as CraftPlayer).handle.waypointIcon()
            val color = icon.color.getOrNull()
            if (color == null) {
                null
            } else {
                TextColor.color(color)
            }
        }

    override fun setWaypointColor(
        player: Player,
        color: TextColor,
    ) {
        runCatching {
            val icon = (player as CraftPlayer).handle.waypointIcon()
            icon.color = Optional.of(color.value())
        }
    }
}
