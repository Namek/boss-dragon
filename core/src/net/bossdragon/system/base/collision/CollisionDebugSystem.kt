package net.bossdragon.system.base.collision

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.system.view.render.RenderSystem

open class CollisionDebugSystem : EntityProcessingSystem(
    Aspect.all(
        Collider::class.java,
        Transform::class.java,
        Size::class.java
    ))
{
    lateinit var collisions: CollisionDetectionSystem
    lateinit var renderSystem: RenderSystem
    lateinit var shapes: ShapeRenderer

    private val rect = Rectangle()

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

    override fun process(e: Entity) {
        val collider = collisions.calculateColliderRect(e, rect)

        if (collider.colliderShape != ColliderShape.RECT) {
            TODO("other shapes than RECT are not supported")
        }

        shapes.color = getColliderDebugColor(e)
        shapes.rect(rect.x, rect.y, rect.width, rect.height)
    }

    open fun getColliderDebugColor(e: Entity): Color {
        return defaultColor
    }
}
