package net.bossdragon.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

import net.bossdragon.GdxArtemisGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.x = 0
        config.y = 300
        config.width = 900
        config.height = 600
        LwjglApplication(GdxArtemisGame(), config)
    }
}
