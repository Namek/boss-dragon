package net.bossdragon.system.base.collision

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.EntitySystem
import com.artemis.annotations.Wire
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.Rectangle
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.system.base.collision.messaging.CollisionEvent
import net.bossdragon.system.base.events.EventSystem
import java.util.*

/**
 * Collision detector working on top of groups and group relations.

 * @todo optimize system to be processed in configurable intervals
 * *
 * @todo optimize by caching entities belongingness to groups, see [.processEntities] comment.
 */
@Wire
open class CollisionDetectionSystem @JvmOverloads constructor(
    val relations: CollisionGroupsRelations = CollisionGroupsRelations(),
    var eventDispatchingEnabled: Boolean = false
) : EntitySystem(
    Aspect.all(
        Collider::class.java,
        Transform::class.java,
        Size::class.java
    )
) {

    lateinit protected var mCollider: ComponentMapper<Collider>
    lateinit protected var mTransform: ComponentMapper<Transform>
    lateinit protected var mSize: ComponentMapper<Size>

    protected var events: EventSystem? = null

    private val phases = CollisionPhases()
    private val rect1 = Rectangle()
    private val rect2 = Rectangle()


    constructor(eventDispatchingEnabled: Boolean) : this(CollisionGroupsRelations(), eventDispatchingEnabled) {}

    override fun processSystem() {
        processEntities(subscription.entities)
    }

    /**
     * **TODO**: optimize by caching entities belongingness to groups.
     * Now all entities are checked against themselves needlessly,
     * e.g. thanks to groups relations Bullets will never collide with Bullets
     * so it doesn't make much performance sense to check those relations between those entities.
     */
    protected fun processEntities(entities: IntBag) {
        val ids = entities.data

        var i = 0
        val n = entities.size()
        while (i < n) {
            val entity1Id = ids[i]
            val collider1 = mCollider.get(entity1Id)

            for (j in i + 1..n - 1) {
                val entity2Id = ids[j]
                val collider2 = mCollider.get(entity2Id)

                val phase = phases[entity1Id, entity2Id]

                if (phase == NONE) {
                    if (!relations.anyRelationExists(collider1.groups, collider2.groups)) {
                        continue
                    }

                    if (checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        onCollisionEnter(entity1Id, collider1, entity2Id, collider2)
                        phases[entity1Id, entity2Id] = ENTERED
                    }
                }
                else if (phase == EXISTING) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases[entity1Id, entity2Id] = NONE
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2)
                    }
                }
                else if (phase == ENTERED) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases[entity1Id, entity2Id] = NONE
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2)
                    }
                    else {
                        phases[entity1Id, entity2Id] = EXISTING
                    }
                }
            }
            ++i
        }
    }

    fun checkOverlap(entity1Id: Int, collider1: Collider, entity2Id: Int, collider2: Collider): Boolean {
        val trans1 = mTransform.get(entity1Id)
        val trans2 = mTransform.get(entity2Id)
        val size1 = mSize.get(entity1Id)
        val size2 = mSize.get(entity2Id)

        when (collider1.colliderShape) {
            ColliderShape.RECT -> {
                setColliderRect(collider1, trans1, size1, rect1)
            }
        }
        when (collider2.colliderShape) {
            ColliderShape.RECT -> {
                setColliderRect(collider2, trans2, size2, rect2)
            }
        }

        var overlaps = false

        if (collider1.colliderShape == collider2.colliderShape) {
            when (collider1.colliderShape) {
                ColliderShape.RECT -> overlaps = rect1.overlaps(rect2)
            }
        }
        else {
            throw Error("Currently there is only one collider type supported!")
        }

        return overlaps
    }

    private inline fun setColliderRect(collider: Collider, trans: Transform, size: Size, outRect: Rectangle) {
        outRect.setPosition(trans.currentPos.x, trans.currentPos.y)

        when (collider.spatialSizeCalc) {
            Collider.SpatialCalculation.BasedOnSizeComponent -> {
                outRect.setSize(size.width, size.height)
            }
            Collider.SpatialCalculation.Constant -> {
                outRect.setSize(collider.spatialSize.x, collider.spatialSize.y)
            }
            Collider.SpatialCalculation.Scale -> {
                outRect.setSize(
                    collider.spatialSize.x * size.width,
                    collider.spatialSize.y * size.height
                )
            }
        }

        when (collider.spatialPosCalc) {
            Collider.SpatialCalculation.BasedOnSizeComponent -> {
                // don't add anything
            }
            Collider.SpatialCalculation.Constant -> {
                outRect.x += collider.spatialPos.x
                outRect.y += collider.spatialPos.y
            }
            Collider.SpatialCalculation.Scale -> {
                outRect.x += (collider.spatialPos.x * size.width)
                outRect.y += (collider.spatialPos.y * size.height)
            }
        }
    }

    override fun removed(entity: Entity) {
        phases.clear(entity.id)
    }

    fun onCollisionEnter(entity1Id: Int, collider1: Collider, entity2Id: Int, collider2: Collider) {
        if (collider1.enterListener != null) {
            collider1.enterListener!!.onCollisionEnter(entity1Id, entity2Id)
        }

        if (collider2.enterListener != null) {
            collider2.enterListener!!.onCollisionEnter(entity2Id, entity1Id)
        }

        if (eventDispatchingEnabled) {
            events!!
                .dispatch(CollisionEvent::class.java)
                .setup(entity1Id, entity2Id, CollisionEvent.ENTER)
        }
    }

    fun onCollisionExit(entity1Id: Int, collider1: Collider, entity2Id: Int, collider2: Collider) {
        if (collider1.exitListener != null) {
            collider1.exitListener!!.onCollisionExit(entity1Id, entity2Id)
        }

        if (collider2.exitListener != null) {
            collider2.exitListener!!.onCollisionExit(entity2Id, entity1Id)
        }

        if (eventDispatchingEnabled) {
            events!!.dispatch(CollisionEvent::class.java)
                .setup(entity1Id, entity2Id, CollisionEvent.EXIT)
        }
    }


    /**
     * @author Namek
     * *
     * @todo **Pleease**, optimizee meee!
     */
    private class CollisionPhases {
        /** Maps entity1Id to entity2d which maps to phase  */
        private val collisionPhases = TreeMap<Int, MutableMap<Int, Int>>()


        operator fun set(entity1Id: Int, entity2Id: Int, phase: Int) {
            var entity1Id = entity1Id
            var entity2Id = entity2Id
            if (entity2Id < entity1Id) {
                val tmp = entity1Id
                entity1Id = entity2Id
                entity2Id = tmp
            }

            var relations: MutableMap<Int, Int>? = collisionPhases[entity1Id]

            if (relations == null) {
                relations = TreeMap<Int, Int>()
                collisionPhases.put(entity1Id, relations)
            }

            relations.put(entity2Id, phase)
        }

        operator fun get(entity1Id: Int, entity2Id: Int): Int {
            var entity1Id = entity1Id
            var entity2Id = entity2Id
            if (entity2Id < entity1Id) {
                val tmp = entity1Id
                entity1Id = entity2Id
                entity2Id = tmp
            }

            val relations = collisionPhases[entity1Id]
            if (relations != null) {
                val phase = relations[entity2Id]

                if (phase != null) {
                    return phase
                }
            }

            return NONE
        }

        fun clear(entityId: Int) {
            val relations = collisionPhases[entityId]

            relations?.clear()

            for (entry in collisionPhases.values) {
                entry.remove(entityId)
            }
        }
    }

    companion object {
        val NONE = 1
        val ENTERED = 2
        val EXISTING = 3
    }
}
