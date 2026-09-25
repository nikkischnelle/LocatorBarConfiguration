package dev.schnelle.locatorBarConfiguration.waypointColorAdapter

import dev.schnelle.locatorBarConfiguration.AttributeAdapter
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.entity.Player
import java.lang.reflect.Method

/**
 * Color adapter using papers official API method.
 *
 * Check if [isSupported] is true before using.
 * Usage on unsupported paper versions will result in color changes not working.
 */
class WaypointColorAdapterPaper : WaypointColorPort {
    companion object {
        var getWaypointColorMethod: Method? = null
        var setWaypointColorMethod: Method? = null

        val isSupported: Boolean =
            try {
                getWaypointColorMethod = Player::class.java.getMethod("getWaypointColor")
                setWaypointColorMethod = Player::class.java.getMethod("setWaypointColor", Color::class.java)
                true
            } catch (_: NoSuchMethodException) {
                false
            }
    }

    override fun getWaypointColor(player: Player): TextColor? {
        val color = getWaypointColorMethod?.invoke(player) as? Color
        return if (color == null) {
            null
        } else {
            TextColor.color(color.asRGB())
        }
    }

    override fun setWaypointColor(
        player: Player,
        color: TextColor,
    ) {
        setWaypointColorMethod?.invoke(player, Color.fromRGB(color.value()))

        if (AttributeAdapter.isLocatorBarEnabled(player)) {
            AttributeAdapter.disableLocatorBar(player)
            AttributeAdapter.enableLocatorBar(player)
        }
    }
}
