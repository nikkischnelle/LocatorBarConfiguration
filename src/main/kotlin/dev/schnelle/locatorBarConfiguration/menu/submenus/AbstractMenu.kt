package dev.schnelle.locatorBarConfiguration.menu.submenus

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
abstract class AbstractMenu {
    /**
     * Show the menu to a player.
     *
     * @param player The Player to show the menu to.
     */
    fun showDialog(player: Player) {
        beforeDialog()
        val dialog = Dialog.create { builder -> build(builder.empty()) }
        player.showDialog(dialog)
    }

    /**
     * Build the menu's dialog using the builder.
     *
     * @param builder the builder to use
     */
    abstract fun build(builder: DialogRegistryEntry.Builder)

    /**
     * Code to execute before the dialog is shown to players.
     */
    protected abstract fun beforeDialog()

    /**
     * Get the menu's title.
     *
     * @return title of the menu
     */
    abstract fun getTitle(): String
}
