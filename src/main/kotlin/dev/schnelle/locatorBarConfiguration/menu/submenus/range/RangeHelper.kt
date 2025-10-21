package dev.schnelle.locatorBarConfiguration.menu.submenus.range

const val MAX_RANGE = 6.0E7

fun rangeToStringK(value: Double): String =
    when {
        value == MAX_RANGE -> "Infinite"
        value >= 1_000 -> String.format("%.1fk", value / 1_000)
        else -> value.toString()
    }
