package net.bossdragon.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 *
 */
public class OnPlayerCollidesSoldier implements Event {
    public int playerEntityId = 0;
    public int soldierEntityId = 0;

    public void setup(int playerEntityId, int soldierEntityId) {
        this.playerEntityId = playerEntityId;
        this.soldierEntityId = soldierEntityId;
    }
}
