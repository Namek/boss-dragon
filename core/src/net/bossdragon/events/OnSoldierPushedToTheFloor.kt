package net.bossdragon.events

import net.mostlyoriginal.api.event.common.Event

/**
 *
 */
class OnSoldierPushedToTheFloor : Event {
    var entityId: Int = 0

    /** direction of slide: 1 or -1 */
    var pushDirX: Int = 0

    fun set(entityId: Int, pushDirX: Int) {
        this.entityId = entityId
        this.pushDirX = pushDirX
    }
}
