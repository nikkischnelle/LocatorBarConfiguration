package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
class ColorMenu(private val player: Player) {
    private val currentColor: NamedTextColor? =
        WaypointColor.getNamedWaypointColor(player).getOrNull()
    private val currentColorName = currentColor?.let { NamedTextColor.NAMES.key(it) } ?: "None"

    private fun currentColorText(): Component {
        return Component.text("Current Color: ")
            .append(getColorNameComponent(currentColorName).color(currentColor))
    }

    init {
        player.showDialog(
            Dialog.create { builder ->
                builder
                    .empty()
                    .base(
                        DialogBase.builder(
                                Component.text("Locator Bar Configuration > Menu > Color")
                            )
                            .body(
                                bodyFromString(
                                        "Select which color your icon appears for other plays on their locator bars."
                                    )
                                    .also { it.add(DialogBody.plainMessage(currentColorText())) }
                            )
                            .pause(false)
                            .afterAction(DialogBase.DialogAfterAction.NONE)
                            .build()
                    )
                    .type(
                        DialogType.multiAction(
                            NamedTextColor.NAMES.keys().map {
                                getColorButton(it, NamedTextColor.NAMES.value(it)!!)
                            },
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
        )
    }

    fun getColorButton(name: String, color: NamedTextColor): ActionButton {
        return ActionButton.create(
            getColorNameComponent(name).color(color),
            Component.text("Set your color to ").append(getColorNameComponent(name).color(color)),
            100,
            DialogAction.staticAction(
                ClickEvent.callback { _ ->
                    WaypointColor.setWaypointColor(player, color)
                    ColorMenu(player)
                }
            ),
        )
    }
}
