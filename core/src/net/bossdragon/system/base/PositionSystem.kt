package net.bossdragon.system.base

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.annotations.Wire
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.PreviousPosition
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.system.base.physics.Physical
import net.bossdragon.system.base.physics.PhysicsSystem


/**
 *
 * System that calculates **desired** position of moving entity
 * based on [Transform.currentPos] and [Velocity.velocity].
 *
 * This system needs a companion system (processing after this one)
 * which will check and modify/copy [Transform.desiredPos] position into [Transform.currentPos].
 * Such companion system would be [PhysicsSystem].
 *
 * @see Transform
 * @see Velocity
 * @see PhysicsSystem
 * @author Namek
 */
@Wire
class PositionSystem : EntityProcessingSystem(
    Aspect.all(Transform::class.java, Velocity::class.java)
        .exclude(Physical::class.java)
) {
    lateinit var pm: ComponentMapper<Transform>
    lateinit var ppm: ComponentMapper<PreviousPosition>
    lateinit var vm: ComponentMapper<Velocity>


    private val tmpVector = Vector2()

    override fun process(e: Entity) {
        val position = pm.get(e)
        val previousPosition = ppm.get(e)
        val velocity = vm.get(e)

        if (previousPosition != null) {
            previousPosition.pos.set(position.currentPos)
        }

        val deltaTime = world.getDelta()
        calculateDesiredPosition(position, velocity, deltaTime)
    }

    fun calculateDesiredPosition(positionComponent: Transform, velocityComponent: Velocity, deltaTime: Float) {
        val velocity = velocityComponent.velocity
        val maxSpeed = velocityComponent.maxSpeed

        // Calculate velocity
        tmpVector.set(velocityComponent.acceleration).scl(deltaTime).add(velocity).limit(maxSpeed)

        if (velocityComponent.frictionOn) {
            val friction = velocityComponent.friction * deltaTime
            val speed = tmpVector.len()

            if (friction < speed) {
                // calculate delta velocity with friction
                tmpVector.nor().scl(-friction)
            }
            else {
                tmpVector.set(velocityComponent.velocity).scl(-1f)
            }

            // Add delta velocity
            velocity.add(tmpVector)
        }
        else {
            velocity.set(tmpVector)
        }

        // Calculate position
        tmpVector.set(velocity).scl(deltaTime)
        positionComponent.desiredPos.add(tmpVector)
    }
}
