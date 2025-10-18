package dev.schnelle.locatorBarConfiguration

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

private const val MAX_RANGE = 6.0E7

private val rangeToRepresentation =
    mapOf(
        0.0 to "0",
        10.0 to "10",
        20.0 to "20",
        50.0 to "50",
        100.0 to "100",
        200.0 to "200",
        500.0 to "500",
        1000.0 to "1k",
        10000.0 to "10k",
        100000.0 to "100k",
        MAX_RANGE to "Infinite",
    )

private val attributeToName =
    mapOf(
        Attribute.WAYPOINT_TRANSMIT_RANGE.key().value() to "Transmit",
        Attribute.WAYPOINT_RECEIVE_RANGE.key().value() to "Receive",
    )

fun getRangeToRepresentation(value: Double): String? {
    return rangeToRepresentation[value]
}

@Suppress("UnstableApiUsage")
class RangeMenu(
    private val player: Player,
    private val attribute: Attribute,
    private vararg val description: String,
) {
    private val attributeName = attributeToName[attribute.key().value()]
    private val currentRange = player.getAttribute(attribute)?.baseValue ?: MAX_RANGE

    private val dialog =
        Dialog.create { builder ->
            builder
                .empty()
                .base(
                    DialogBase.builder(
                            Component.text("Locator Bar Configuration > Menu > $attributeName")
                        )
                        .pause(false)
                        .afterAction(DialogBase.DialogAfterAction.NONE)
                        .body(bodyFromString(*description))
                        .build()
                )
                .type(
                    DialogType.multiAction(
                        rangeToRepresentation.map { pair -> getRangeButton(pair, currentRange) },
                        ActionButton.create(
                            Component.text("Back"),
                            null,
                            200,
                            DialogAction.staticAction(
                                ClickEvent.callback { _ -> Menu(player).show() }
                            ),
                        ),
                        2,
                    )
                )
        }

    init {
        player.showDialog(dialog)
    }

    private fun getRangeButton(
        range: Map.Entry<Double, String>,
        currentRange: Double,
    ): ActionButton {
        val color =
            if (currentRange == range.component1()) {
                NamedTextColor.GREEN
            } else {
                NamedTextColor.GRAY
            }

        return ActionButton.create(
            Component.text(range.component2()).color(color),
            Component.text("Other players can see you up to ${range.component2()} away."),
            100,
            callback(range.component1()),
        )
    }

    private fun callback(range: Double): DialogAction.CustomClickAction {
        return DialogAction.customClick(
            { _, audience ->
                if (audience is Player) {
                    audience.getAttribute(attribute)!!.baseValue = range
                    audience.sendRichMessage(
                        "You selected a range of <color:#ccfffd><range> blocks</color>!",
                        Placeholder.component("range", Component.text(range)),
                    )
                }
                RangeMenu(player, attribute, *description)
            },
            ClickCallback.Options.builder()
                .uses(ClickCallback.UNLIMITED_USES)
                .lifetime(ClickCallback.DEFAULT_LIFETIME)
                .build(),
        )
    }
}
