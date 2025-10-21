package dev.schnelle.locatorBarConfiguration.menu.submenus

import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.ActionButtonImpl
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.input.DialogInput
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.jetbrains.annotations.Range
import javax.naming.Name
import javax.swing.Action

const val BACK_BUTTON_SIZE: Int = 200

@Suppress("UnstableApiUsage")
abstract class AbstractSubMenu(
    private val player: Player,
    private val subTitle: String,
    private val parentMenu: AbstractMenu?,
    private val columns: Int = 2,
) : AbstractMenu() {
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

    protected abstract fun getBody(): List<DialogBody>

    protected abstract fun getActionButtons(): List<ActionButton>

    private fun getExitActionButton(): ActionButton =
        ActionButton.create(
            Component.text("Done"),
            null,
            BACK_BUTTON_SIZE,
            DialogAction.staticAction(ClickEvent.callback { _ -> parentMenu?.showDialog(player) }),
        )

    fun getNavigationButton(): ActionButton {
        beforeDialog()
        return ActionButton.create(
            getNavigationButtonContent(),
            lockableNavigationTooltip(),
            300,
            getLockableNavigationAction(),
        )
    }

    abstract fun isLocked(): Boolean

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

    abstract fun getNavigationButtonContent(): Component

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

    abstract fun getNavigationTooltip(): String
}
