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
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player

const val backButtonSize: Int = 200

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

    override fun getTitle(): String {
        return "${parentMenu?.getTitle()} > $subTitle"
    }

    override fun build(builder: DialogRegistryEntry.Builder) {
        builder
            .base(
                DialogBase.builder(Component.text(getTitle()).decorate(TextDecoration.BOLD))
                    .pause(false)
                    .afterAction(DialogBase.DialogAfterAction.NONE)
                    .body(getBody())
                    .build()
            )
            .type(DialogType.multiAction(getActionButtons(), getExitActionButton(), columns))
    }

    protected abstract fun getBody(): List<DialogBody>

    protected abstract fun getActionButtons(): List<ActionButton>

    private fun getExitActionButton(): ActionButton {
        return ActionButton.create(
            Component.text("Done"),
            null,
            backButtonSize,
            DialogAction.staticAction(ClickEvent.callback { _ -> parentMenu?.showDialog(player) }),
        )
    }

    fun getNavigationButton(): ActionButton {
        beforeDialog()
        return ActionButton.create(
            getNavigationButtonContent(),
            Component.text(getNavigationTooltip()),
            300,
            DialogAction.customClick(
                { _, _ -> showDialog() },
                ClickCallback.Options.builder().uses(1).build(),
            ),
        )
    }

    abstract fun getNavigationButtonContent(): Component

    abstract fun getNavigationTooltip(): String
}
