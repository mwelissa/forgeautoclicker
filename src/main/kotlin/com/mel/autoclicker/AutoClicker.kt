package com.mel.autoclicker

import com.mel.autoclicker.commands.AutoClickerCommand
import com.mel.autoclicker.core.Config
import com.mel.autoclicker.events.packet.PacketEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
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
        Config.initialize()
        AutoClickerCommand.register()
        MinecraftForge.EVENT_BUS.register(PacketEvent)
        MinecraftForge.EVENT_BUS.register(ShowCps)
    }


    object ShowCps {
        var rightCps = mutableListOf<Long>()
        var leftCps = mutableListOf<Long>()

        @SubscribeEvent
        fun onRender(event: RenderGameOverlayEvent.Text) {
            rightCps.removeIf { System.currentTimeMillis() - it > 1000 }
            leftCps.removeIf { System.currentTimeMillis() - it > 1000 }
            if (!Config.showCps) return
            val fr = Minecraft.getMinecraft().fontRendererObj
            val widthMiddle = ScaledResolution(Minecraft.getMinecraft()).scaledWidth / 2
            val height = (ScaledResolution(Minecraft.getMinecraft()).scaledHeight - fr.FONT_HEIGHT) / 2
            // left click
            fr.drawStringWithShadow(leftCps.size.toString(), widthMiddle - 6f - fr.getStringWidth(leftCps.size.toString()), height.toFloat(), Color.WHITE.rgb)
            // right click
            fr.drawStringWithShadow(rightCps.size.toString(), widthMiddle + 8f, height.toFloat(), Color.WHITE.rgb)
        }

        fun addRight() {
            rightCps.add(System.currentTimeMillis())
        }

        fun addLeft() {
            leftCps.add(System.currentTimeMillis())
        }
    }
}
