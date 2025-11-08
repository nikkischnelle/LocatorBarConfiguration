package dev.schnelle.locatorBarConfiguration.menu.submenus.implementations

import com.destroystokyo.paper.profile.ProfileProperty
import dev.schnelle.locatorBarConfiguration.menu.bodyFromString
import dev.schnelle.locatorBarConfiguration.menu.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.menu.submenus.BACK_BUTTON_SIZE
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

// my own skull texture for
private const val SKULL_TEXTURE =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGMzMmE5MjlhYmUyZmYyZGY2NDk1NTkwZWNjNDE4Yjc3NzE3MzJmYmU2MTkzNmFmMWI5ZTBhOTRhNGFjMGQ0NSJ9fX0="
private val AUTHOR_PLAYER_UUID: UUID = UUID.fromString("b4817e5b-acad-4f56-b19a-5b304384557a")

/**
 * Info screen.
 *
 * Show some info about the plugin.
 * I'm not gonna lie. This mostly exists just so the MainSubMenu isn't the only button in the initial menu.
 */
@Suppress("UnstableApiUsage")
class InfoMenu(
    private val player: Player,
    private val parent: AbstractMenu,
) : AbstractMenu() {
    /**
     * ItemStack of my skull for showing in dialog.
     */
    val skullItem =
        ItemStack(Material.PLAYER_HEAD).also {
            val profile = Bukkit.createProfile(AUTHOR_PLAYER_UUID)
            profile.setProperty(ProfileProperty("textures", SKULL_TEXTURE))
            val meta = (it.itemMeta as SkullMeta)
            meta.playerProfile = profile
            it.itemMeta = meta
        }

    /**
     * Show menu to player.
     */
    fun showDialog() {
        showDialog(player)
    }

    override fun build(builder: DialogRegistryEntry.Builder) {
        builder
            .base(
                DialogBase
                    .builder(
                        Component
                            .text("${parent.getTitle()} > ${getTitle()}")
                            .decorate(TextDecoration.BOLD),
                    ).pause(false)
                    .afterAction(DialogBase.DialogAfterAction.NONE)
                    .body(getBody())
                    .build(),
            ).type(DialogType.notice(getExitActionButton()))
    }

    override fun beforeDialog() {}

    override fun getTitle(): String = "Info"

    private fun getBody(): List<DialogBody> =
        bodyFromString(
            "A small plugin that allows players to change their locator bar settings " +
                "without needing to use commands.",
            "Inspired by RedSyven's Datapack 'Locator Bar Options'",
        ).also { it.addAll(createdByMessage(skullItem)) }

    private fun getExitActionButton(): ActionButton =
        ActionButton.create(
            Component.text("Done"),
            null,
            BACK_BUTTON_SIZE,
            DialogAction.staticAction(ClickEvent.callback { _ -> parent.showDialog(player) }),
        )

    private fun createdByMessage(skullItem: ItemStack): List<DialogBody> =
        listOf(
            DialogBody.plainMessage(
                Component
                    .text("Created by")
                    .appendNewline()
                    .append(Component.text("nikkischnelle").color(NamedTextColor.DARK_PURPLE)),
            ),
            DialogBody.item(skullItem).showTooltip(false).build(),
        )
}
