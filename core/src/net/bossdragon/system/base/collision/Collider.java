package net.bossdragon.system.base.collision;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener;
import net.bossdragon.system.base.collision.messaging.CollisionExitListener;

/**
 * Collider describes parameters for collision tests of certain entity.
 *
 * @author Namek
 * @see CollisionDetectionSystem
 */
public class Collider extends PooledComponent {
    /** Bitset of relations to which this entity belongs. */
    public long groups = 0;

    /** For basic shape types supported by [CollisionDetectionSystem], look into [ColliderShape]  */
    public int colliderShape = ColliderShape.RECT;


    public final Vector2 spatialSize = new Vector2();
    public final Vector2 spatialPos = new Vector2();

    public CollisionEnterListener enterListener;
    public CollisionExitListener exitListener;


    @Override
    protected void reset() {
        groups = 0;
        colliderShape = ColliderShape.RECT;
        spatialSize.setZero();
        spatialPos.setZero();
        enterListener = null;
        exitListener = null;
    }
}
