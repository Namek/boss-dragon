package net.bossdragon.system.base.collision;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import net.bossdragon.component.base.Position;
import net.bossdragon.system.base.collision.messaging.CollisionEvent;
import net.bossdragon.system.base.events.EventSystem;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 * Collision detector working on top of groups and group relations.
 *
 * @todo optimize system to be processed in configurable intervals
 * @todo optimize by caching entities belongingness to groups, see [.processEntities] comment.
 */
public class CollisionDetectionSystem extends EntitySystem {
    public CollisionDetectionSystem(CollisionGroupsRelations relations, boolean eventDispatchingEnabled) {
        super(Aspect.all(Collider.class, Position.class));
        this.relations = relations;
    }

    public CollisionDetectionSystem(CollisionGroupsRelations relations) {
        this(relations, false);
    }

    public CollisionDetectionSystem(boolean eventDispatchingEnabled) {
        this(new CollisionGroupsRelations());
    }


    M<Collider> mCollider;
    M<Position> mPosition;

    EventSystem events;

    boolean eventDispatchingEnabled = false;
    final CollisionGroupsRelations relations;
    final CollisionPhases phases = new CollisionPhases();

    private final Rectangle rect1 = new Rectangle();
    private final Rectangle rect2 = new Rectangle();
    private final Circle circle1 = new Circle();
    private final Circle circle2 = new Circle();

    @Override
    protected void processSystem() {
        processEntities(subscription.getEntities());
    }

    /**
     * **TODO**: optimize by caching entities belongingness to groups.
     * Now all entities are checked against themselves needlessly,
     * e.g. thanks to groups relations Bullets will never collide with Bullets
     * so it doesn't make much performance sense to check those relations between those entities.
     */
    protected void processEntities(IntBag entities) {
        int[] ids = entities.getData();

        int i = 0;
        int n = entities.size();
        while (i < n) {
            int entity1Id = ids[i];
            Collider collider1 = mCollider.get(entity1Id);

            for (int j = i+1; j < n; ++j) {
                int entity2Id = ids[j];
                Collider collider2 = mCollider.get(entity2Id);

                int phase = phases.get(entity1Id, entity2Id);

                if (phase == CollisionPhases.None) {
                    if (!relations.anyRelationExists(collider1.groups, collider2.groups)) {
                        continue;
                    }

                    if (checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        onCollisionEnter(entity1Id, collider1, entity2Id, collider2);
                        phases.set(entity1Id, entity2Id, CollisionPhases.Entered);
                    }
                }
                else if (phase == CollisionPhases.Existing) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases.set(entity1Id, entity2Id, CollisionPhases.None);
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2);
                    }
                }
                else if (phase == CollisionPhases.Entered) {
                    if (!checkOverlap(entity1Id, collider1, entity2Id, collider2)) {
                        phases.set(entity1Id, entity2Id, CollisionPhases.None);
                        onCollisionExit(entity1Id, collider1, entity2Id, collider2);
                    }
                    else {
                        phases.set(entity1Id, entity2Id, CollisionPhases.Existing);
                    }
                }
            }
            ++i;
        }
    }

    public boolean checkOverlap(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
        Position pos1 = mPosition.get(entity1Id);
        Position pos2 = mPosition.get(entity1Id);

        boolean isRect1 = collider1.colliderShape == ColliderShape.RECT;
        boolean isRect2 = collider2.colliderShape == ColliderShape.RECT;
        boolean isCircle1 = collider1.colliderShape == ColliderShape.CIRCLE;
        boolean isCircle2 = collider2.colliderShape == ColliderShape.CIRCLE;


        if (isRect1) {
            setColliderRect(collider1, pos1, rect1, true);
        }
        else if (isCircle1) {
            setColliderCircle(collider1, pos1, circle1, true);
        }
        else {
            throw new UnsupportedOperationException();
        }

        if (isRect2) {
            setColliderRect(collider2, pos2, rect2, true);
        }
        else if (isCircle2) {
            setColliderCircle(collider2, pos2, circle2, true);
        }
        else {
            throw new UnsupportedOperationException();
        }

        boolean overlaps =
            (isRect1 && isRect2 && rect1.overlaps(rect2))
         || (isCircle1 && isCircle2 && circle1.overlaps(circle2))
         || (isRect1 && isCircle2 && Intersector.overlaps(circle2, rect1))
         || (isCircle1 && isRect2 && Intersector.overlaps(circle1, rect2));

        return overlaps;
    }

    public Collider calculateColliderRect(Entity e, Rectangle outRect, boolean desiredPos) {
        Collider collider = mCollider.get(e);
        setColliderRect(collider, mPosition.get(e), outRect, desiredPos);
        return collider;
    }

    public Collider calculateColliderRect(Entity e, Rectangle outRect) {
        return calculateColliderRect(e, outRect, false);
    }

    private void setColliderRect(Collider collider, Position pos, Rectangle outRect, boolean desiredPos) {
        outRect.setSize(collider.spatialSize.x, collider.spatialSize.y);
        outRect.setPosition(desiredPos ? pos.desiredPos : pos.currentPos);
        outRect.x += collider.spatialPos.x;
        outRect.y += collider.spatialPos.y;
    }

    public Collider calculateColliderCircle(Entity e, Circle outCircle, boolean desiredPos) {
        Collider collider = mCollider.get(e);
        setColliderCircle(collider, mPosition.get(e), outCircle, desiredPos);
        return collider;
    }

    public Collider calculateColliderCircle(Entity e, Circle outCircle) {
        return calculateColliderCircle(e, outCircle, false);
    }

    private void setColliderCircle(Collider collider, Position pos, Circle outCircle, boolean desiredPos) {
        outCircle.setRadius(collider.spatialSize.x / 2);;
        outCircle.setPosition(desiredPos ? pos.desiredPos : pos.currentPos);
        outCircle.x +=collider.spatialPos.x;
        outCircle.y += collider.spatialPos.y;
    }

    @Override
    public void removed(Entity e) {
        phases.clear(e.getId());
    }

    public void onCollisionEnter(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
        if (collider1.enterListener != null) {
            collider1.enterListener.onCollisionEnter(entity1Id, entity2Id);
        }

        if (collider2.enterListener != null) {
            collider2.enterListener.onCollisionEnter(entity2Id, entity1Id);
        }

        if (eventDispatchingEnabled) {
            events
                .dispatch(CollisionEvent.class)
                .setup(entity1Id, entity2Id, CollisionEvent.ENTER);
        }
    }

    public void onCollisionExit(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
        if (collider1.exitListener != null) {
            collider1.exitListener.onCollisionExit(entity1Id, entity2Id);
        }

        if (collider2.exitListener != null) {
            collider2.exitListener.onCollisionExit(entity2Id, entity1Id);
        }

        if (eventDispatchingEnabled) {
            events
                .dispatch(CollisionEvent.class)
                .setup(entity1Id, entity2Id, CollisionEvent.EXIT);
        }
    }
}
