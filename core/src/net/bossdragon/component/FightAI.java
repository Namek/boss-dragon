package net.bossdragon.component;

import com.artemis.PooledComponent;
import com.artemis.annotations.DelayedComponentRemoval;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.util.ai.SteerableEntity;

@DelayedComponentRemoval
public class FightAI extends PooledComponent {
    public final SteerableEntity steerable = new SteerableEntity();
    public BlendedSteering<Vector2> steering;

    // blended steering:
    CollisionAvoidance<Vector2> avoidEachOther;
    Flee<Vector2> avoidPlayer;
    Arrive<Vector2> seekPlayer;

    // dynamic settings for behaviors
    RadiusProximity<Vector2> otherEnemyProximity;


    @Override
    protected void reset() {
        steering = null;
        avoidEachOther = null;
        avoidPlayer = null;
        seekPlayer = null;
        otherEnemyProximity = null;
    }
}
