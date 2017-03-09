package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3

import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.Player
import net.bossdragon.enums.C
import net.bossdragon.enums.Tags
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.view.render.RenderSystem

@Wire
class PlayerStateSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java, Velocity::class.java)
), CollisionEnterListener {
    lateinit internal var mPlayer: ComponentMapper<Player>
    lateinit internal var mTransform: ComponentMapper<Transform>
    lateinit internal var mVelocity: ComponentMapper<Velocity>

    lateinit internal var entityFactorySystem: EntityFactorySystem
    lateinit internal var inputSystem: InputSystem
    lateinit internal var renderSystem: RenderSystem
    lateinit internal var tags: TagManager

    lateinit internal var input: Input

    var dirX = 0
    var dirY = 0
    var requestedSlide = false


    override fun initialize() {
        input = Gdx.input
    }

    override fun process(e: Entity) {
        val player = mPlayer.get(e)
        val transform = mTransform.get(e)
        val velocity = mVelocity.get(e)

        if (requestedSlide && canSlide()) {
            // TODO slide

            requestedSlide = false
        }
        else {
            velocity.setMovement(dirX, dirY, C.Player.Acceleration)
        }
    }

    private fun canSlide(): Boolean {
        return false
    }

    override fun onCollisionEnter(entityId: Int, otherEntityId: Int) {
        val entity = world.getEntity(entityId)
        val otherEntity = world.getEntity(otherEntityId)


    }

}
