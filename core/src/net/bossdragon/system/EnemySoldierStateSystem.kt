package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Soldier
import net.bossdragon.component.FightAI
import net.bossdragon.component.base.Attached
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.enums.CollisionGroups
import net.bossdragon.events.DoThrowSoldierAction
import net.bossdragon.events.OnSoldierPunched
import net.bossdragon.events.OnSoldierGetUp
import net.bossdragon.events.OnSoldierPushedToTheFloor
import net.bossdragon.system.base.collision.Collider
import net.bossdragon.system.base.events.EventSystem
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 *
 */
class EnemySoldierStateSystem : EntityProcessingSystem(
    Aspect.all(Soldier::class.java)
        .exclude(Attached::class.java)
) {
    lateinit var mSoldier: M<Soldier>
    lateinit var mFightAI: M<FightAI>
    lateinit var mVelocity: M<Velocity>
    lateinit var mCollider: M<Collider>

    lateinit var events: EventSystem


    override fun process(e: Entity) {
        val soldier = mSoldier[e]

        if (soldier.lyingCooldown > 0f) {
            soldier.lyingCooldown -= world.getDelta()

            if (soldier.lyingCooldown <= 0f) {
                mFightAI.create(e)
                soldier.lyingCooldown = 0f

                events.dispatch(OnSoldierGetUp::class.java)
                    .entityId = e.id
            }
        }
    }

    @Subscribe
    fun onEnemyPunched(evt: OnSoldierPunched) {
        val soldier = mSoldier[evt.entityId]
        val vel = mVelocity[evt.entityId]

        if (soldier.lyingCooldown <= 0f) {
            soldier.lyingCooldown = C.Soldier.StunCooldown
            vel.immediateStop()

            mFightAI.remove(evt.entityId)

            events.dispatch(OnSoldierPushedToTheFloor::class.java)
                .set(evt.entityId, evt.pushDirX)
        }
    }

    @Subscribe
    fun doThrowSoldier(action: DoThrowSoldierAction) {
        mCollider[action.soldierId].groups(CollisionGroups.BULLET)
        mFightAI.remove(action.soldierId)
        mSoldier.remove(action.soldierId)

        val vel = mVelocity[action.soldierId]
        vel.maxSpeed = C.Soldier.MaxSpeedWhenThrown
        vel.setMovement(action.dirX, action.dirY, C.Soldier.AccelWhenThrown)

        // TODO dispose this entity when it's out of map or hits a dragon
    }
}
