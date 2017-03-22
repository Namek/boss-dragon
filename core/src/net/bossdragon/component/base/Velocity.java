package net.bossdragon.component.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.system.base.PositionSystem;

/**
 * To move entity, play with {@link #acceleration}.
 * Modify {@link #velocity} onlly to immediately stop entity.
 *
 *  Issues:
 *  <ul>
 *      <li>entity doesn't move? Ensure {@code maxSpeed > 0} and friction is off</li>
 *      <li>friction doesn't work? {@code frictionOn = true}</li>
 *  </ul>
 *
 *  @see PositionSystem
 */
public class Velocity extends PooledComponent {
    public final Vector2 velocity = new Vector2();
    public final Vector2 acceleration = new Vector2();

    public float maxSpeed = 0f;
    public float friction = 0f;
    public boolean frictionOn = false;

    @Override
    protected void reset() {
        velocity.setZero();
        acceleration.setZero();
        maxSpeed = 0;
        friction = 0;
        frictionOn = false;
    }

    public Velocity maxSpeed(float s) {
        this.maxSpeed = s;
        return this;
    }

    public Velocity friction(float f) {
        this.friction = f;
        this.frictionOn = true;
        return this;
    }

    public void immediateStop() {
        velocity.setZero();
        acceleration.setZero();
    }

    public void setMovement(float dirX, float dirY, float accel) {
        final boolean isMoving = dirX*dirX + dirY*dirY != 0f;
        frictionOn = !isMoving;
        acceleration.set(dirX, dirY);

        if (isMoving)
            acceleration.scl(accel);
    }

    public void setMovement(int dirX, int dirY, float accel) {
        final boolean isMoving = dirX + dirY*2 != 0;
        frictionOn = !isMoving;
        acceleration.set(dirX, dirY);

        if (isMoving)
            acceleration.scl(accel);
    }

    public void setMovement(float dirX, float dirY) {
        setMovement(dirX, dirY, maxSpeed);
    }

    public void setMovement(int dirX, int dirY) {
        setMovement(dirX, dirY, maxSpeed);
    }

    public void setMovement(final Vector2 dir) {
        setMovement(dir.x, dir.y);
    }

    public void setMovementWithEpsilon(float dirX, float dirY, float epsilon) {
        final boolean isMoving = Math.abs(dirX*dirX + dirY*dirY) > epsilon;
        frictionOn = !isMoving;
        acceleration.set(dirX, dirY);

        if (isMoving)
            acceleration.scl(maxSpeed);
    }

    public void setMovementWithEpsilon(float dirX, float dirY) {
        setMovementWithEpsilon(dirX, dirY, 0.0001f);
    }

    public void setVelocityAtMax(Vector2 dir) {
        velocity.set(dir).setLength(maxSpeed);
    }

    public boolean isMoveRequested() {
        return acceleration.len2() > 0;
    }

    public boolean isMoving() {
        return velocity.len2() > 0;
    }

    public boolean isStopping() {
        return isMoving() && !isMoveRequested();
    }

    public boolean isIdle() {
        return !isMoving() && !isMoveRequested();
    }
}
