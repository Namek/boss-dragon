package net.bossdragon.component;

import com.artemis.PooledComponent;

/**
 *
 */
public class Soldier extends PooledComponent {
    public float lyingCooldown = 0f;

    @Override
    protected void reset() {
        lyingCooldown = 0f;
    }

    public boolean isLying() {
        return lyingCooldown > 0f;
    }
}
