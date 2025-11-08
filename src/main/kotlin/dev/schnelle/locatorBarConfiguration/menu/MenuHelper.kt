package dev.schnelle.locatorBarConfiguration.menu

import dev.schnelle.locatorBarConfiguration.ColorConfig
import dev.schnelle.locatorBarConfiguration.Config
import io.papermc.paper.registry.data.dialog.body.DialogBody
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

/**
 * Create a list of DialogBodies by a list of strings.
 *
 * Uses simple Components, no styling. Mostly meant for paragraphs of texts.
 */
@Suppress("UnstableApiUsage")
fun bodyFromString(vararg texts: String): MutableList<DialogBody> =
    texts.map { DialogBody.plainMessage(Component.text(it)) }.toMutableList()

/**
 * Get an unstyled component for a color.
 */
fun getColorNameComponent(color: ColorConfig): Component = Component.text(color.displayName)

/**
 * Get a colors name. Falls back to hex code if no display name is configured.
 */
fun getColorName(color: TextColor): String = Config.getInstance().getColorDisplayName(color) ?: color.asHexString()
