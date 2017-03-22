package net.bossdragon.system.view

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 * Defines animation for player character on each frame.
 */
class PlayerCharacterAnimSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java, Velocity::class.java)
) {
    lateinit var mKeyFramedAnim: M<KeyFrameAnimations>
    lateinit var mVelocity: M<Velocity>
    lateinit var mPlayer: M<Player>
    lateinit var mTransform: M<Transform>

    override fun process(e: Entity) {
        val vel = mVelocity[e]
        val anim = mKeyFramedAnim[e]
        val player = mPlayer[e]
        val trans = mTransform[e]

        var animName = Animations.StickMan.IDLE

        if (vel.isMoving) {
            if (vel.velocity.x > 0)
                animName = Animations.StickMan.WALK_RIGHT
            else if (vel.velocity.x < 0)
                animName = Animations.StickMan.WALK_LEFT
            else if (vel.velocity.y > 0)
                animName = Animations.StickMan.WALK_RIGHT
            else if (vel.velocity.y < 0)
                animName = Animations.StickMan.WALK_LEFT
        }

        if (player.isSliding) {
            animName = Animations.StickMan.JUMP

            val dirX = player.slideDirX
            val dirY = player.slideDirY

            trans.rotation =
                if (dirY == 1)
                    180f - dirX * 45f
                else if (dirY == -1)
                    dirX * 45f
                else
                    90f * dirX
        }
        else {
            trans.rotation = 0f
        }

        if (anim.setAnimation(animName)) {
            anim.stateTime = 0f
        }
    }
}
