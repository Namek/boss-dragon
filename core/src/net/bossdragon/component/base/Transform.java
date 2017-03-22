package net.bossdragon.component.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Graphical position, rotation and flip.
 */
public class Transform extends PooledComponent {
    /** UP on screen */
    public static final Vector2 DEFAULT_DIRECTION = new Vector2(0f, 1f);

    /** Current position to render. It's directly transformed from [Position] component. */
    public Vector2 position = new Vector2();

    /** Additional displacement to position. Usually used for graphics puroses, like head bobbing. */
    public Vector2 displacement = new Vector2();

    /** degrees */
    public float rotation = 0f;

    /** used for [rotation]. range: [0, 1] */
    public float originX = 0.5f;

    /** used for [rotation]. range: [0, 1] */
    public float originY = 0.5f;

    public boolean flipX = false;
    public boolean flipY = false;


    public Transform xy(float x, float y) {
        position.set(x, y);
        return this;
    }

    public Transform xy(Vector2 pos) {
        position.set(pos);
        return this;
    }

    public Transform flip(boolean x, boolean y) {
        flipX = x;
        flipY = y;
        return this;
    }

    @Override
    protected void reset() {
        position.setZero();
        displacement.setZero();
        rotation = 0f;
        originX = 0.5f;
        originY = 0.5f;
        flipX = false;
        flipY = false;
    }

    public Vector2 toDirection(Vector2 outDir) {
        return outDir
            .set(DEFAULT_DIRECTION)
            .rotate(rotation);
    }
}
