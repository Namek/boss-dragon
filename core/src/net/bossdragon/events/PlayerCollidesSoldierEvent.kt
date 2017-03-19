package net.bossdragon.events

import net.mostlyoriginal.api.event.common.Event
/**
 *
 */
class PlayerCollidesSoldierEvent : Event {
    var playerEntityId: Int = 0
    var soldierEntityId: Int = 0

    fun setup(playerEntityId: Int, soldierEntityId: Int) {
        this.playerEntityId = playerEntityId
        this.soldierEntityId = soldierEntityId
    }

}
