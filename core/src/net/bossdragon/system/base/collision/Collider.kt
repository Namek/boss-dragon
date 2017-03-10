package net.bossdragon.system.base.collision

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
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

    var spatialSizeCalc = SpatialCalculation.BasedOnSizeComponent
    var spatialPosCalc = SpatialCalculation.BasedOnSizeComponent

    /**
     * @see spatialSize
     * @see spatialPos
     */
    enum class SpatialCalculation {
        BasedOnSizeComponent,
        Constant,
        Scale
    }

    /**
     * When [spatialSizeCalc] equals:
     *
     *  * [SpatialCalculation.BasedOnSizeComponent]
     *    then it's ignored
     *
     *  * [SpatialCalculation.Constant]
     *    then collider size is exactly the given value
     *
     *  * [SpatialCalculation.Scale]
     *    then collider size is calculated is a scaled [Size] component
     */
    var spatialSize = Vector2()

    /**
     * When [spatialPosCalc] equals:
     *
     *  * [SpatialCalculation.BasedOnSizeComponent]
     *    then it's ignored
     *
     *  * [SpatialCalculation.Constant]
     *    then a constant value from this vector is added
     *    to [Transform.currentPos]
     *
     *  * [SpatialCalculation.Scale]
     *    then a percentage value based on [Size] component is added to [Transform.currentPos]
     */
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
        colliderShape = ColliderShape.CIRCLE
        return spatialConstantSize(radius*2, radius*2)
    }

    fun spatialConstantSize(colliderWidth: Float, colliderHeight: Float): Collider {
        spatialSizeCalc = SpatialCalculation.Constant
        spatialSize.set(colliderWidth, colliderHeight)
        return this
    }

    fun spatialScaledSize(colliderWidthScale: Float, colliderHeightScale: Float): Collider {
        spatialSizeCalc = SpatialCalculation.Scale
        spatialSize.set(colliderWidthScale, colliderHeightScale)
        return this
    }

    fun spatialConstantPos(colliderX: Float, colliderY: Float): Collider {
        spatialPosCalc = SpatialCalculation.Constant
        spatialPos.set(colliderX, colliderY)
        return this
    }

    fun spatialScaledPos(colliderWidthScale: Float, colliderHeightScale: Float): Collider {
        spatialPosCalc = SpatialCalculation.Scale
        spatialPos.set(colliderWidthScale, colliderHeightScale)
        return this
    }

    override fun reset() {
        groups = 0
        colliderShape = ColliderShape.RECT
        spatialPosCalc = SpatialCalculation.BasedOnSizeComponent
        spatialSizeCalc = SpatialCalculation.BasedOnSizeComponent
        enterListener = null
        exitListener = null
    }

    fun isInGroup(group: Long): Boolean {
        return this.groups and group > 0
    }

    fun isInAnyGroup(groups: Long, relations: CollisionGroupsRelations): Boolean {
        return relations.anyRelationExists(groups, this.groups)
    }
}

