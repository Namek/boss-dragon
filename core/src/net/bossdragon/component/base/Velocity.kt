package net.bossdragon.component.base

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2
import net.bossdragon.system.base.PositionSystem


/**
 * To move entity, play with {@link #acceleration}.
 * Modify {@link #velocity} onlly to immediately stop entity.
 *
 *  Issues:
 *  <ul>
 *      <li>entity doesn't move? Ensure {@code maxSpeed > 0} and friction is off</li>
 *      <li>friction doesn't work? {@code frictionOn = true}</li>
 *  </ul>
 *
 *  @see PositionSystem
 */
class Velocity : PooledComponent() {
    val velocity = Vector2()
    val acceleration = Vector2()

    var maxSpeed: Float = 0f
    var friction: Float = 0f
    var frictionOn: Boolean = false

    override fun reset() {
        velocity.setZero()
        acceleration.setZero()
        maxSpeed = 0f
        friction = 0f
        frictionOn = false
    }

    fun maxSpeed(s: Float): Velocity {
        this.maxSpeed = s
        return this
    }

    fun friction(f: Float): Velocity {
        this.friction = f
        this.frictionOn = true
        return this
    }

    fun immediateStop() {
        velocity.setZero()
        acceleration.setZero()
    }

    fun setMovement(dirX: Int, dirY: Int, accel: Float) {
        val isMoving = dirX + dirY*2 != 0
        frictionOn = !isMoving
        acceleration.set(dirX.toFloat(), dirY.toFloat())

        if (isMoving)
            acceleration.scl(accel)
    }

    fun setMovement(dirX: Int, dirY: Int) {
        setMovement(dirX, dirY, maxSpeed)
    }
}
