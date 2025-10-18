package dev.schnelle.locatorBarConfiguration

import dev.schnelle.locatorBarConfiguration.menu.MainMenu
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.event.RegistryEvents
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import net.kyori.adventure.key.Key

@Suppress("UnstableApiUsage", "Unused")
class Bootstrapper : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        context.logger.debug("LocatorBarConfiguration bootstrap started.")

        context.lifecycleManager.registerEventHandler(
            RegistryEvents.DIALOG.compose().newHandler { event ->
                event.registry().register(
                    TypedKey.create(RegistryKey.DIALOG, Key.key("lbc:menu"))
                ) { builder ->
                    //                    MenuDialogBuilder.build(builder)
                    MainMenu().build(builder)
                }
            }
        )

        context.lifecycleManager.registerEventHandler(
            LifecycleEvents.DATAPACK_DISCOVERY.newHandler { event ->
                try {
                    val uri: URI = javaClass.getResource("/datapack")!!.toURI()
                    event.registrar().discoverPack(uri, "provided")
                } catch (e: URISyntaxException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        )
    }
}
