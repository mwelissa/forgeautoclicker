package com.mel.autoclicker

import com.mel.autoclicker.commands.AutoClickerCommand
import com.mel.autoclicker.core.Config
import com.mel.autoclicker.events.packet.PacketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@Mod(
    modid = AutoClicker.MODID,
    version = AutoClicker.VERSION,
    name = AutoClicker.NAME,
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.8.9]",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object AutoClicker: CoroutineScope {
    const val NAME = "Auto Clicker"
    const val MODID = "forgeautoclicker"
    const val VERSION = "1.0"

    override val coroutineContext: CoroutineContext = Executors.newFixedThreadPool(10).asCoroutineDispatcher() + SupervisorJob()

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        Config.preload()
        AutoClickerCommand.register()
        MinecraftForge.EVENT_BUS.register(PacketEvent)
    }
}