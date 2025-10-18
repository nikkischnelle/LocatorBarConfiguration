package dev.schnelle.locatorBarConfiguration

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.logging.Level

class Scheduler(
    private val plugin: Plugin,
) {
    val isFolia: Boolean =
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer")
            true
        } catch (_: ClassNotFoundException) {
            false
        }

    fun runAsyncTask(
        messageForErrors: String,
        runnable: () -> Unit,
    ) {
        val safeRunnable = {
            try {
                runnable()
            } catch (e: Exception) {
                plugin.logger.log(Level.WARNING, messageForErrors, e)
            }
        }

        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin) { _ -> safeRunnable() }
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, safeRunnable)
        }
    }
}
