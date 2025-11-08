package dev.schnelle.locatorBarConfiguration

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * Scheduler Helper for easy Folia compatibility.
 */
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

    /**
     * Schedules the specified task to be executed asynchronously after the time delay has passed.
     * @param messageForErrors message that gets logged if an error occurs.
     * @param delaySeconds – The time delay to pass before the task should be executed.
     * @param runnable – Specified task.
     */
    fun runAsyncTask(
        messageForErrors: String,
        delaySeconds: Long,
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
            Bukkit.getAsyncScheduler().runDelayed(plugin, { _ -> safeRunnable() }, delaySeconds, TimeUnit.SECONDS)
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, safeRunnable, delaySeconds * 20L)
        }
    }
}
