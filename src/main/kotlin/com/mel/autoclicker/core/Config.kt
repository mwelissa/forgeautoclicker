package com.mel.autoclicker.core

import com.mel.autoclicker.AutoClicker
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.minecraft.util.MathHelper
import java.io.File
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

object Config: Vigilant(File("./config/${AutoClicker.MODID}config.toml")) {

    init {
        initialize()
    }
    @Property(type = PropertyType.CHECKBOX, name = "Show CPS", category = "Auto Clicker")
    var showCps: Boolean = false

    @Property(type = PropertyType.CHECKBOX, name = "Left Click Auto Clicker", category = "Auto Clicker")
    var leftEnabled: Boolean = false

    @Property(type = PropertyType.SLIDER, name = "Left Click CPS", min = 1, max = 20, category = "Auto Clicker")
    var leftCps: Int = 20

    @Property(type = PropertyType.SLIDER, name = "Left Click Variance", min = -5, max = 5, category = "Auto Clicker")
    var leftVariance: Int = 0

    @Property(type = PropertyType.CHECKBOX, name = "Right Click Auto Clicker", category = "Auto Clicker")
    var rightEnabled: Boolean = false

    @Property(type = PropertyType.SLIDER, name = "Right Click CPS", min = 1, max = 20, category = "Auto Clicker")
    var rightCps: Int = 20

    @Property(type = PropertyType.SLIDER, name = "Right Click Variance", min = -5, max = 5, category = "Auto Clicker")
    var rightVariance: Int = 0

    fun getRightClickDelay(): Int {
        val cps = MathHelper.clamp_double(rightCps.toDouble() + (Math.random() * rightVariance),1.0, 20.0)
        val temp = 20 / cps
        var ticks = floor(temp)
        val random = Math.random()
        if (random < temp % 1) ticks++
        return ticks.toInt()
    }

    fun getLeftClickDelay(): Int {
        val cps = MathHelper.clamp_double(leftCps.toDouble() + (Math.random() * leftVariance),1.0, 20.0)
        val temp = 20 / cps
        var ticks = floor(temp)
        val random = Math.random()
        if (random < temp % 1) ticks++
        return ticks.toInt()
    }
}