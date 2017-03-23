package net.bossdragon.component.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.system.base.AttachmentSystem;

/**
 * @see AttachmentSystem
 */
public class Attached extends PooledComponent {
    public int entityId = -1;
    public final Vector2 displace = new Vector2();


    protected void reset() {
        this.entityId = -1;
        this.displace.setZero();
    }

    public final void attachTo(int entityId, float x, float y) {
        this.entityId = entityId;
        this.displace.set(x, y);
    }

    public final void attachTo(int entityId, Vector2 displace) {
        this.entityId = entityId;
        this.displace.set(displace);
    }
}
