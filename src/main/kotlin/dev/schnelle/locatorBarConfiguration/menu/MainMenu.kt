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

    private fun getSubMenuButton(): ActionButton {
        return ActionButton.create(
            Component.text("Configuration"),
            Component.text(
                "Configure how you can see others on the " +
                    "Locator Bar and how others can see you.",
            ),
            200,
            DialogAction.customClick(
                { _, audience ->
                    if (audience !is Player) {
                        audience.sendMessage(Component.text("Only players can trigger this."))
                        return@customClick
                    }
                    MainSubMenu(audience, this).showDialog()
                },
                ClickCallback.Options
                    .builder()
                    .uses(ClickCallback.UNLIMITED_USES)
                    .lifetime(Duration.ofDays(1024L))
                    .build(),
            ),
        )
    }

    private fun getInfoMenuButton(): ActionButton {
        return ActionButton.create(
            Component.text("Info"),
            null,
            200,
            DialogAction.customClick(
                { _, audience ->
                    if (audience !is Player) {
                        audience.sendMessage(Component.text("Only players can trigger this."))
                        return@customClick
                    }

                    InfoMenu(audience, this).showDialog()
                },
                ClickCallback.Options
                    .builder()
                    .uses(ClickCallback.UNLIMITED_USES)
                    .lifetime(Duration.ofDays(1024L))
                    .build(),
            ),
        )
    }

    override fun beforeDialog() {}

    override fun getTitle(): String = "Locator Bar Configuration"
}
