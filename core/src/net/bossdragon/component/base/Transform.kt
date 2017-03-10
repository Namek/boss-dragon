package net.bossdragon.component.base

import com.artemis.Component
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

/**
 * Graphical position and rotation.
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


    fun xy(x: Float, y: Float): Transform {
        position.set(x, y)
        return this
    }

    fun xy(pos: Vector2): Transform {
        position.set(pos)
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
