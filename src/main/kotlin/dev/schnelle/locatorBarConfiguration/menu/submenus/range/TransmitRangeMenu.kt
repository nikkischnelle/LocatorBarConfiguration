package dev.schnelle.locatorBarConfiguration.menu.submenus.range

import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class TransmitRangeMenu(
    player: Player,
    parentMenu: AbstractMenu?,
) : RangeSubMenu(player, parentMenu, Attribute.WAYPOINT_TRANSMIT_RANGE) {
    override fun getDescriptionText(): Array<String> =
        arrayOf(
            "Select how far other people can see you on their locator bar.",
            "Example: Set your transmit range to 1k and other people will see you on their locator bar if you're within 1000 blocks.",
        )

    override fun getButtonToolTip(rangeRepresentation: String): String = "Other players can see you up to $rangeRepresentation blocks away."
}
