package net.bossdragon.system.view.render.debug

import com.artemis.Entity
import net.bossdragon.system.base.collision.ColliderShape
import net.bossdragon.system.base.collision.CollisionDebugSystem

/**
 * Render collision shapes adjusted to graphical Y-axis scale.
 *
 * **Reason to scale**: collision is calculated on circles
 * but game renders ellipse-like shapes due to isometric view.
 */
class CollisionScaledDebugSystem : CollisionDebugSystem() {
    override fun process(e: Entity) {
        val scale = C.Map.RescaleAxisY

        val collider = mCollider[e]

        shapes.color = getColliderDebugColor(e)

        if (collider.colliderShape == ColliderShape.RECT) {
            collisions.calculateColliderRect(e, rect)
            shapes.rect(rect.x, rect.y * scale, rect.width, rect.height * scale)
        }
        else if (collider.colliderShape == ColliderShape.CIRCLE) {
            collisions.calculateColliderCircle(e, circle)
            shapes.ellipse(
                circle.x - circle.radius,
                (circle.y - circle.radius) * scale,
                circle.radius*2,
                circle.radius*2 * scale
            )
        }
        else {
            TODO("other shapes than RECT are not supported")
        }
    }
}
