package net.bossdragon.component.render;

import com.artemis.PooledComponent;
import net.bossdragon.component.render.anim.KeyFrameAnimations;

public class Renderable extends PooledComponent {
    public static final int NONE = 0;

    /** @see TextureComponent */
    public static final int TEXTURE = 1 << 1;

    /** @see KeyFrameAnimations */
    public static final int ANIM = 1 << 2;

    /**
     * Layer: higher is in front, lower is behind.
     */
    public int layer = 0;

    /**
     * Mask for combination of renderer types: None, DECAL, TEXTURE, MODEL.
     */
    public int type = NONE;


    @Override
    protected void reset() {
        layer = 0;
        type = 0;
    }

    public Renderable type(final int t) {
        this.type = t;
        return this;
    }
}
