package net.bossdragon.events

import net.mostlyoriginal.api.event.common.Event

/**
 *
 */
class DoThrowSoldierAction(
    val soldierId: Int, val playerId: Int, val dirX: Int, val dirY: Int
) : Event { }
