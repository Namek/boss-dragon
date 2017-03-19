package net.bossdragon.system

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.math.Vector2
import net.bossdragon.enums.Xbox360Controller.*
import net.bossdragon.system.base.collision.CollisionDebugSystem
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.system.view.render.RenderSystem

@Wire
class InputSystem : BaseSystem() {
    lateinit internal var renderSystem: RenderSystem
    lateinit internal var collisionDebugSystem: CollisionDebugSystem
    lateinit internal var events: EventSystem
    lateinit internal var entityFactory: EntityFactorySystem
    lateinit internal var characterSystem: CharacterStateSystem

    lateinit internal var inputMultiplexer: InputMultiplexer

    lateinit var input: Input
    internal var controller: Controller? = null


    override fun initialize() {
        inputMultiplexer = InputMultiplexer()
        input = Gdx.input
        input.inputProcessor = inputMultiplexer
        input.isCursorCatched = true

        controller = Controllers.getControllers()
            .filter{ c -> isXbox360Controller(c) }
            .firstOrNull()
    }

    override fun processSystem() {
        if (input.isKeyJustPressed(Keys.GRAVE)) {
            input.isCursorCatched = !Gdx.input.isCursorCatched
        }

        if (input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit()
        }

        if (input.isKeyJustPressed(Keys.D))
            collisionDebugSystem.isEnabled = !collisionDebugSystem.isEnabled

        if (input.isKeyJustPressed(Keys.PAGE_DOWN))
            renderSystem.camera.zoom = Math.max(0.1f, renderSystem.camera.zoom / 1.5f)
        else if (input.isKeyJustPressed(Keys.PAGE_UP))
            renderSystem.camera.zoom = Math.min(10f, renderSystem.camera.zoom * 1.5f)
        else if (input.isKeyJustPressed(Keys.HOME))
            renderSystem.camera.zoom = 1f

        val controller = this.controller
        if (controller != null) {
            if (controller.getButton(BUTTON_A)) {
                characterSystem.requestedSlideOrThrow = true
            }

            val x = controller.getAxis(AXIS_LEFT_X)
            val y = controller.getAxis(AXIS_LEFT_Y)
            characterSystem.dirX = if (Math.abs(x) > 0.5f) Math.signum(x).toInt() else 0
            characterSystem.dirY = if (Math.abs(y) > 0.5f) -Math.signum(y).toInt() else 0
        }


        if (input.isKeyPressed(Keys.LEFT))
            characterSystem.dirX = -1
        else if (input.isKeyPressed(Keys.RIGHT))
            characterSystem.dirX = +1
        else if (controller == null)
            characterSystem.dirX = 0

        if (input.isKeyPressed(Keys.UP))
            characterSystem.dirY = +1
        else if (input.isKeyPressed(Keys.DOWN))
            characterSystem.dirY = -1
        else if (controller == null)
            characterSystem.dirY = 0

        if (input.isKeyJustPressed(Keys.SPACE))
            characterSystem.requestedSlideOrThrow = true


        // TODO just a test, to be removed
        if (input.isKeyJustPressed(Keys.X)) {
            entityFactory.createFireball(Vector2(-600f, 0f), Vector2(1f, 0f))
        }
    }
}
