package dev.schnelle.locatorBarConfiguration.menu.submenus.range

/**
 * The max range minecraft allows for transmit and receive range.
 */
const val MAX_RANGE = 6.0E7

/**
 * Convert range in the range menus to a readable string.
 *
 * e.g. converts 1000.0 to "1k"
 *
 * @param value the value to convert
 * @return string representing the value
 */
fun rangeToStringK(value: Double): String =
    when {
        value >= MAX_RANGE -> "Infinite"
        value >= 1_000 -> String.format("%.1fk", value / 1_000)
        else -> value.toString()
    }
