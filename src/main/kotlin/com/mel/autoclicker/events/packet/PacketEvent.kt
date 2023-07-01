package com.mel.autoclicker.events.packet

import com.mel.autoclicker.AutoClicker
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object PacketEvent {

    @SubscribeEvent
    fun onJoinServer(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        event.manager.channel().pipeline().addAfter(
            "fml:packet_handler",
            "${AutoClicker.MODID}_packet_handler",
            CustomChannelDuplexHandler()
        )
    }
}