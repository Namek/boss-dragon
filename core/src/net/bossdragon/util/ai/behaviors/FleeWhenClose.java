package net.bossdragon.util.ai.behaviors;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class FleeWhenClose extends Flee<Vector2> {
    public float closeRadius = 0f;
    public float epsilon = 1f;

    public FleeWhenClose(SteerableEntity owner, Location<Vector2> target, float closeRadius) {
        super(owner, target);
        this.closeRadius = closeRadius;
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
        float diff = target.getPosition().dst(owner.getPosition()) - closeRadius;
        if (diff > epsilon) {
            // stop fleeing off
            steering.linear.setZero();
            steering.angular = 0f;
        }
        else {
            super.calculateRealSteering(steering);
        }

        return steering;
    }
}
