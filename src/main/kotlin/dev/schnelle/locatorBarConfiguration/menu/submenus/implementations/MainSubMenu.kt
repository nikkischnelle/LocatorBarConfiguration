package dev.schnelle.locatorBarConfiguration.menu.submenus.implementations

import dev.schnelle.locatorBarConfiguration.AttributeAdapter
import dev.schnelle.locatorBarConfiguration.menu.bodyFromString
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractSubMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.range.ReceiveRangeMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.range.TransmitRangeMenu
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
class MainSubMenu(
    private val player: Player,
    parentMenu: AbstractMenu?,
) : AbstractSubMenu(player, "Menu", parentMenu, 1) {
    override fun getNavigationButtonContent(): Component = Component.text("Menu")

    override fun getNavigationTooltip(): String = "Configure your locator bar."

    override fun beforeDialog() {}

    override fun getBody(): List<DialogBody> =
        bodyFromString(
            "The locator bar shows the position of other players.",
            "You can configure the range at which other players can see you on" +
                "their locator bar, as well as how far you can see other players on yours, using this menu.",
        )

    override fun getActionButtons(): List<ActionButton> =
        listOf(
            getToggleButton(),
            ReceiveRangeMenu(player, this).getNavigationButton(),
            TransmitRangeMenu(player, this).getNavigationButton(),
            ColorSubMenu(player, this).getNavigationButton(),
        )

    private fun getToggleButton(): ActionButton {
        val barEnabled = AttributeAdapter.isLocatorBarEnabled(player)

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
                    "other players' locator bars and you cannot see your own.",
            ),
            200,
            DialogAction.customClick(
                { _, _ ->
                    if (barEnabled) {
                        AttributeAdapter.disableLocatorBar(player)
                    } else {
                        AttributeAdapter.enableLocatorBar(player)
                    }
                    showDialog()
                },
                ClickCallback.Options
                    .builder()
                    .uses(1)
                    .build(),
            ),
        )
    }
}
