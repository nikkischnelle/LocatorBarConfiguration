package dev.schnelle.locatorBarConfiguration.menu

import dev.schnelle.locatorBarConfiguration.ColorConfig
import dev.schnelle.locatorBarConfiguration.Config
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
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

fun getColorNameComponent(color: ColorConfig): Component = Component.text(color.displayName)

fun getColorName(color: TextColor): String = Config.getInstance().getColorDisplayName(color) ?: color.asHexString()
