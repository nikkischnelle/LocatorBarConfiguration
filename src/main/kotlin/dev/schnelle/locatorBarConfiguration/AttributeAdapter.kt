package dev.schnelle.locatorBarConfiguration

import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

object AttributeAdapter {
    private val disableModifierKey = NamespacedKey("lbc", "disable")

    /**
     * Attribute modifier that disables transmit and receive ranges by multiplying the base value by -1.
     */
    private val disableModifier =
        AttributeModifier(
            disableModifierKey,
            -1.0,
            AttributeModifier.Operation.MULTIPLY_SCALAR_1,
        )

    /**
     * set base value of an attribute for a player.
     *
     * @param player the player to set the value for
     * @param attribute the attribute to set the value for
     * @param value the value to set
     */
    fun setAttributeBaseValue(
        player: Player,
        attribute: Attribute,
        value: Double,
    ) {
        player.getAttribute(attribute)!!.baseValue = value
    }

    /**
     * get base value of an attribute for a player.
     *
     * @param player the player to get the value for
     * @param attribute the attribute to get the value for
     */
    fun getAttributeBaseValue(
        player: Player,
        attribute: Attribute,
    ): Double? = player.getAttribute(attribute)?.baseValue

    /**
     * get value of an attribute for a player.
     *
     * @param player the player to get the value for
     * @param attribute the attribute to get the value for
     */
    fun getAttributeValue(
        player: Player,
        attribute: Attribute,
    ): Double? = player.getAttribute(attribute)?.value

    /**
     * check if locator bar is enabled.
     *
     * Checks if the disable modifier is set for the transmit attribute.
     *
     * @param player check if bar is enabled for this player
     */
    fun isLocatorBarEnabled(player: Player): Boolean = getTransmitAttribute(player).getModifier(disableModifierKey) == null

    /**
     * Disable the locator bar for a player.
     */
    fun disableLocatorBar(player: Player) {
        getReceiveAttribute(player).addModifier(disableModifier)
        getTransmitAttribute(player).addModifier(disableModifier)
    }

    /**
     * Enable the locator bar for a player.
     */
    fun enableLocatorBar(player: Player) {
        removeModifier(getReceiveAttribute(player), disableModifierKey)
        removeModifier(getTransmitAttribute(player), disableModifierKey)
    }

    /**
     * check if an attribute instance has a modifier applied.
     */
    fun hasAttributeModifier(
        attributeInstance: AttributeInstance,
        key: NamespacedKey,
    ): Boolean = attributeInstance.getModifier(key) != null

    /**
     * Remove the modifier from an attribute instance.
     */
    fun removeModifier(
        attributeInstance: AttributeInstance,
        modifierKey: NamespacedKey,
    ) {
        attributeInstance.removeModifier(modifierKey)
    }

    /**
     * Get the attribute instance for the waypoint receive range.
     */
    private fun getReceiveAttribute(player: Player): AttributeInstance = player.getAttribute(Attribute.WAYPOINT_RECEIVE_RANGE)!!

    /**
     * Get the attribute instance for the waypoint transmit range.
     */
    private fun getTransmitAttribute(player: Player): AttributeInstance = player.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE)!!
}
