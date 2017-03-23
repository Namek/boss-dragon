package net.bossdragon.system.base.collision;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import net.bossdragon.component.base.Position;
import net.bossdragon.system.view.render.RenderSystem;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 *
 */
public class CollisionDebugSystem extends EntityProcessingSystem {
    M<Collider> mCollider;

    CollisionDetectionSystem collisions;
    RenderSystem renderSystem;
    ShapeRenderer shapes;

    protected final Rectangle rect = new Rectangle();
    protected final Circle circle = new Circle();

    public final Color defaultColor = Color.PINK.cpy();


    CollisionDebugSystem() {
        super(Aspect.all(Collider.class, Position.class));
    }

    @Override
    public void initialize() {
        shapes = new ShapeRenderer();
    }

    @Override
    protected void begin() {
        shapes.getTransformMatrix().set(renderSystem.camera.view);
        shapes.getProjectionMatrix().set(renderSystem.camera.projection);
        shapes.updateMatrices();
        shapes.begin(ShapeRenderer.ShapeType.Line);
    }

    @Override
    protected void end() {
        shapes.end();
    }

    @Override
    protected void process(Entity e) {
        Collider collider = mCollider.get(e);

        shapes.setColor(getColliderDebugColor(e));
        if (collider.colliderShape == ColliderShape.RECT) {
            collisions.calculateColliderRect(e, rect);
            shapes.rect(rect.x, rect.y, rect.width, rect.height);
        }
        else if (collider.colliderShape == ColliderShape.CIRCLE) {
            collisions.calculateColliderCircle(e, circle);
            shapes.circle(circle.x, circle.y, circle.radius);
        }
        else {
            throw new UnsupportedOperationException("other shapes than RECT are not supported");
        }
    }

    public Color getColliderDebugColor(Entity e) {
        return defaultColor;
    }
}
