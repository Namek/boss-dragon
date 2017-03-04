package net.bossdragon.component.base

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class Size : Component() {
    var width = 1f
    var height = 1f

    fun set(w: Float, h: Float): Size {
        width = w
        height = h
        return this
    }
}
