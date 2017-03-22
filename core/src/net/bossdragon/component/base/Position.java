package net.bossdragon.component.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Logical position.
 */
public class Position extends PooledComponent {
    /** Position set before collision detection. */
    public final Vector2 desiredPos = new Vector2();

    /** Finally accepted position, result of collision checks and physical forces. */
    public final Vector2 currentPos = new Vector2();

    public final Position xy(float x, float y) {
        desiredPos.set(x, y);
        currentPos.set(x, y);

        return this;
    }

    public final Position xy(Vector2 pos) {
        desiredPos.set(pos);
        currentPos.set(pos);

        return this;
    }

    @Override
    protected void reset() {
        desiredPos.setZero();
        currentPos.setZero();
    }
}
