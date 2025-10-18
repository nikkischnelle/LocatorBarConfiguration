package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

private val disableModifierKey = NamespacedKey("lbc", "disable")
private val disableModifier =
    AttributeModifier(disableModifierKey, -1.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1)

private val transmitDescription =
    arrayOf(
        "Select how far other people can see you on their locator bar.",
        "Example: Set your transmit range to 1k and other people will see you on their locator bar if you're within 1000 blocks.",
    )

private val receiveDescription =
    arrayOf(
        "Select how far you can see other people on your locator bar.",
        "Example: Set your receive range to 1k to see players up to 1000 blocks away on your locator bar.",
        "Your transmit range is also your receive range. If you set your receive range to 10k, but your transmit range to " +
            "1k, you will only see players up to 1k blocks away on your locator bar.",
    )

@Suppress("UnstableApiUsage")
class Menu(private val player: Player) {
    private val receiveAttribute = player.getAttribute(Attribute.WAYPOINT_RECEIVE_RANGE)!!
    private val transmitAttribute = player.getAttribute(Attribute.WAYPOINT_TRANSMIT_RANGE)!!

    private val dialog: Dialog =
        Dialog.create { builder ->
            builder
                .empty()
                .base(
                    DialogBase.builder(Component.text("Locator Bar Configuration > Menu"))
                        .inputs(listOf())
                        .pause(false)
                        .afterAction(DialogBase.DialogAfterAction.NONE)
                        .body(
                            bodyFromString(
                                "The locator bar shows the position of other players.",
                                "You can configure the range at which other players can see you on" +
                                    "their locator bar, as well as how far you can see other players on yours, using this menu.",
                            )
                        )
                        .build()
                )
                .type(
                    DialogType.multiAction(
                        listOf(
                            getToggleButton(),
                            getTransmitButton(),
                            getReceiveButton(),
                            getColorButton(),
                        ),
                        ActionButton.create(
                            Component.text("Back"),
                            null,
                            300,
                            DialogAction.customClick(
                                { _, _ -> MenuDialogBuilder.show(player) },
                                ClickCallback.Options.builder().uses(1).build(),
                            ),
                        ),
                        1,
                    )
                )
        }

    private fun getToggleButton(): ActionButton {
        val barEnabled = transmitAttribute.getModifier(disableModifierKey) == null

        val enabledComponent =
            if (barEnabled) {
                Component.text("Enabled").color(NamedTextColor.GREEN)
            } else {
                Component.text("Disabled").color(NamedTextColor.RED)
            }

        return ActionButton.create(
            Component.text("Locator Bar: ").append(enabledComponent),
            Component.text(
                "Disables the entire locator bar. If disabled, you do not show up on " +
                    "other players' locator bars and you cannot see your own."
            ),
            200,
            DialogAction.customClick(
                { _, _ ->
                    if (barEnabled) {
                        transmitAttribute.addModifier(disableModifier)
                        receiveAttribute.addModifier(disableModifier)
                    } else {
                        transmitAttribute.removeModifier(disableModifierKey)
                        receiveAttribute.removeModifier(disableModifierKey)
                    }
                    Menu(player).show()
                },
                ClickCallback.Options.builder().uses(1).build(),
            ),
        )
    }

    private fun getReceiveButton(): ActionButton {
        val range =
            getRangeToRepresentation(receiveAttribute.baseValue)
                ?: receiveAttribute.baseValue.toInt().toString()

        val name =
            Component.text("Receive Range: ")
                .append(Component.text("$range blocks").color(NamedTextColor.LIGHT_PURPLE))

        return ActionButton.create(
            name,
            Component.text(receiveDescription.first()),
            300,
            DialogAction.customClick(
                { _, _ ->
                    RangeMenu(player, Attribute.WAYPOINT_RECEIVE_RANGE, *receiveDescription)
                },
                ClickCallback.Options.builder().uses(1).build(),
            ),
        )
    }

    private fun getTransmitButton(): ActionButton {
        val range =
            getRangeToRepresentation(transmitAttribute.baseValue)
                ?: transmitAttribute.baseValue.toInt().toString()

        val name =
            Component.text("Transmit Range: ")
                .append(Component.text("$range blocks").color(NamedTextColor.LIGHT_PURPLE))

        return ActionButton.create(
            name,
            Component.text(transmitDescription.first()),
            300,
            DialogAction.customClick(
                { _, _ ->
                    RangeMenu(player, Attribute.WAYPOINT_TRANSMIT_RANGE, *transmitDescription)
                },
                ClickCallback.Options.builder().uses(1).build(),
            ),
        )
    }

    private fun getColorButton(): ActionButton {
        val color = WaypointColor.getNamedWaypointColor(player).getOrNull()
        val currentColorName = getColorName(color?.let { NamedTextColor.NAMES.key(it) } ?: "None")
        val text =
            Component.text("Icon Color: ")
                .append(Component.text("⬤ $currentColorName ⬤").color(color))

        return ActionButton.create(
            text,
            Component.text("Select which color your icon appears in."),
            300,
            DialogAction.customClick(
                { _, _ -> ColorMenu(player) },
                ClickCallback.Options.builder().uses(1).build(),
            ),
        )
    }

    fun show() {
        player.showDialog(dialog)
    }
}
