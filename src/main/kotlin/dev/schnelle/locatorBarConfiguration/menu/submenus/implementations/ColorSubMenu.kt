package dev.schnelle.locatorBarConfiguration.menu.submenus.implementations

import dev.schnelle.locatorBarConfiguration.Config
import dev.schnelle.locatorBarConfiguration.menu.bodyFromString
import dev.schnelle.locatorBarConfiguration.menu.getColorName
import dev.schnelle.locatorBarConfiguration.menu.getColorNameComponent
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractSubMenu
import dev.schnelle.locatorBarConfiguration.waypointColorAdapter.WaypointColor
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
class ColorSubMenu(
    private val player: Player,
    parentMenu: AbstractMenu?,
) : AbstractSubMenu(player, "Color", parentMenu) {
    private var currentColor: TextColor? = null

    private fun currentColorText(): Component {
        val currentColorTranslated = currentColor?.let { getColorName(it) } ?: "Unknown/Not set"
        return Component
            .text("Icon Color: ")
            .append(Component.text("⬤ $currentColorTranslated ⬤").color(currentColor))
    }

    override fun getNavigationButtonContent(): Component = currentColorText()

    override fun getNavigationTooltip(): String = "Select which color your icon appears for other plays on their locator bars."

    override fun beforeDialog() {
        currentColor = WaypointColor.getNamedWaypointColor(player).getOrNull()
    }

    override fun getBody(): List<DialogBody> =
        bodyFromString(getNavigationTooltip()).also { list ->
            list.add(DialogBody.plainMessage(currentColorText()))
        }

    override fun getActionButtons(): List<ActionButton> =
        Config.getInstance().getColors().map { color ->
            ActionButton.create(
                getColorNameComponent(color).color(color.textColor),
                Component
                    .text("Set your color to ")
                    .append(getColorNameComponent(color).color(color.textColor)),
                100,
                DialogAction.staticAction(
                    ClickEvent.callback { _ ->
                        WaypointColor.setWaypointColor(player, color.textColor)
                        showDialog()
                    },
                ),
            )
        }

    override fun isLocked(): Boolean = Config.getInstance().getColors().isEmpty()
}
