package net.bossdragon.system.base.events;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Dispatching:
 * <ul>
 *   <li>implement {@link Event} interface and <code>dispatch(ExplosionEvent.class)</code>.
 *   <li><code>signal(someIntegerCode)</code> if you don't need to pass any data into event object</li>
 * </ul>
 * 
 * To subscribe for event just add method into system or manager:
 * <code>public void onPlayerShot(ExplosionEvent evt) { ... }</code>
 * 
 * 
 * @author Namek
 * @see Event
 */
public class EventSystem extends net.mostlyoriginal.api.event.common.EventSystem {
	public EventSystem() {
		super(new PollingEventDispatcher(), new SignalAndEventFinder());
	}
	
	public void signal(int code) {
		this.dispatch(net.bossdragon.system.base.events.Signal.class).code = code;
	}
}
