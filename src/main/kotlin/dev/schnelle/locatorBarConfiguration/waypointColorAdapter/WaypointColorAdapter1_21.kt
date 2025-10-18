package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

@Suppress("ClassName")
class WaypointColorAdapter1_21 : WaypointColorPort {
    override fun getNamedWaypointColor(player: Player): Result<NamedTextColor?> {
        return runCatching {
            val icon = (player as CraftPlayer).handle.waypointIcon()
            val color = icon.color.getOrNull()
            if (color == null) {
                null
            } else {
                NamedTextColor.nearestTo(TextColor.color(color))
            }
        }
    }

    override fun setWaypointColor(player: Player, color: NamedTextColor) {
        runCatching {
            val icon = (player as CraftPlayer).handle.waypointIcon()
            icon.color = Optional.of(color.value())
        }
    }
}
