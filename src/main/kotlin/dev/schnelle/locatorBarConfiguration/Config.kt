package dev.schnelle.locatorBarConfiguration

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.Plugin
import java.lang.IllegalStateException

data class ColorConfig(
    val name: String,
    val displayName: String,
    val namedTextColor: NamedTextColor,
)

class Config(
    private val plugin: Plugin,
) {
    companion object {
        @Volatile private var instance: Config? = null

        fun init(plugin: Plugin) {
            synchronized(this) {
                instance = Config(plugin)
            }
        }

        fun getInstance(): Config = instance ?: throw IllegalStateException("Config was not initialized.")
    }

    private lateinit var colors: List<ColorConfig>
    private lateinit var transmitRanges: List<Double>
    private lateinit var receiveRanges: List<Double>

    init {
        plugin.saveDefaultConfig()
        reload()
    }

    fun reload() {
        plugin.reloadConfig()
        colors =
            plugin.config.getMapList("iconColors").mapNotNull {
                val name = it["name"] as String
                val color =
                    NamedTextColor.NAMES.value(name)
                if (color != null) {
                    ColorConfig(
                        name,
                        it["displayName"] as String,
                        color,
                    )
                } else {
                    plugin.logger.warning(
                        "Color $name from config is not a valid color. Check the default config" +
                            "for all available colors. Ignoring.",
                    )
                    null
                }
            }

        transmitRanges = plugin.config.getDoubleList("transmitRanges")
        receiveRanges =
            if (!plugin.config.isSet("receiveRanges")) {
                transmitRanges.toList()
            } else {
                plugin.config.getDoubleList("receiveRanges")
            }
    }

    fun getColors(): List<ColorConfig> = colors

    fun getColorDisplayName(name: String): String? = colors.find { it.name == name }?.displayName

    fun getTransmitRanges(): List<Double> = transmitRanges

    fun getReceiveRanges(): List<Double> = receiveRanges
}
