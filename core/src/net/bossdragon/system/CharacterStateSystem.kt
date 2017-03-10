package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import net.bossdragon.component.Player
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.view.render.RenderSystem

@Wire
class CharacterStateSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java, Velocity::class.java)
), CollisionEnterListener {
    lateinit internal var mPlayer: ComponentMapper<Player>
    lateinit internal var mPosition: ComponentMapper<Position>
    lateinit internal var mVelocity: ComponentMapper<Velocity>

    var dirX = 0
    var dirY = 0
    var requestedSlide = false


    override fun process(character: Entity) {
        val player = mPlayer.get(character)
        val position = mPosition.get(character)
        val velocity = mVelocity.get(character)

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
