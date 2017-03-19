package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Soldier
import net.bossdragon.component.FightAI
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.events.SoldierPunchedEvent
import net.bossdragon.events.SoldierGetUpEvent
import net.bossdragon.events.SoldierPushedToTheFloorEvent
import net.bossdragon.system.base.events.EventSystem
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 *
 */
class EnemySoldierStateSystem : EntityProcessingSystem(Aspect.all(Soldier::class.java)) {
    lateinit var mSoldier: M<Soldier>
    lateinit var mFightAI: M<FightAI>
    lateinit var mVelocity: M<Velocity>

    lateinit var events: EventSystem


    override fun process(e: Entity) {
        val soldier = mSoldier[e]

        if (soldier.lyingCooldown > 0f) {
            soldier.lyingCooldown -= world.getDelta()

            if (soldier.lyingCooldown <= 0f) {
                mFightAI.create(e)
                soldier.lyingCooldown = 0f

                events.dispatch(SoldierGetUpEvent::class.java)
                    .entityId = e.id
            }
        }
    }

    @Subscribe
    fun onEnemyPunched(evt: SoldierPunchedEvent) {
        val soldier = mSoldier[evt.entityId]
        val vel = mVelocity[evt.entityId]

        if (soldier.lyingCooldown <= 0f) {
            soldier.lyingCooldown = C.Soldier.StunCooldown
            vel.immediateStop()

            mFightAI.remove(evt.entityId)

            events.dispatch(SoldierPushedToTheFloorEvent::class.java)
                .set(evt.entityId, evt.pushDirX)
        }
    }
}
