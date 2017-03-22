package net.bossdragon.util.ai;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Position;
import net.bossdragon.component.base.Velocity;

/**
 *
 */
public class LocationableEntity implements Location<Vector2> {
    public final Position pos;
    public final Velocity vel;

    public LocationableEntity(final Position pos, final Velocity vel) {
        this.pos = pos;
        this.vel = vel;
    }

    @Override
    public Vector2 getPosition() {
        return pos.currentPos;
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return null;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }
}
