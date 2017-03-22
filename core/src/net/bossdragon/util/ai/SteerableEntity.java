package net.bossdragon.util.ai;

import com.artemis.Entity;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Velocity;
import net.bossdragon.system.CollisionSystem;

/**
 *
 */
public class SteerableEntity implements Steerable<Vector2> {
    private boolean _tagged = false;

    public Entity entity;
    public Velocity vel;
    public CollisionSystem collisionSystem;

    private final Circle circle = new Circle();
    private final Vector2 pos = new Vector2();
    private float lastTime = 0f;


    private void recalculateCollider() {
        final float currentTime = GdxAI.getTimepiece().getTime();

        // If the frame is new then avoid repeating calculations
        // when used multiple times per frame.
        if (this.lastTime != currentTime) {
            this.lastTime = currentTime;

            collisionSystem.calculateColliderCircle(entity, circle);
        }
    }



    @Override
    public Vector2 getLinearVelocity() {
        return vel.velocity;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        recalculateCollider();
        return circle.radius;
    }

    @Override
    public boolean isTagged() {
        return _tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        _tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return vel.maxSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        vel.maxSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 0;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }

    @Override
    public Vector2 getPosition() {
        recalculateCollider();
        return pos.set(circle.x, circle.y);
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
