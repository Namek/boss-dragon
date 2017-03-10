package net.bossdragon.component

import com.artemis.Component
import com.badlogic.gdx.math.Vector2
import net.bossdragon.enums.C
import net.bossdragon.util.ActionTimer

class Player : Component() {
    val slide = ActionTimer(C.Player.SlideDuration)
    val slideCooldown = ActionTimer(C.Player.SlideCooldownDuration)

    var slideDirX = 0
    var slideDirY = 0

    val isSliding: Boolean
        get() = slide.isRunning

    val canStartSlide: Boolean
        get() = !isSliding && !slideCooldown.isRunning
}
