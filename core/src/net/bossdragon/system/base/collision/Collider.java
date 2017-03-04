package net.bossdragon.system.base.collision;

import net.bossdragon.system.base.collision.messaging.CollisionEnterListener;

import com.artemis.PooledComponent;
import net.bossdragon.system.base.collision.messaging.CollisionExitListener;

/**
 * Collider describes parameters for collision tests of certain entity. 
 * 
 * @author Namek
 * @see CollisionDetectionSystem
 */
public class Collider extends PooledComponent {
	/** Bitset of relations to which this entity belongs. */
	public long groups;
	
	/** For basic shape types supported by `CollisionDetectionSystem`, look into {@link ColliderType} */
	public int colliderType = ColliderType.RECT;


	public CollisionEnterListener enterListener;
	public CollisionExitListener exitListener;
	
	
	public Collider setup(long groups, int colliderType) {
		this.groups = groups;
		this.colliderType = colliderType;
		
		return this;
	}
	
	public Collider groups(long groups) {
		this.groups = groups;
		return this;
	}


	@Override
	protected void reset() {
		groups = 0;
		colliderType = ColliderType.RECT;
		enterListener = null;
		exitListener = null;
	}
	
	public boolean hasGroup(long group) {
		return (this.groups & group) > 0;
	}
	
	public boolean hasGroups(long groups) {
		return (this.groups & groups) > 0;
	}
}

