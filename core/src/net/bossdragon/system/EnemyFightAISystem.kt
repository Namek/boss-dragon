package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.EntitySystem
import com.artemis.managers.TagManager
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.steer.SteeringAcceleration
import com.badlogic.gdx.ai.steer.behaviors.*
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering.BehaviorAndWeight
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.Enemy
import net.bossdragon.component.FightAI
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.enums.Tags
import net.bossdragon.util.ai.BlendedSteering
import net.bossdragon.util.ai.LocationableEntity
import net.bossdragon.util.ai.SteerableEntity
import net.bossdragon.util.ai.behaviors.FleeWhenClose
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 * Rules for AI:
 *   * go straight for player character
 *   * if there are at least X other enemies near the player character then don't approach
 */
class EnemyFightAISystem : EntitySystem(
    Aspect.all(
        Enemy::class.java,
        FightAI::class.java,
        Position::class.java,
        Velocity::class.java
    )
) {
    lateinit var mFightAI: M<FightAI>
    lateinit var mVelocity: M<Velocity>
    lateinit var mPosition: M<Position>
    lateinit var tags: TagManager
    lateinit var collisionSystem: CollisionSystem

    private lateinit var playerEntity: Entity
    private lateinit var playerEntityLocation: LocationableEntity
    private lateinit var playerEntitySteerable: SteerableEntity
    private lateinit var playerEntitySteerables: Iterable<SteerableEntity>

    private var aiStrategyCooldown = 0f


    override fun begin() {
        playerEntity = tags.getEntity(Tags.Player)
        playerEntityLocation = LocationableEntity(mPosition[playerEntity], mVelocity[playerEntity])

        playerEntitySteerable = SteerableEntity()
        playerEntitySteerable.collisionSystem = collisionSystem
        playerEntitySteerable.entity = playerEntity
        playerEntitySteerable.vel = mVelocity[playerEntity]

        playerEntitySteerables = arrayListOf(playerEntitySteerable)
    }

    private val allSteerableEnemies = ArrayList<SteerableEntity>()
    private val steeringOutput = SteeringAcceleration<Vector2>(Vector2());


    override fun inserted(e: Entity) {
        val ai = mFightAI[e]
        val vel = mVelocity[e]

        ai.steerable.entity = e
        ai.steerable.vel = vel
        ai.steerable.collisionSystem = collisionSystem

        // TODO add obstacle steering - the map

        // priority #1. player chase
        ai.steering = BlendedSteering(ai.steerable)

        ai.avoidPlayer = FleeWhenClose(ai.steerable, playerEntityLocation, C.Enemy.AI.DistanceWhenWatching)
        ai.steering.add(ai.avoidPlayer, 3f)

        ai.seekPlayer = Arrive<Vector2>(ai.steerable, playerEntityLocation)
        ai.steering.add(ai.seekPlayer, 1f)

        // priority #2. avoid each other
        ai.otherEnemyProximity = RadiusProximity<Vector2>(ai.steerable, allSteerableEnemies, C.Enemy.AI.AvoidanceRadius)
        ai.avoidEachOther = CollisionAvoidance<Vector2>(ai.steerable, ai.otherEnemyProximity)
        ai.steering.add(ai.avoidEachOther, 1f)

        allSteerableEnemies.add(ai.steerable)
    }

    override fun removed(e: Entity) {
        val ai = mFightAI[e]
        ai.steerable.collisionSystem = null
        ai.steerable.entity = null
        ai.steerable.vel = null

        allSteerableEnemies.remove(ai.steerable)
    }

    override fun processSystem() {
        val entities = this.entities.data
        val n = this.entities.size()

        GdxAI.getTimepiece().update(world.delta)

        if (n == 0)
            return

        val playerPos = mPosition[playerEntity].currentPos

        // first, update who's going to attack
        if (aiStrategyCooldown > 0) {
            aiStrategyCooldown -= world.getDelta()
        }
        else {
            aiStrategyCooldown = C.Enemy.AI.StrategyCooldown

            var attackerCount = 0

            for (i in 0..n-1) {
                val e = entities[i]
                val ai = mFightAI[e]

                if (!ai.avoidPlayer.isEnabled)
                    attackerCount += 1
            }

            // update attackers only if there are less than a minimum
            val lackingAttackers = C.Enemy.AI.EnemyCountForStabbingDirectly - attackerCount

            for (i in 0..Math.min(n, lackingAttackers)-1) {
                val e = entities[i]
                val ai = mFightAI[e]

                ai.avoidPlayer.setEnabled(false)
            }
        }

        // now, update movement for each enemy
        for (i in 0..n-1) {
            val e = entities[i]
            val ai = mFightAI[e]
            val pos = mPosition[e]
            val vel = mVelocity[e]

            val isInPlayerRange = playerPos.dst(pos.currentPos) < C.Enemy.AI.PlayerCloseRangeRadius

            ai.steering.calculateSteering(steeringOutput)
            vel.acceleration.set(steeringOutput.linear)

            vel.frictionOn = isInPlayerRange
        }
    }
}
