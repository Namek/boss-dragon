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
import net.bossdragon.component.base.Attached
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.events.OnSoldierPunched
import net.bossdragon.events.OnPlayerCollidesSoldier
import net.bossdragon.events.OnPlayerLiftsSoldier
import net.bossdragon.events.DoThrowSoldierAction
import net.bossdragon.system.base.collision.Collider
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.util.ActionTimer.TimerState.JustStopped
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import net.bossdragon.enums.CollisionGroups as CG

@Wire
class CharacterStateSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java, Velocity::class.java)
), CollisionEnterListener {
    lateinit internal var mPlayer: ComponentMapper<Player>
    lateinit internal var mSoldier: ComponentMapper<Soldier>
    lateinit internal var mVelocity: ComponentMapper<Velocity>
    lateinit internal var mAttached: M<Attached>

    lateinit internal var events: EventSystem
    lateinit internal var entityFactory: EntityFactorySystem

    var dirX = 0
    var dirY = 0
    var requestedSlideOrThrow = false


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
            if (player.slideCooldown.isRunning) {
                player.slideCooldown.update(dt)
            }

            accel = C.Player.Acceleration

            if (requestedSlideOrThrow) {
                if (player.isCarryingAnySoldier) {
                    if (dirX + 2*dirY != 0) {
                        throwSoldier(player, e.id, dirX, dirY)
                    }
                }
                else if (player.canStartSlide && dirX + 2*dirY != 0) {
                    player.slideDirX = dirX
                    player.slideDirY = dirY
                    player.slide.start()

                    accel = C.Player.AccelerationOnSlide
                }
            }
        }

        velocity.maxSpeed(if (player.isSliding) C.Player.MaxSpeedOnSlide else C.Player.MaxSpeed)
        velocity.setMovement(dirX, dirY, accel)

        requestedSlideOrThrow = false
    }

    private fun throwSoldier(player: Player, playerId: Int, dirX: Int, dirY: Int) {
        val soldierId = player.carriedSoldierId
        player.carriedSoldierId = -1

        mAttached.remove(soldierId)
        events.dispatch(DoThrowSoldierAction(soldierId, playerId, dirX, dirY))
    }

    override fun onCollisionEnter(entityId: Int, otherEntityId: Int) {
        val entity = world.getEntity(entityId)
        val otherEntity = world.getEntity(otherEntityId)


    }

    @Subscribe
    fun onCollidingEnemySoldier(evt: OnPlayerCollidesSoldier) {
        val soldier = mSoldier[evt.soldierEntityId]
        val player = mPlayer[evt.playerEntityId]

        val soldierId = evt.soldierEntityId

        if (player.isSliding) {
            events.dispatch(OnSoldierPunched::class.java)
                .set(soldierId, player.slideDirX)
        }
        else {
            if (!player.isCarryingAnySoldier && soldier.isLying) {
                player.carriedSoldierId = soldierId

                mAttached.create(soldierId)
                    .attachTo(evt.playerEntityId, 0f, Assets.Character.Height.toFloat())

                events.dispatch(OnPlayerLiftsSoldier(soldierId))
            }
            else {
                // TODO should be game over
            }
        }
    }

}
