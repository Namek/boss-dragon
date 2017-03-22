package net.bossdragon.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 *
 */
public class OnPlayerLiftsSoldier implements Event {
    public final int soldierId;

    public OnPlayerLiftsSoldier(int solderId) {
        this.soldierId = solderId;
    }
}
