package net.bossdragon.system

import com.artemis.Entity
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.events.PlayerCollidesEnemyEvent
import net.bossdragon.system.base.collision.CollisionDetectionSystem
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.base.collision.messaging.CollisionExitListener
import net.bossdragon.util.operations.funcs.*
import se.feomedia.orion.OperationFactory.*
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import net.bossdragon.enums.CollisionGroups as CG

class CollisionSystem : CollisionDetectionSystem(true) {
    lateinit var mVelocity: M<Velocity>

    override fun initialize() {
        relations.connectGroups(CG.CHARACTER, CG.ENEMY or CG.WALL)
        relations.connectGroups(CG.BURNING_AREA, CG.CHARACTER or CG.ENEMY)
        relations.connectGroups(CG.FIREBALL, CG.CHARACTER or CG.ENEMY)
        relations.connectGroups(CG.BULLET, CG.DRAGON)
    }

    override fun inserted(e: Entity) {
        val collider = mCollider[e]

        if (collider.isInGroup(CG.CHARACTER)) {
            collider.enterListener = playerColliding
            collider.exitListener = playerCollisionExit
        }
        else if (collider.isInGroup(CG.FIREBALL)) {
            collider.enterListener = fireballColliding
        }
    }

    val playerColliding = CollisionEnterListener { e: Int, other: Int ->
        val collider = mCollider[other]

        if (collider.isInGroup(CG.WALL)) {
            // TODO
        }

        if (collider.isInGroup(CG.ENEMY)) {
            events!!.dispatch(PlayerCollidesEnemyEvent::class.java)
                .setup(e, other)
        }
    }

    val playerCollisionExit = CollisionExitListener { e: Int, other: Int ->
        val collider = mCollider[other]
    }

    val fireballColliding = CollisionEnterListener { fireball, other ->
        val collider = mCollider[other]

        if (collider.isInGroup(CG.CHARACTER)) {
            mVelocity[fireball].immediateStop()

            repeat(
                sequence(
                    rotateBy(+360f, 1f, Interpolation.linear),
                    rotateBy(-360f, 1f, Interpolation.linear)
                )
            ).register(world, fireball)
        }
    }
}
