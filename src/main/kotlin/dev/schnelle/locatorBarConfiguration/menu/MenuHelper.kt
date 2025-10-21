package dev.schnelle.locatorBarConfiguration.menu

import dev.schnelle.locatorBarConfiguration.Config
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
fun bodyFromString(vararg texts: String): MutableList<DialogBody> =
    texts.map { DialogBody.plainMessage(Component.text(it)) }.toMutableList()

@Suppress("UnstableApiUsage")
fun createdByMessage(skullItem: ItemStack): List<DialogBody> =
    listOf(
        DialogBody.plainMessage(
            Component
                .text("Created by")
                .appendNewline()
                .append(Component.text("nikkischnelle").color(NamedTextColor.DARK_PURPLE)),
        ),
        DialogBody.item(skullItem).showTooltip(false).build(),
    )

private val defaultColorTranslations =
    mapOf(
        "dark_red" to "Dark Red",
        "green" to "Green",
        "dark_green" to "Dark Green",
        "black" to "Black",
        "dark_purple" to "Dark Purple",
        "gold" to "Gold",
        "red" to "Red",
        "yellow" to "Yellow",
        "dark_blue" to "Dark Blue",
        "aqua" to "Aqua",
        "gray" to "Gray",
        "light_purple" to "Light Purple",
        "blue" to "Blue",
        "white" to "White",
        "dark_aqua" to "Dark Aqua",
        "dark_gray" to "Dark Gray",
    )

fun getColorNameComponent(color: String): Component = Component.text(getColorName(color))

fun getColorName(color: String): String =
    Config.getInstance().getColorDisplayName(color)
        ?: defaultColorTranslations[color] ?: color
