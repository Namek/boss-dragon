package net.bossdragon.system.base.collision

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.EntitySystem
import com.artemis.annotations.Wire
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Size
import net.bossdragon.system.base.collision.messaging.CollisionEvent
import net.bossdragon.system.base.events.EventSystem
import java.util.*

/**
 * Collision detector working on top of groups and group relations.
 *
 * @todo optimize system to be processed in configurable intervals
 * @todo optimize by caching entities belongingness to groups, see [.processEntities] comment.
 */
@Wire
open class CollisionDetectionSystem @JvmOverloads constructor(
    val relations: CollisionGroupsRelations = CollisionGroupsRelations(),
    var eventDispatchingEnabled: Boolean = false
) : EntitySystem(
    Aspect.all(
        Collider::class.java,
        Position::class.java,
        Size::class.java
    )
) {

    lateinit protected var mCollider: ComponentMapper<Collider>
    lateinit protected var mPosition: ComponentMapper<Position>
    lateinit protected var mSize: ComponentMapper<Size>

    protected var events: EventSystem? = null

    val phases = CollisionPhases()

    private val rect1 = Rectangle()
    private val rect2 = Rectangle()
    private val circle1 = Circle()
    private val circle2 = Circle()


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

                if (phase == CollisionPhases.None) {
                    if (!relations.anyRelationExists(collider1.groups, collider2.groups)) {
                        continue
                    }

                    if (checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        onCollisionEnter(entity1Id, collider1, entity2Id, collider2)
                        phases[entity1Id, entity2Id] = CollisionPhases.Entered
                    }
                }
                else if (phase == CollisionPhases.Existing) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases[entity1Id, entity2Id] = CollisionPhases.None
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2)
                    }
                }
                else if (phase == CollisionPhases.Entered) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases[entity1Id, entity2Id] = CollisionPhases.None
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2)
                    }
                    else {
                        phases[entity1Id, entity2Id] = CollisionPhases.Existing
                    }
                }
            }
            ++i
        }
    }

    fun checkOverlap(entity1Id: Int, collider1: Collider, entity2Id: Int, collider2: Collider): Boolean {
        val pos1 = mPosition.get(entity1Id)
        val pos2 = mPosition.get(entity2Id)
        val size1 = mSize.get(entity1Id)
        val size2 = mSize.get(entity2Id)

        when (collider1.colliderShape) {
            ColliderShape.RECT -> {
                setColliderRect(collider1, pos1, size1, rect1, true)
            }
            ColliderShape.CIRCLE -> {
                setColliderCircle(collider1, pos1, size1, circle1, true)
            }
        }
        when (collider2.colliderShape) {
            ColliderShape.RECT -> {
                setColliderRect(collider2, pos2, size2, rect2, true)
            }
            ColliderShape.CIRCLE -> {
                setColliderCircle(collider2, pos2, size2, circle2, true)
            }
        }

        val isRect1 = collider1.colliderShape == ColliderShape.RECT
        val isRect2 = collider2.colliderShape == ColliderShape.RECT
        val isCircle1 = collider1.colliderShape == ColliderShape.CIRCLE
        val isCircle2 = collider2.colliderShape == ColliderShape.CIRCLE

        val overlaps =
            if (isRect1 && isRect2)
                rect1.overlaps(rect2)
            else if (isCircle1 && isCircle2)
                circle1.overlaps(circle2)
            else if (isRect1 && isCircle2)
                Intersector.overlaps(circle2, rect1)
            else if (isCircle1 && isRect2)
                Intersector.overlaps(circle1, rect2)
            else
                throw Error("I don't know how to handle this collision check!")

        return overlaps
    }

    internal fun calculateColliderRect(e: Entity, outRect: Rectangle, desiredPos: Boolean = false): Collider {
        val collider = mCollider[e]
        setColliderRect(collider, mPosition[e], mSize[e], outRect, desiredPos)
        return collider
    }

    private inline fun setColliderRect(collider: Collider, pos: Position, size: Size, outRect: Rectangle, desiredPos: Boolean) {
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

        outRect.setPosition(if (desiredPos) pos.desiredPos else pos.currentPos)

        when (collider.spatialPosCalc) {
            Collider.SpatialCalculation.BasedOnSizeComponent -> {
                outRect.x -= (size.origin.x * outRect.width)
                outRect.y -= (size.origin.y * outRect.height)
            }
            Collider.SpatialCalculation.Constant -> {
                outRect.x += collider.spatialPos.x
                outRect.y += collider.spatialPos.y
            }
            Collider.SpatialCalculation.Scale -> {
                outRect.x -= (collider.spatialPos.x * size.width)
                outRect.y -= (collider.spatialPos.y * size.height)
            }
        }
    }

    internal fun calculateColliderCircle(e: Entity, outCircle: Circle, desiredPos: Boolean = false): Collider {
        val collider = mCollider[e]
        setColliderCircle(collider, mPosition[e], mSize[e], outCircle, desiredPos)
        return collider
    }

    private inline fun setColliderCircle(collider: Collider, pos: Position, size: Size, outCircle: Circle, desiredPos: Boolean) {
        var radius = 0f

        when (collider.spatialSizeCalc) {
            Collider.SpatialCalculation.BasedOnSizeComponent -> {
                radius = size.width / 2
            }
            Collider.SpatialCalculation.Constant -> {
                radius = collider.spatialSize.x / 2
            }
            Collider.SpatialCalculation.Scale -> {
                radius = collider.spatialSize.x * size.width / 2
            }
        }
        outCircle.setRadius(radius)

        outCircle.setPosition(if (desiredPos) pos.desiredPos else pos.currentPos)
        outCircle.x += radius/2
        outCircle.y += radius/2

        when (collider.spatialPosCalc) {
            Collider.SpatialCalculation.BasedOnSizeComponent -> {
                outCircle.x -= (size.origin.x * radius)
                outCircle.y -= (size.origin.y * radius)
            }
            Collider.SpatialCalculation.Constant -> {
                outCircle.x += collider.spatialPos.x
                outCircle.y += collider.spatialPos.y
            }
            Collider.SpatialCalculation.Scale -> {
                outCircle.x -= (collider.spatialPos.x * radius)
                outCircle.y -= (collider.spatialPos.y * radius)
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
}
