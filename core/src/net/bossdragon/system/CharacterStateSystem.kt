package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.annotations.Wire
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.math.Interpolation
import net.bossdragon.component.Soldier
import net.bossdragon.component.FightAI
import net.bossdragon.component.Player
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.events.SoldierPunchedEvent
import net.bossdragon.events.PlayerCollidesSoldierEvent
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.util.ActionTimer.TimerState.JustStopped
import net.mostlyoriginal.api.event.common.Subscribe

@Wire
class CharacterStateSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java, Velocity::class.java)
), CollisionEnterListener {
    lateinit internal var mPlayer: ComponentMapper<Player>
    lateinit internal var mSoldier: ComponentMapper<Soldier>
    lateinit internal var mVelocity: ComponentMapper<Velocity>

    lateinit internal var events: EventSystem

    var dirX = 0
    var dirY = 0
    var requestedSlide = false


    override fun process(e: Entity) {
        val player = mPlayer.get(e)
        val velocity = mVelocity.get(e)
        val dt = world.getDelta()

        var dirX = this.dirX
        var dirY = this.dirY
        var accel = 0f

        if (player.isSliding) {
            if (player.slide.update(dt) == JustStopped) {
                velocity.immediateStop()
                player.slideCooldown.start()
            }
            else {
                accel = Interpolation.bounceOut.apply(
                    C.Player.AccelerationOnSlide, 0f, player.slide.progress
                )

                dirX = player.slideDirX
                dirY = player.slideDirY
            }
        }
        else {
            if (requestedSlide && player.canStartSlide && dirX + 2*dirY != 0) {
                player.slideDirX = dirX
                player.slideDirY = dirY
                player.slide.start()

                accel = C.Player.AccelerationOnSlide
            }
            else {
                if (player.slideCooldown.isRunning) {
                    player.slideCooldown.update(dt)
                }

                accel = C.Player.Acceleration
            }
        }

        velocity.maxSpeed(if (player.isSliding) C.Player.MaxSpeedOnSlide else C.Player.MaxSpeed)
        velocity.setMovement(dirX, dirY, accel)

        requestedSlide = false
    }

    override fun onCollisionEnter(entityId: Int, otherEntityId: Int) {
        val entity = world.getEntity(entityId)
        val otherEntity = world.getEntity(otherEntityId)


    }

    @Subscribe
    fun onCollidingEnemySoldier(evt: PlayerCollidesSoldierEvent) {
        val soldier = mSoldier[evt.soldierEntityId]
        val player = mPlayer[evt.playerEntityId]

        if (player.isSliding) {
            events.dispatch(SoldierPunchedEvent::class.java)
                .set(evt.soldierEntityId, player.slideDirX)
        }
        else {
            // TODO should be game over
        }
    }

}
