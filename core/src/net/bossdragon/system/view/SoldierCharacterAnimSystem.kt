package net.bossdragon.system.view

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Enemy
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.enums.Animations
import net.bossdragon.events.EnemyPunchedEvent
import net.bossdragon.events.SoldierGetUpEvent
import net.bossdragon.events.SoldierPushedToTheFloorEvent
import net.bossdragon.util.operations.funcs.*
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import se.feomedia.orion.OperationFactory.parallel
import se.feomedia.orion.OperationFactory.sequence

/**
 *
 */
class SoldierCharacterAnimSystem : EntityProcessingSystem(
    Aspect.all(
        Enemy::class.java,
        Velocity::class.java,
        Transform::class.java,
        KeyFrameAnimations::class.java
    )
) {
    lateinit var mEnemy: M<Enemy>
    lateinit var mVelocity: M<Velocity>
    lateinit var mTransform: M<Transform>
    lateinit var mKeyFramedAnim: M<KeyFrameAnimations>

    val LyingAnimAfterDuration = 0.3f

    override fun process(e: Entity) {
        val enemy = mEnemy[e]
        val vel = mVelocity[e]
        val anim = mKeyFramedAnim[e]
        val trans = mTransform[e]


        if (enemy.isLying) {
            if (enemy.lyingCooldown > LyingAnimAfterDuration) {
                anim.setAnimation(Animations.StickMan.JUMP)
                anim.stateTime = 0f
            }
        }
        else {
            var animName = Animations.Soldier.IDLE

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

            if (anim.setAnimation(animName)) {
                anim.stateTime = 0f
            }
        }
    }

    @Subscribe
    fun onSoldierPushedOnTheFloor(evt: SoldierPushedToTheFloorEvent) {
        sequence(
            parallel(
                // jump up/down
                sequence(
                    displaceBy(0f, 20f, 0.1f),
                    displaceBy(0f, -20f, 0.2f)
                ),

                // rotate
                rotateBy(evt.pushDirX * 90f, 0.3f)
            )
        )
            .register(world, evt.entityId)
    }

    @Subscribe
    fun onSoldierGetUp(evt: SoldierGetUpEvent) {
        // rotate him back
        mTransform[evt.entityId].rotation = 0f
    }
}
