package net.bossdragon.component

import com.artemis.PooledComponent
import com.badlogic.gdx.ai.steer.behaviors.*
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity
import com.badlogic.gdx.math.Vector2
import net.bossdragon.util.ai.SteerableEntity

/**
 *
 */
class FightAI : PooledComponent() {
    val steerable = SteerableEntity()

    lateinit var steering: BlendedSteering<Vector2>

    // blended steering:
    lateinit var avoidEachOther: CollisionAvoidance<Vector2>
    lateinit var avoidPlayer: Flee<Vector2>
    lateinit var seekPlayer: Arrive<Vector2>

    // dynamic settings for behevaiors
    lateinit var otherEnemyProximity: RadiusProximity<Vector2>


    override fun reset() {
    }
}
