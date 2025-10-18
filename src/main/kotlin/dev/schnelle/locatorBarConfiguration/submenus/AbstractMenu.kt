package dev.schnelle.locatorBarConfiguration.submenus

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
abstract class AbstractMenu {
    fun showDialog(player: Player) {
        beforeDialog()
        val dialog = Dialog.create { builder -> build(builder.empty()) }
        player.showDialog(dialog)
    }

    abstract fun build(builder: DialogRegistryEntry.Builder)

    abstract fun beforeDialog()

    abstract fun getTitle(): String
}
