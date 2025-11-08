package dev.schnelle.locatorBarConfiguration

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.Plugin
import java.lang.IllegalStateException

/**
 * Configuration of a single color.
 *
 * Includes a display name and the TextColor representation.
 */
data class ColorConfig(
    val displayName: String,
    val textColor: TextColor,
) {
    companion object {
        /**
         * Get a ColorConfig from a Map.
         *
         * The map has to be formatted like:
         * ```yaml
         * color: green
         * displayName: Green
         * ```
         *
         * @param map the map to parse
         *
         * @return the ColorConfig with parse exceptions wrapped in a result.
         */
        fun fromMap(map: Map<*, *>): Result<ColorConfig> {
            try {
                val colorString = map["color"] as String
                val displayName = map["displayName"] as String
                val color =
                    NamedTextColor.NAMES.value(colorString)
                        ?: TextColor.fromHexString(colorString)
                        ?: return Result.failure(
                            IllegalStateException(
                                "Color $colorString is neither a valid minecraft color name " +
                                    "nor a valid hex code. Ignoring.",
                            ),
                        )

                return Result.success(ColorConfig(displayName, color))
            } catch (_: Exception) {
                return Result.failure(
                    IllegalStateException(
                        "Color items in iconColors need a `color` and `displayName`.",
                    ),
                )
            }
        }
    }
}

/**
 * General parsed configuration.
 *
 * Includes a Singleton-type architecture so `Config.getInstance()` can be called from anywhere to get the current one.
 */
class Config(
    private val plugin: Plugin,
) {
    companion object {
        @Volatile private var instance: Config? = null

        /**
         * Initialize the configuration instance.
         */
        fun init(plugin: Plugin) {
            synchronized(this) {
                instance = Config(plugin)
            }
        }

        /**
         * Get initialized config.
         *
         * @throws IllegalStateException if the config has not been initialized yet
         */
        fun getInstance(): Config = instance ?: throw IllegalStateException("Config was not initialized.")
    }

    private lateinit var colors: List<ColorConfig>
    private lateinit var transmitRanges: List<Double>
    private lateinit var receiveRanges: List<Double>
    private lateinit var lboSection: ConfigurationSection

    init {
        plugin.saveDefaultConfig()
        reload()
    }

    /**
     * Reload the config from the configuration file.
     * the current instance will contain the reloaded values
     */
    fun reload() {
        plugin.reloadConfig()
        colors =
            plugin.config.getMapList("iconColors").mapNotNull { map ->
                ColorConfig.fromMap(map).fold(
                    onSuccess = { it },
                    onFailure = {
                        plugin.logger.warning(it.message)
                        null
                    },
                )
            }

        transmitRanges = plugin.config.getDoubleList("transmitRanges")
        receiveRanges =
            if (!plugin.config.isSet("receiveRanges")) {
                transmitRanges.toList()
            } else {
                plugin.config.getDoubleList("receiveRanges")
            }
        lboSection = plugin.config.getConfigurationSection("lboMigration")!!
    }

    fun getEnableLBOChecks(): Boolean {
        if (!lboSection.isSet("compatibilityChecks")) {
            return true
        }

        return lboSection.getBoolean("compatibilityChecks")
    }

    fun getEnableLBOMigration(): Boolean = lboSection.getBoolean("enabled")

    fun getLBOMigrationUserNotification(): String = lboSection.getString("userNotification") ?: ""

    fun getColors(): List<ColorConfig> = colors

    fun getColorDisplayName(color: TextColor): String? = colors.find { it.textColor == color }?.displayName

    fun getTransmitRanges(): List<Double> = transmitRanges

    fun getReceiveRanges(): List<Double> = receiveRanges
}
