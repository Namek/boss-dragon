package net.bossdragon.system.base.physics

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.EntitySystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.ReflectionPool
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.system.base.collision.Collider
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 * System simulating Physics for entities having [Physical] component set.
 *
 * If entity has [Transform] then [Transform.currentPos] and [Transform.rotation] is the source of truth before simulation if body type is NOT DynamicBody.
 * If entity has [Velocity] then [Velocity.velocity] is the source of truth before simulation.
 * If entity has [Size] then [Size.origin] modifies the positon from [Transform.currentPos] before sim.
 * If entity has [Collider] then it throws an exception.
 * After simulation [Transform.currentPos], [Transform.desiredPos] and [Velocity.velocity] are updated.
 */
class PhysicsSystem(val gravity: Vector2) : EntitySystem(
    Aspect.all(Physical::class.java)
) {
    lateinit var mTransform: M<Transform>
    lateinit var mPhysical: M<Physical>
    lateinit var mSize: M<Size>
    lateinit var mVelocity: M<Velocity>
    lateinit var mCollider: M<Collider>

    lateinit var engine: World

    private val TIME_STEP = 1/60f
    private var timeAccumulator = 0f
    private var pos = Vector2()


    override fun initialize() {
        engine = World(gravity, true)
    }

    override fun dispose() {
        engine.dispose()
    }

    override fun inserted(e: Entity) {
        val body = mPhysical[e].body
        val userData = UserData.obtain(e.id)

        for (fixture in body.fixtureList)
            fixture.userData = userData

        body.userData = userData
    }

    override fun removed(e: Entity) {
        val phys = mPhysical[e]
        UserData.free(phys.body.userData as UserData)
        engine.destroyBody(phys.body)
    }

    override fun processSystem() {
        val entities = entities
        val array = entities.data
        var i = 0
        val s = entities.size()

        // copy values to from rendering/logic pipeline to physics engine
        while (s > i) {
            val e = array[i++] as Entity

            if (mCollider.getSafe(e, null) != null)
                throw RuntimeException("entities in PhysicsSystem can't have Collider component because it's owned by another system") as Throwable

            val body = mPhysical[e].body
            val transform = mTransform.getSafe(e, null)
            val size = mSize.getSafe(e, null)
            val velocity = mVelocity.getSafe(e, null)

            if (transform != null && body.type != BodyDef.BodyType.DynamicBody) {
                pos.set(transform.currentPos)

                if (size != null) {
                    pos.x -= (size.origin.x * size.width)
                    pos.y -= (size.origin.y * size.height)
                }

                body.position.set(pos)
            }

            if (velocity != null) {
                body.setLinearVelocity(velocity.velocity.x, velocity.velocity.y)
            }
        }

        // simulate engine!
        timeAccumulator += Math.min(world.getDelta(), 0.25f)

        while (timeAccumulator >= TIME_STEP) {
            engine.step(TIME_STEP, 6, 2)
            timeAccumulator -= TIME_STEP
        }

        // copy values back from physics engine to rendering pipeline
        i = 0
        while (s > i) {
            val e = array[i++] as Entity
            val body = mPhysical[e].body
            val transform = mTransform.getSafe(e, null)
            val velocity = mVelocity.getSafe(e, null)

            if (transform != null) {
                transform.currentPos.set(transform.desiredPos.set(body.position))
                transform.rotation = body.angle * MathUtils.radiansToDegrees
            }

            if (velocity != null) {
                velocity.velocity.set(body.linearVelocity)
            }
        }
    }


    class UserData : Pool.Poolable {
        private var entityId: Int = 0
        var data: Any? = null

        fun init(entityId: Int): UserData {
            this.entityId = entityId

            return this
        }

        override fun reset() {
            entityId = -1
        }

        companion object {
            private var pool: Pool<UserData> = ReflectionPool<UserData>(UserData::class.java)

            fun obtain(entityId: Int): UserData {
                return pool.obtain().init(entityId)
            }

            fun free(userData: UserData?) {
                if (userData != null) {
                    pool.free(userData)
                }
            }
        }
    }
}
