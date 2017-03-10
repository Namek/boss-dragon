package net.bossdragon.component.base

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

/**
 * Logical position.
 */
class Position : PooledComponent() {
    /** Position set before collision detection. */
    val desiredPos = Vector2()

    /** Finally accepted position, result of collision checks and physical forces. */
    val currentPos = Vector2()


    fun xy(x: Float, y: Float): Position {
        desiredPos.set(x, y)
        currentPos.set(x, y)

        return this
    }

    fun xy(pos: Vector2): Position {
        desiredPos.set(pos)
        currentPos.set(pos)

        return this
    }


    override fun reset() {
        desiredPos.setZero()
        currentPos.setZero()
    }
}
