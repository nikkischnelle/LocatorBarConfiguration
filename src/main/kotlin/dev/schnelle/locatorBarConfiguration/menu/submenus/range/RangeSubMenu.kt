package dev.schnelle.locatorBarConfiguration.menu.submenus.range

import dev.schnelle.locatorBarConfiguration.AttributeAdapter.Companion.getAttributeBaseValue
import dev.schnelle.locatorBarConfiguration.AttributeAdapter.Companion.setAttributeBaseValue
import dev.schnelle.locatorBarConfiguration.menu.bodyFromString
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractSubMenu
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

private val attributeToName =
    mapOf(
        Attribute.WAYPOINT_TRANSMIT_RANGE.key().value() to "Transmit",
        Attribute.WAYPOINT_RECEIVE_RANGE.key().value() to "Receive",
    )

private fun getAttributeName(attribute: Attribute): String = attributeToName[attribute.key().value()] ?: attribute.key().value()

private fun rangeToStringK(value: Double): String =
    when {
        value == MAX_RANGE -> "Infinite"
        value >= 1_000_000 -> String.format("%.1fm", value / 1_000_000)
        value >= 1_000 -> String.format("%.1fk", value / 1_000)
        else -> value.toString()
    }

private fun getRanges(): Map<Double, String> =
    listOf(0.0, 10.0, 20.0, 50.0, 100.0, 200.0, 500.0, 1000.0, 10000.0, 100000.0, MAX_RANGE)
        .associateWith { rangeToStringK(it) }

@Suppress("UnstableApiUsage")
abstract class RangeSubMenu(
    private val player: Player,
    parentMenu: AbstractMenu?,
    private val attribute: Attribute,
) : AbstractSubMenu(player, getAttributeName(attribute), parentMenu) {
    private val attributeName = getAttributeName(attribute)
    private var currentRange: Double = MAX_RANGE
    private var currentRangeRep: String = ""

    override fun beforeDialog() {
        currentRange = getAttributeBaseValue(player, attribute) ?: MAX_RANGE
        currentRangeRep = rangeToStringK(currentRange)
    }

    override fun getBody(): List<DialogBody> = bodyFromString(*getDescriptionText())

    override fun getActionButtons(): List<ActionButton> =
        getRanges().map { (range, rep) ->
            val color =
                when {
                    currentRange == range -> NamedTextColor.GREEN
                    else -> null
                }

            ActionButton.create(
                Component.text(rep).color(color),
                Component.text(getButtonToolTip(rep)),
                100,
                callback(range),
            )
        }

    private fun callback(range: Double): DialogAction.CustomClickAction =
        DialogAction.customClick(
            { _, audience ->
                if (audience is Player) {
                    setAttributeBaseValue(player, attribute, range)
                }
                showDialog()
            },
            ClickCallback.Options
                .builder()
                .uses(ClickCallback.UNLIMITED_USES)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build(),
        )

    override fun getNavigationButtonContent(): Component =
        Component
            .text("$attributeName Range: ")
            .append(Component.text("$currentRangeRep blocks").color(NamedTextColor.LIGHT_PURPLE))

    override fun getNavigationTooltip(): String = getDescriptionText().first()

    protected abstract fun getButtonToolTip(rangeRepresentation: String): String

    abstract fun getDescriptionText(): Array<String>
}
