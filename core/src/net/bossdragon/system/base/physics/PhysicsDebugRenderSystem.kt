package net.bossdragon.system.base.physics

import com.artemis.Aspect
import com.artemis.EntitySystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer

/**
 *
 */
@Wire
class PhysicsDebugRenderSystem : EntitySystem(
    Aspect.all(Physical::class.java)
) {
    @Wire lateinit var camera: OrthographicCamera
    lateinit var physicsSystem: PhysicsSystem

    lateinit var renderer: Box2DDebugRenderer
    var mat = Matrix4()

    override fun initialize() {
        renderer = Box2DDebugRenderer()
        renderer.isDrawVelocities = true
    }

    override fun processSystem() {
        camera.update()
        mat.set(camera.combined)

        renderer.render(physicsSystem.engine, mat)
    }
}
