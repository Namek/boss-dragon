package net.bossdragon.component.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class PreviousPosition extends PooledComponent {
    public final Vector2 pos = new Vector2(0f, 0f);

    @Override
    protected void reset() {
        pos.setZero();
    }
}
