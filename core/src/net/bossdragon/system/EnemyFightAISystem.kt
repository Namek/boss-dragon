package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.EntitySystem
import com.artemis.managers.TagManager
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.Enemy
import net.bossdragon.component.FightAI
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.enums.Tags
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

    private lateinit var playerEntity: Entity

    private var fighters = ArrayList<Int>()
    private var aiStrategyCooldown = 0f
    private var dir = Vector2()


    override fun begin() {
        playerEntity = tags.getEntity(Tags.Player)
    }

    override fun processSystem() {
        val entities = entities.data
        val n = this.entities.size()

        if (n == 0)
            return

        val playerPos = mPosition[playerEntity].currentPos

        // group strategy
        if (aiStrategyCooldown > 0) {
            aiStrategyCooldown -= world.getDelta()
        }
        else {
            fighters.clear()

            // first find enemies in close range to player
            for (i in 0..n-1) {
                val entity = entities[i]
                val pos = mPosition[entity].currentPos
                val dist = playerPos.dst2(pos)

                if (dist <= C.Enemy.AI.PlayerCloseRangeRadius) {
                    fighters.add(entity.id)
                }

                // mark all enemies as non-fighters (prevent looping later)
                val ai = mFightAI[entity]
                ai.trackPlayer = false
            }

            // look for more entities to target player, if needed
            var anythingAddedThisRound = true
            while (fighters.size < C.Enemy.AI.MaxEntitiesTargettingPlayer && anythingAddedThisRound) {
                // find the closest free one and add it to entities in range
                anythingAddedThisRound = false
                var closestDist = Float.MAX_VALUE
                var closestEntity: Entity? = null
                for (i in 0..n - 1) {
                    val entity = entities[i]

                    if (fighters.indexOf(entity.id) < 0) {
                        val pos = mPosition[entity].currentPos
                        val dist = playerPos.dst2(pos)

                        if (dist <= closestDist) {
                            closestDist = dist
                            closestEntity = entity
                        }
                    }
                }

                if (closestEntity != null) {
                    fighters.add(closestEntity.id)
                    anythingAddedThisRound = true
                }
            }

            for (entity in fighters) {
                val ai = mFightAI[entity]
                ai.trackPlayer = true
            }

            aiStrategyCooldown = C.Enemy.AI.StrategyCooldown
        }

        // move marked enemies towards player
        for (i in 0..n-1) {
            val entity = entities[i]
            val velocity = mVelocity[entity]
            val ai = mFightAI[entity]

            if (ai.trackPlayer) {
                val pos = mPosition[entity].currentPos

                // TODO check if he's close to player, if yes, then stab him

                velocity.setMovement(dir.set(playerPos).sub(pos).nor())
            }
            else {
                velocity.immediateStop()
            }
        }
    }
}
