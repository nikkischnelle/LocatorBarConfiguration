package dev.schnelle.locatorBarConfiguration.menu.submenus.implementations

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
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
class ColorSubMenu(
    private val player: Player,
    parentMenu: AbstractMenu?,
) : AbstractSubMenu(player, "Color", parentMenu) {
    private var currentColor: NamedTextColor? = null
    private var currentColorName: String = "None"

    private fun currentColorText(): Component {
        val currentColorTranslated = getColorName(currentColorName)
        return Component
            .text("Icon Color: ")
            .append(Component.text("⬤ $currentColorTranslated ⬤").color(currentColor))
    }

    override fun getNavigationButtonContent(): Component = currentColorText()

    override fun getNavigationTooltip(): String = "Select which color your icon appears for other plays on their locator bars."

    override fun beforeDialog() {
        currentColor = WaypointColor.getNamedWaypointColor(player).getOrNull()
        currentColorName = currentColor?.let { NamedTextColor.NAMES.key(it) } ?: "None"
    }

    override fun getBody(): List<DialogBody> =
        bodyFromString(getNavigationTooltip()).also { list ->
            list.add(DialogBody.plainMessage(currentColorText()))
        }

    override fun getActionButtons(): List<ActionButton> =
        NamedTextColor.NAMES.keys().map { name ->
            val color = NamedTextColor.NAMES.value(name)!!
            ActionButton.create(
                getColorNameComponent(name).color(color),
                Component
                    .text("Set your color to ")
                    .append(getColorNameComponent(name).color(color)),
                100,
                DialogAction.staticAction(
                    ClickEvent.callback { _ ->
                        WaypointColor.setWaypointColor(player, color)
                        showDialog()
                    },
                ),
            )
        }
}
