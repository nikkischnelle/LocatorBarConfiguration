package dev.schnelle.locatorBarConfiguration.menu.submenus

import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

const val BACK_BUTTON_SIZE: Int = 200

@Suppress("UnstableApiUsage")
abstract class AbstractSubMenu(
    private val player: Player,
    private val subTitle: String,
    private val parentMenu: AbstractMenu?,
    private val columns: Int = 2,
) : AbstractMenu() {
    /**
     * Show the menu to the player.
     */
    fun showDialog() {
        showDialog(player)
    }

    override fun getTitle(): String = "${parentMenu?.getTitle()} > $subTitle"

    override fun build(builder: DialogRegistryEntry.Builder) {
        builder
            .base(
                DialogBase
                    .builder(Component.text(getTitle()).decorate(TextDecoration.BOLD))
                    .pause(false)
                    .afterAction(DialogBase.DialogAfterAction.NONE)
                    .body(getBody())
                    .build(),
            ).type(DialogType.multiAction(getActionButtons(), getExitActionButton(), columns))
    }

    /**
     * Get the body for the submenu.
     * Used for building.
     */
    protected abstract fun getBody(): List<DialogBody>

    /**
     * Get the action buttons for the submenu.
     * Used for building.
     */
    protected abstract fun getActionButtons(): List<ActionButton>

    /**
     * Check if the menu is locked.
     *
     * If this returns true, there is no reason for the menu to open.
     * The navigation button in `getNavigationButton` will not have a click action and the tooltip will hint at this.
     */
    abstract fun isLocked(): Boolean

    /**
     * Get the content inside the navigation button.
     */
    protected abstract fun getNavigationButtonContent(): Component

    /**
     * Get the text inside the navigation buttons tooltip.
     */
    protected abstract fun getNavigationTooltip(): String

    /**
     * Get the navigation button that opens this submenu.
     */
    fun getNavigationButton(): ActionButton {
        beforeDialog()
        return ActionButton.create(
            getNavigationButtonContent(),
            lockableNavigationTooltip(),
            300,
            getLockableNavigationAction(),
        )
    }

    /**
     * Get the action of the navigation button.
     *
     * null if `isLocked()` returns true.
     */
    private fun getLockableNavigationAction(): DialogAction? =
        if (!isLocked()) {
            DialogAction.customClick(
                { _, _ -> showDialog() },
                ClickCallback.Options
                    .builder()
                    .uses(1)
                    .build(),
            )
        } else {
            null
        }

    private fun lockableNavigationTooltip(): Component {
        val original = Component.text(getNavigationTooltip())
        return if (isLocked()) {
            Component
                .text("LOCKED by Administrators")
                .color(NamedTextColor.DARK_RED)
                .appendNewline()
                .append(original.color(NamedTextColor.GRAY))
        } else {
            original
        }
    }

    private fun getExitActionButton(): ActionButton =
        ActionButton.create(
            Component.text("Done"),
            null,
            BACK_BUTTON_SIZE,
            DialogAction.staticAction(ClickEvent.callback { _ -> parentMenu?.showDialog(player) }),
        )
}
