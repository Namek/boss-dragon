package net.bossdragon.system.base;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Attached;
import net.bossdragon.component.base.Position;
import net.bossdragon.component.base.PreviousPosition;
import net.bossdragon.component.base.Velocity;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 * System that calculates **desired** position of moving entity.
 *
 * This system needs a companion system (processing after this one)
 * which will check and modify/copy [Position.desiredPos] position into [Position.currentPos].
 *
 * @see Position
 */
public class PositionSystem extends EntityProcessingSystem {
    public PositionSystem() {
        super(Aspect.all(Position.class, Velocity.class)
            .exclude(Attached.class));
    }

    M<Position> pm;
    M<PreviousPosition> ppm;
    M<Velocity> vm;


    private Vector2 tmpVector = new Vector2();


    @Override
    protected void process(Entity e) {
        Position position = pm.get(e);
        PreviousPosition previousPosition = ppm.get(e);
        Velocity velocity = vm.get(e);

        if (previousPosition != null) {
            previousPosition.pos.set(position.currentPos);
        }

        float deltaTime = world.getDelta();
        calculateDesiredPosition(position, velocity, deltaTime);
    }

    public void calculateDesiredPosition(Position positionComponent, Velocity velocityComponent, float deltaTime) {
        Vector2 velocity = velocityComponent.velocity;
        float maxSpeed = velocityComponent.maxSpeed;

        // Calculate velocity
        tmpVector
            .set(velocityComponent.acceleration)
            .scl(deltaTime)
            .add(velocity)
            .limit(maxSpeed);

        if (velocityComponent.frictionOn) {
            float friction = velocityComponent.friction * deltaTime;
            float speed = tmpVector.len();

            if (friction < speed) {
                // calculate delta velocity with friction
                tmpVector.nor().scl(-friction);
            }
            else {
                tmpVector.set(velocityComponent.velocity).scl(-1f);
            }

            // Add delta velocity
            velocity.add(tmpVector);
        }
        else {
            velocity.set(tmpVector);
        }

        // Calculate position
        tmpVector.set(velocity).scl(deltaTime);
        positionComponent.desiredPos.add(tmpVector);
    }
}
