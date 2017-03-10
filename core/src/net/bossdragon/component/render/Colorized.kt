package net.bossdragon.component.render

import com.artemis.PooledComponent
import com.badlogic.gdx.graphics.Color

/**
 * Color including transparency channel.
 */
class Colorized : PooledComponent() {
    val color = Color.WHITE.cpy()

    override fun reset() {
        color.set(Color.WHITE)
    }
}
