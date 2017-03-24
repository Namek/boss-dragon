package net.bossdragon.system.view.render.debug;

import com.artemis.Entity;
import net.bossdragon.enums.C;
import net.bossdragon.system.base.collision.Collider;
import net.bossdragon.system.base.collision.ColliderShape;
import net.bossdragon.system.base.collision.CollisionDebugSystem;

/**
 * Render collision shapes adjusted to graphical Y-axis scale.
 *
 * <b>Reason to scale</b>: collision is calculated on circles
 * but game renders ellipse-like shapes due to isometric view.
 */
public class CollisionScaledDebugSystem extends CollisionDebugSystem {
     @Override
    protected void process(Entity e) {
        float scale = C.Map.RescaleAxisY;
        Collider collider = mCollider.get(e);

        shapes.setColor(getColliderDebugColor(e));

        if (collider.colliderShape == ColliderShape.RECT) {
            collisions.calculateColliderRect(e, rect);
            shapes.rect(rect.x, rect.y * scale, rect.width, rect.height * scale);
        }
        else if (collider.colliderShape == ColliderShape.CIRCLE) {
            collisions.calculateColliderCircle(e, circle);
            shapes.ellipse(
                circle.x - circle.radius,
                (circle.y - circle.radius) * scale,
                circle.radius*2,
                circle.radius*2 * scale
            );
        }
        else {
            throw new UnsupportedOperationException("other shapes than RECT are not supported");
        }
    }
}
