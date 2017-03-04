package net.bossdragon.component.base

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

public class Velocity : PooledComponent() {
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
}
