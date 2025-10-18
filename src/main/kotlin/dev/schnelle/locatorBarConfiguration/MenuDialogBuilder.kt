package dev.schnelle.locatorBarConfiguration

import com.destroystokyo.paper.profile.ProfileProperty
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.type.DialogType
import java.time.Duration
import java.util.UUID
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

private const val skullTexture =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGMzMmE5MjlhYmUyZmYyZGY2NDk1NTkwZWNjNDE4Yjc3NzE3MzJmYmU2MTkzNmFmMWI5ZTBhOTRhNGFjMGQ0NSJ9fX0="
private val playerUUID: UUID = UUID.fromString("b4817e5b-acad-4f56-b19a-5b304384557a")

@Suppress("UnstableApiUsage")
class MenuDialogBuilder {
    companion object {
        fun build(builder: DialogRegistryEntry.Builder) {
            builder
                .base(
                    DialogBase.builder(Component.text("Locator Bar Configuration"))
                        .pause(false)
                        .afterAction(DialogBase.DialogAfterAction.NONE)
                        .build()
                )
                .type(
                    DialogType.multiAction(
                        listOf(getRangeMenuButton(), getInfoMenuButton()),
                        ActionButton.create(
                            Component.text("Close"),
                            null,
                            300,
                            DialogAction.customClick(
                                { _, audience -> audience.closeDialog() },
                                ClickCallback.Options.builder().uses(1).build(),
                            ),
                        ),
                        1,
                    )
                )
        }

        private fun getRangeMenuButton(): ActionButton {
            return ActionButton.create(
                Component.text("Configuration"),
                Component.text(
                    "Configure how you can see others on the " +
                        "Locator Bar and how others can see you."
                ),
                300,
                DialogAction.customClick(
                    { _, audience ->
                        if (audience !is Player) {
                            audience.sendMessage(Component.text("Only players can trigger this."))
                            return@customClick
                        }

                        Menu(audience).show()
                    },
                    ClickCallback.Options.builder()
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
                300,
                DialogAction.customClick(
                    { _, audience ->
                        if (audience !is Player) {
                            audience.sendMessage(Component.text("Only players can trigger this."))
                            return@customClick
                        }
                        showInfoScreen(audience)
                    },
                    ClickCallback.Options.builder()
                        .uses(ClickCallback.UNLIMITED_USES)
                        .lifetime(Duration.ofDays(1024L))
                        .build(),
                ),
            )
        }

        fun showInfoScreen(player: Player) {
            val skullItem =
                ItemStack(Material.PLAYER_HEAD).also {
                    val profile = Bukkit.createProfile(playerUUID)
                    profile.setProperty(ProfileProperty("textures", skullTexture))
                    val meta = (it.itemMeta as SkullMeta)
                    meta.playerProfile = profile
                    it.itemMeta = meta
                }

            player.showDialog(
                Dialog.create { builder ->
                    builder
                        .empty()
                        .base(
                            DialogBase.builder(Component.text("Locator Bar Configuration > Info"))
                                .body(
                                    bodyFromString(
                                            "A small plugin that allows players to change their locator bar settings " +
                                                "without needing to use commands.",
                                            "Inspired by RedSyven's Datapack 'Locator Bar Options'",
                                        )
                                        .also { it.addAll(createdByMessage(skullItem)) }
                                )
                                .pause(false)
                                .afterAction(DialogBase.DialogAfterAction.NONE)
                                .build()
                        )
                        .type(
                            DialogType.notice(
                                ActionButton.create(
                                    Component.text("Back"),
                                    null,
                                    300,
                                    DialogAction.customClick(
                                        { _, _ -> show(player) },
                                        ClickCallback.Options.builder()
                                            .uses(ClickCallback.UNLIMITED_USES)
                                            .lifetime(Duration.ofDays(1024L))
                                            .build(),
                                    ),
                                )
                            )
                        )
                }
            )
        }

        fun show(player: Player) {
            player.showDialog(Dialog.create { builder -> build(builder.empty()) })
        }
    }
}
