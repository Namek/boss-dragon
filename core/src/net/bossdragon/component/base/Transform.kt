package net.bossdragon.component.base

import com.artemis.Component
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

class Transform : PooledComponent() {
    object companion {
        /** UP on screen */
        val DEFAULT_DIRECTION = Vector2(0f, 1f)
    }

    /** Position set before collision detection. */
    val desiredPos = Vector2()

    /** Finally accepted position, result of collision checks and physical forces. */
    val currentPos = Vector2()

    /** Additional displacement to position. Usually used for graphics puroses, like head bobbing. */
    val displacement = Vector2()

    /** radians */
    var rotation = 0f


    fun xy(x: Float, y: Float): Transform {
        desiredPos.set(x, y)
        currentPos.set(x, y)
        return this
    }

    override fun reset() {
        desiredPos.setZero()
        currentPos.setZero()
        displacement.setZero()
        rotation = 0f
    }

    fun toDirection(outDir: Vector2): Vector2 {
        return outDir
            .set(companion.DEFAULT_DIRECTION)
            .rotateRad(rotation)
    }
}
