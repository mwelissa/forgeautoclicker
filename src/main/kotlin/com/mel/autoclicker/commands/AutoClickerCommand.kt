package com.mel.autoclicker.commands

import com.mel.autoclicker.AutoClicker
import com.mel.autoclicker.core.Config
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import net.minecraftforge.common.MinecraftForge

object AutoClickerCommand: Command("autoclicker") {

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(Config.gui())
    }
}