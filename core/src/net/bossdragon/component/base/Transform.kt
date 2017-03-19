package net.bossdragon.component.base

import com.artemis.Component
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

/**
 * Graphical position, rotation and flip.
 * @see Position
 */
class Transform : PooledComponent() {
    object companion {
        /** UP on screen */
        val DEFAULT_DIRECTION = Vector2(0f, 1f)
    }

    /** Current position to render. It's directly transformed from [Position] component. */
    val position = Vector2()

    /** Additional displacement to position. Usually used for graphics puroses, like head bobbing. */
    val displacement = Vector2()

    /** degrees */
    var rotation = 0f

    /** used for [rotation]. range: [0, 1] */
    var originX = 0.5f

    /** used for [rotation]. range: [0, 1] */
    var originY = 0.5f

    var flipX = false
    var flipY = false


    fun xy(x: Float, y: Float): Transform {
        position.set(x, y)
        return this
    }

    fun xy(pos: Vector2): Transform {
        position.set(pos)
        return this
    }

    fun flip(x: Boolean, y: Boolean): Transform {
        flipX = x
        flipY = y
        return this
    }

    fun origin(x: Float, y: Float): Transform {
        originX = x
        originY = y
        return this
    }

    override fun reset() {
        position.setZero()
        displacement.setZero()
        rotation = 0f
    }

    fun toDirection(outDir: Vector2): Vector2 {
        return outDir
            .set(companion.DEFAULT_DIRECTION)
            .rotate(rotation)
    }

}
