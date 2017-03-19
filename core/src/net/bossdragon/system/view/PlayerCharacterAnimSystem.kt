package net.bossdragon.system.view

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import net.bossdragon.component.Player
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.enums.Animations
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

        if (player.isSliding || Gdx.input.isKeyPressed(Input.Keys.P)) {
            animName = Animations.StickMan.LYING

            val dirX = //player.slidedDirX
                if (Gdx.input.isKeyPressed(Input.Keys.J)) -1
                else if (Gdx.input.isKeyPressed(Input.Keys.L)) 1
                else 0
            val dirY = //player.slideDirY
                if (Gdx.input.isKeyPressed(Input.Keys.I)) 1
                else if (Gdx.input.isKeyPressed(Input.Keys.K)) -1
                else 0

            

            trans.rotation =
                if (dirY == 1)
                    -90f - Math.signum(dirX.toFloat()) * 45f
                else if (dirY == -1)
                    90f + Math.signum(dirX.toFloat()) * 45f
                else
                    0f
        }
        else {
            trans.rotation = 0f
        }

        if (anim.setAnimation(animName)) {
            anim.stateTime = 0f
        }

//        trans.flipX = player.isSliding && player.slideDirX > 0
    }
}
