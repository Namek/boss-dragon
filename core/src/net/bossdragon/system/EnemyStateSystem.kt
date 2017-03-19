package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Enemy
import net.bossdragon.component.FightAI
import net.bossdragon.component.base.Velocity
import net.bossdragon.enums.C
import net.bossdragon.events.EnemyPunchedEvent
import net.bossdragon.events.SoldierGetUpEvent
import net.bossdragon.events.SoldierPushedToTheFloorEvent
import net.bossdragon.system.base.events.EventSystem
import net.mostlyoriginal.api.event.common.Subscribe
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 *
 */
class EnemyStateSystem : EntityProcessingSystem(Aspect.all(Enemy::class.java)) {
    lateinit var mEnemy: M<Enemy>
    lateinit var mFightAI: M<FightAI>
    lateinit var mVelocity: M<Velocity>

    lateinit var events: EventSystem


    override fun process(e: Entity) {
        val enemy = mEnemy[e]

        if (enemy.lyingCooldown > 0f) {
            enemy.lyingCooldown -= world.getDelta()

            if (enemy.lyingCooldown <= 0f) {
                mFightAI.create(e)
                enemy.lyingCooldown = 0f

                events.dispatch(SoldierGetUpEvent::class.java)
                    .entityId = e.id
            }
        }
    }

    @Subscribe
    fun onEnemyPunched(evt: EnemyPunchedEvent) {
        val enemy = mEnemy[evt.entityId]
        val vel = mVelocity[evt.entityId]

        if (enemy.lyingCooldown <= 0f) {
            enemy.lyingCooldown = C.Enemy.StunCooldown
            vel.immediateStop()

            mFightAI.remove(evt.entityId)

            events.dispatch(SoldierPushedToTheFloorEvent::class.java)
                .set(evt.entityId, evt.pushDirX)
        }
    }
}
