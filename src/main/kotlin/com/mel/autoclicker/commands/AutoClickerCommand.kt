package com.mel.autoclicker.commands

import com.mel.autoclicker.core.Config
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler

object AutoClickerCommand: Command("autoclicker") {

    override val commandAliases: Set<Alias>
        get() = setOf(Alias("ac"))

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(Config.gui())
    }
}