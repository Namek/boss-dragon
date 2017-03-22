package net.bossdragon.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 *
 */
public class DoThrowSoldierAction implements Event {
    public final int soldierId;
    public final int playerId;
    public final int dirX;
    public final int dirY;

    public DoThrowSoldierAction(int soldierId, int playerId, int dirX, int dirY) {
        this.soldierId = soldierId;
        this.playerId = playerId;
        this.dirX = dirX;
        this.dirY = dirY;
    }
}
