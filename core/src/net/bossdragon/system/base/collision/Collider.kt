package net.bossdragon.system.base.collision

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.base.collision.messaging.CollisionExitListener

/**
 * Collider describes parameters for collision tests of certain entity.
 *
 * @author Namek
 * @see CollisionDetectionSystem
 */
class Collider : PooledComponent() {
    /** Bitset of relations to which this entity belongs.  */
    var groups: Long = 0

    /** For basic shape types supported by [CollisionDetectionSystem], look into [ColliderShape]  */
    var colliderShape = ColliderShape.RECT


    var spatialSize = Vector2()
    var spatialPos = Vector2()


    var enterListener: CollisionEnterListener? = null
    var exitListener: CollisionExitListener? = null


    fun setup(groups: Long, colliderType: Int): Collider {
        this.groups = groups
        this.colliderShape = colliderType

        return this
    }

    fun groups(groups: Long): Collider {
        this.groups = groups
        return this
    }

    fun setCircular(radius: Float): Collider {
        val r2 = radius*2
        colliderShape = ColliderShape.CIRCLE
        spatialSize.set(r2, r2)
        return this
    }

    override fun reset() {
        groups = 0
        colliderShape = ColliderShape.RECT
        enterListener = null
        exitListener = null
        spatialPos.setZero()

    }

    fun isInGroup(group: Long): Boolean {
        return this.groups and group > 0
    }

    fun isInAnyGroup(groups: Long, relations: CollisionGroupsRelations): Boolean {
        return relations.anyRelationExists(groups, this.groups)
    }
}

