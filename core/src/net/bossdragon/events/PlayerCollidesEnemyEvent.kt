package net.bossdragon.events

import net.mostlyoriginal.api.event.common.Event
/**
 *
 */
class PlayerCollidesEnemyEvent : Event {
    var playerEntityId: Int = 0
    var enemyEntityId: Int = 0

    fun setup(playerEntityId: Int, enemyEntityId: Int) {
        this.playerEntityId = playerEntityId
        this.enemyEntityId = enemyEntityId
    }

}
