package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.Enemy
import net.bossdragon.component.FightAI

/**
 *
 */
class EnemyFightAISystem : EntityProcessingSystem(
    Aspect.all(Enemy::class.java, FightAI::class.java)
) {
    override fun process(e: Entity) {
        // TODO
    }
}
