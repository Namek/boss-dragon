package net.bossdragon.system

import com.artemis.Entity
import net.bossdragon.component.base.Velocity
import net.bossdragon.system.base.collision.CollisionDetectionSystem
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.base.collision.messaging.CollisionExitListener
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import net.bossdragon.enums.CollisionGroups as CG

class CollisionSystem : CollisionDetectionSystem(true) {
    override fun initialize() {
        relations.connectGroups(CG.PLAYER, CG.ENEMY or CG.WALL)
        relations.connectGroups(CG.BURNING_AREA, CG.PLAYER or CG.ENEMY)
        relations.connectGroups(CG.FIREBALL, CG.PLAYER or CG.ENEMY)
        relations.connectGroups(CG.BULLET, CG.DRAGON)
    }

    override fun inserted(e: Entity) {
        val collider = mCollider[e]

        if (collider.hasGroup(CG.PLAYER)) {
            collider.enterListener = playerColliding
            collider.exitListener = playerCollisionExit
        }
    }

    val playerColliding = CollisionEnterListener { e: Int, other: Int ->
        val collider = mCollider[other]

        if (collider.hasGroup(CG.ENEMY)) {
            // TODO player collided enemy
        }
    }

    val playerCollisionExit = CollisionExitListener { e: Int, other: Int ->
        val collider = mCollider[other]
    }
}
