package net.bossdragon.util.ai

import com.artemis.Entity
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.system.CollisionSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 *
 */
class SteerableEntity : Steerable<Vector2> {
    private var _tagged = false

    var entity: Entity? = null
    var vel: Velocity? = null
    var collisionSystem: CollisionSystem? = null

    private val circle = Circle()
    private val pos = Vector2()
    private var lastTime: Float = 0f


    private inline fun recalculateCollider() {
        val currentTime = GdxAI.getTimepiece().time

        // If the frame is new then avoid repeating calculations
        // when used multiple times per frame.
        if (this.lastTime != currentTime) {
            this.lastTime = currentTime

            collisionSystem!!.calculateColliderCircle(entity!!, circle)
        }
    }


    override fun getLinearVelocity(): Vector2 {
        return vel!!.velocity
    }

    override fun getOrientation(): Float {
        // TODO
        return 0f
    }

    override fun getAngularVelocity(): Float {
        // TODO
        return 0f
    }

    override fun setMaxAngularAcceleration(maxAngularAcceleration: Float) {
        TODO("not implemented")
    }

    override fun newLocation(): Location<Vector2> {
        throw RuntimeException("this could be used only in Formation")
    }

    override fun getMaxAngularSpeed(): Float {
        TODO("not implemented")
    }

    override fun setOrientation(orientation: Float) {
        // TODO
    }

    override fun getMaxLinearAcceleration(): Float {
        // TODO
//        return vel.acceleration.len()
        return vel!!.maxSpeed * 10
    }

    override fun setTagged(tagged: Boolean) {
        _tagged = tagged
    }

    override fun vectorToAngle(vector: Vector2): Float {
        TODO("not implemented")
    }

    override fun isTagged(): Boolean {
        return _tagged
    }

    override fun setMaxAngularSpeed(maxAngularSpeed: Float) {
        TODO("not implemented")
    }

    override fun getBoundingRadius(): Float {
        recalculateCollider()
        return circle.radius
    }

    override fun setZeroLinearSpeedThreshold(value: Float) {
        TODO("not implemented")
    }

    override fun getMaxLinearSpeed(): Float {
        return vel!!.maxSpeed
    }

    override fun setMaxLinearAcceleration(maxLinearAcceleration: Float) {
        TODO("not implemented")
    }

    override fun setMaxLinearSpeed(maxLinearSpeed: Float) {
        TODO("not implemented")
    }

    override fun getPosition(): Vector2 {
        recalculateCollider()
        return pos.set(circle.x, circle.y)
    }

    override fun getZeroLinearSpeedThreshold(): Float {
        TODO("not implemented")
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        TODO("not implemented")
    }

    override fun getMaxAngularAcceleration(): Float {
        // TODO
        return 0f
    }

}
