package net.bossdragon.util.ai;

import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class BlendedSteering extends com.badlogic.gdx.ai.steer.behaviors.BlendedSteering<Vector2> {

    public BlendedSteering(SteerableEntity owner) {
        super(owner);
    }

    public void clear() {
        this.list.clear();
    }
}
