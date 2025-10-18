package dev.schnelle.locatorBarConfiguration.submenus.range

import dev.schnelle.locatorBarConfiguration.submenus.AbstractMenu
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class ReceiveRangeMenu(player: Player, parentMenu: AbstractMenu?) :
    RangeSubMenu(player, parentMenu, Attribute.WAYPOINT_RECEIVE_RANGE) {
    override fun getDescriptionText(): Array<String> {
        return arrayOf(
            "Select how far you can see other people on your locator bar.",
            "Example: Set your receive range to 1k to see players up to 1000 blocks away on your locator bar.",
            "Your transmit range is also your receive range. If you set your receive range to 10k, but your transmit range to " +
                "1k, you will only see players up to 1k blocks away on your locator bar.",
        )
    }

    override fun getButtonToolTip(rangeRepresentation: String): String {
        return "You can see other players up to $rangeRepresentation blocks away."
    }
}
