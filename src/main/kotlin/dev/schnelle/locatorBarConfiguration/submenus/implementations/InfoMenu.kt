package dev.schnelle.locatorBarConfiguration.submenus.implementations

import com.destroystokyo.paper.profile.ProfileProperty
import dev.schnelle.locatorBarConfiguration.bodyFromString
import dev.schnelle.locatorBarConfiguration.createdByMessage
import dev.schnelle.locatorBarConfiguration.submenus.AbstractMenu
import dev.schnelle.locatorBarConfiguration.submenus.backButtonSize
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import java.util.UUID
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

private const val skullTexture =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGMzMmE5MjlhYmUyZmYyZGY2NDk1NTkwZWNjNDE4Yjc3NzE3MzJmYmU2MTkzNmFmMWI5ZTBhOTRhNGFjMGQ0NSJ9fX0="
private val playerUUID: UUID = UUID.fromString("b4817e5b-acad-4f56-b19a-5b304384557a")

@Suppress("UnstableApiUsage")
class InfoMenu(private val player: Player, private val parent: AbstractMenu) : AbstractMenu() {
    fun showDialog() {
        showDialog(player)
    }

    override fun build(builder: DialogRegistryEntry.Builder) {
        builder
            .base(
                DialogBase.builder(
                        Component.text("${parent.getTitle()} > ${getTitle()}")
                            .decorate(TextDecoration.BOLD)
                    )
                    .pause(false)
                    .afterAction(DialogBase.DialogAfterAction.NONE)
                    .body(getBody())
                    .build()
            )
            .type(DialogType.notice(getExitActionButton()))
    }

    val skullItem =
        ItemStack(Material.PLAYER_HEAD).also {
            val profile = Bukkit.createProfile(playerUUID)
            profile.setProperty(ProfileProperty("textures", skullTexture))
            val meta = (it.itemMeta as SkullMeta)
            meta.playerProfile = profile
            it.itemMeta = meta
        }

    private fun getBody(): List<DialogBody> {
        return bodyFromString(
                "A small plugin that allows players to change their locator bar settings " +
                    "without needing to use commands.",
                "Inspired by RedSyven's Datapack 'Locator Bar Options'",
            )
            .also { it.addAll(createdByMessage(skullItem)) }
    }

    private fun getExitActionButton(): ActionButton {
        return ActionButton.create(
            Component.text("Done"),
            null,
            backButtonSize,
            DialogAction.staticAction(ClickEvent.callback { _ -> parent.showDialog(player) }),
        )
    }

    override fun beforeDialog() {}

    override fun getTitle(): String = "Info"
}
