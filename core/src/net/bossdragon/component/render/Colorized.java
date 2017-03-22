package net.bossdragon.component.render;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;

/**
 * Color including transparency channel.
 */
public class Colorized extends PooledComponent {
    public final Color color = Color.WHITE.cpy();
    @Override
    protected void reset() {
        color.set(Color.WHITE);
    }
}
