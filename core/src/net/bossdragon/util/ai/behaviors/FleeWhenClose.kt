package net.bossdragon.util.ai.behaviors

import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.behaviors.Flee
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import net.bossdragon.util.ai.SteerableEntity

/**
 *
 */
class FleeWhenClose(
    owner: SteerableEntity,
    target: Location<Vector2>,
    var closeRadius: Float = 0f
) : Flee<Vector2>(owner, target)
{
    var epsilon = 1f

    override fun calculateRealSteering(steering: SteeringAcceleration<Vector2>): SteeringAcceleration<Vector2> {
        val diff = target.position.dst(owner.position) - closeRadius
        if (diff > epsilon) {
            // stop fleeing off
            steering.linear.setZero()
            steering.angular = 0f
        }
        else {
            super.calculateRealSteering(steering)
        }

        return steering
    }
}
