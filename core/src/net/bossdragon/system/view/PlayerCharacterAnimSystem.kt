package net.bossdragon.system.view

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Player
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

    override fun process(entity: Entity) {
        val vel = mVelocity[entity]
        val anim = mKeyFramedAnim[entity]
        val player = mPlayer[entity]

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

        if (player.isSliding)
            animName = Animations.StickMan.LYING

        if (anim.setAnimation(animName)) {
            anim.stateTime = 0f
        }
    }
}
