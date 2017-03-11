package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.systems.IntervalEntitySystem
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.Enemy
import net.bossdragon.component.FightAI
import net.bossdragon.component.render.Colorized
import net.bossdragon.enums.C
import net.bossdragon.util.operations.funcs.*
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import se.feomedia.orion.OperationFactory.*

/**
 *
 */
class EnemySpawnSystem : IntervalEntitySystem(
    Aspect.all(Enemy::class.java)
    , C.Enemy.SpawnCooldown
) {
    lateinit var mColorized: M<Colorized>
    lateinit var mFightAI: M<FightAI>

    lateinit var entityFactory: EntityFactorySystem

    private var lastSpawnpointIndex = 0

    private val startPoint = Vector2()

    override fun processSystem() {
        val shouldSpawn = entities.size() < C.Enemy.MaxEnemyCount

        if (shouldSpawn) {
            lastSpawnpointIndex++

            if (lastSpawnpointIndex >= C.Map.SpawnLandPoints.size) {
                lastSpawnpointIndex = 0
            }

            val landPoint = C.Map.SpawnLandPoints[lastSpawnpointIndex]
            val flyDir = C.Map.SpawnFlyDirections[lastSpawnpointIndex]

            startPoint.set(flyDir)
                .scl(-1f * C.Enemy.SpawnFlyDistance)
                .add(landPoint)

            val e = entityFactory.createEnemy(startPoint)
            mColorized.create(e).color.a = 0f

            sequence(
                opacityTo(1f, 0.7f, Interpolation.bounceIn),
                moveTo(landPoint, 0.7f, Interpolation.circleOut),
                call(Runnable {
                    mFightAI.create(e)
                })
            )
                .register(world, e.id)
        }
    }

}
