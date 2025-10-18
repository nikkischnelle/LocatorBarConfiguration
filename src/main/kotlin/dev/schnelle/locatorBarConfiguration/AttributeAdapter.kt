package dev.schnelle.locatorBarConfiguration

import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class AttributeAdapter {
    companion object {
        private val disableModifierKey = NamespacedKey("lbc", "disable")
        private val disableModifier =
            AttributeModifier(
                disableModifierKey,
                -1.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1,
            )

        fun setAttributeBaseValue(player: Player, attribute: Attribute, value: Double) {
            player.getAttribute(attribute)!!.baseValue = value
        }

        fun getAttributeBaseValue(player: Player, attribute: Attribute): Double? {
            return player.getAttribute(attribute)?.baseValue
        }

        fun getAttributeValue(player: Player, attribute: Attribute): Double? {
            return player.getAttribute(attribute)?.value
        }

        fun isLocatorBarEnabled(player: Player): Boolean {
            return getTransmitAttribute(player).getModifier(disableModifierKey) == null
        }

        fun hasAttributeModifier(
            attributeInstance: AttributeInstance,
            key: NamespacedKey,
        ): Boolean {
            return attributeInstance.getModifier(key) != null
        }

        fun disableLocatorBar(player: Player) {
            getReceiveAttribute(player).addModifier(disableModifier)
            getTransmitAttribute(player).addModifier(disableModifier)
        }

        fun enableLocatorBar(player: Player) {
            removeModifier(getReceiveAttribute(player), disableModifierKey)
            removeModifier(getTransmitAttribute(player), disableModifierKey)
        }

        fun removeModifier(attributeInstance: AttributeInstance, modifierKey: NamespacedKey) {
            attributeInstance.removeModifier(modifierKey)
        }

        private fun getReceiveAttribute(player: Player): AttributeInstance {
            return player.getAttribute(Attribute.WAYPOINT_RECEIVE_RANGE)!!
        }

        private fun getTransmitAttribute(player: Player): AttributeInstance {
            return player.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE)!!
        }
    }
}
