package net.bossdragon.system.base.collision

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.EntitySystem
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle
import net.bossdragon.component.base.Position
import net.bossdragon.system.view.render.RenderSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

open class CollisionDebugSystem : EntitySystem(
    Aspect.all(
        Collider::class.java,
        Position::class.java
    ))
{
    lateinit var mCollider: M<Collider>

    lateinit var collisions: CollisionDetectionSystem
    lateinit var renderSystem: RenderSystem
    lateinit var shapes: ShapeRenderer

    protected val rect = Rectangle()
    protected val circle = Circle()

    val defaultColor: Color = Color.PINK.cpy()


    override fun initialize() {
        shapes = ShapeRenderer()
    }

    override fun begin() {
        shapes.transformMatrix.set(renderSystem.camera.view)
        shapes.projectionMatrix.set(renderSystem.camera.projection)
        shapes.updateMatrices()
        shapes.begin(ShapeRenderer.ShapeType.Line)
    }

    override fun end() {
        shapes.end()
    }

    /** @inheritDoc
     */
    override fun processSystem() {
        val entities = entities
        val array = entities.data
        var i = 0
        val s = entities.size()
        while (s > i) {
            process(array[i] as Entity)
            i++
        }
    }

    open fun process(e: Entity) {
        val collider = mCollider[e]

        shapes.color = getColliderDebugColor(e)

        if (collider.colliderShape == ColliderShape.RECT) {
            collisions.calculateColliderRect(e, rect)
            shapes.rect(rect.x, rect.y, rect.width, rect.height)
        }
        else if (collider.colliderShape == ColliderShape.CIRCLE) {
            collisions.calculateColliderCircle(e, circle)
            shapes.circle(circle.x, circle.y, circle.radius)
        }
        else {
            TODO("other shapes than RECT are not supported")
        }
    }

    open fun getColliderDebugColor(e: Entity): Color {
        return defaultColor
    }
}
