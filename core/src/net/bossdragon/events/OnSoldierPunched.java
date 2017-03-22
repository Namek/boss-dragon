package net.bossdragon.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 *
 */
public class OnSoldierPunched implements Event {
    public int entityId = 0;

    /** direction of slide: 1 or -1 */
    public int pushDirX = 0;

    public void set(int entityId, int pushDirX) {
        this.entityId = entityId;
        this.pushDirX = pushDirX;
    }
}
