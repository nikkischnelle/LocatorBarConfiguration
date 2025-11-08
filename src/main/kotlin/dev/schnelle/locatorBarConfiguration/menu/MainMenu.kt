package dev.schnelle.locatorBarConfiguration.menu

import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.implementations.InfoMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.implementations.MainSubMenu
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import org.bukkit.entity.Player
import java.time.Duration

/**
 * The "main menu" which shows buttons to navigate to the MainSubMenu and the info screen.
 *
 * Necessary because to use dialogs in the pause screen, they have to be built by the time the players join.
 * Meant to be registered in registry during bootstrap.
 */
@Suppress("UnstableApiUsage")
class MainMenu : AbstractMenu() {
    override fun build(builder: DialogRegistryEntry.Builder) {
        builder
            .base(
                DialogBase
                    .builder(Component.text("Locator Bar Configuration"))
                    .pause(false)
                    .afterAction(DialogBase.DialogAfterAction.NONE)
                    .build(),
            ).type(
                DialogType.multiAction(
                    listOf(getSubMenuButton(), getInfoMenuButton()),
                    ActionButton.create(
                        Component.text("Done"),
                        null,
                        200,
                        DialogAction.customClick(
                            { _, audience -> audience.closeDialog() },
                            ClickCallback.Options
                                .builder()
                                .uses(1)
                                .build(),
                        ),
                    ),
                    1,
                ),
            )
    }

    override fun beforeDialog() {}

    override fun getTitle(): String = "Locator Bar Configuration"

    /**
     * Get button for navigating to MainSubMenu.
     */
    private fun getSubMenuButton(): ActionButton =
        getNavButton(
            Component.text("Configuration"),
            Component.text(
                "Configure how you can see others on the " +
                    "Locator Bar and how others can see you.",
            ),
            ::MainSubMenu,
        )

    /**
     * Get button for navigating to InfoMenu.
     */
    private fun getInfoMenuButton(): ActionButton = getNavButton(Component.text("Info"), null, ::InfoMenu)

    private fun getNavButton(
        label: Component,
        tooltip: Component?,
        menuFactory: (player: Player, parent: AbstractMenu) -> AbstractMenu,
    ): ActionButton {
        return ActionButton.create(
            label,
            tooltip,
            200,
            DialogAction.customClick(
                { _, audience ->
                    if (audience !is Player) {
                        audience.sendMessage(Component.text("Only players can trigger this."))
                        return@customClick
                    }

                    menuFactory(audience, this).showDialog(audience)
                },
                ClickCallback.Options
                    .builder()
                    .uses(ClickCallback.UNLIMITED_USES)
                    .lifetime(Duration.ofDays(1024L))
                    .build(),
            ),
        )
    }
}
