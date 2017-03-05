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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import net.bossdragon.enums.Xbox360Controller.*
import net.bossdragon.system.base.collision.CollisionDebugSystem
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.system.view.render.RenderSystem

@Wire
class InputSystem : BaseSystem() {
    lateinit internal var renderSystem: RenderSystem
    lateinit internal var collisionDebugSystem: CollisionDebugSystem
    lateinit internal var events: EventSystem
    lateinit internal var playerSystem: PlayerStateSystem

    lateinit internal var inputMultiplexer: InputMultiplexer
    lateinit internal var debugCamController: CameraInputController

    internal var enableDebugCamera = false
    private val isDebugCamEnabled = false

    lateinit var input: Input
    internal var controller: Controller? = null


    override fun initialize() {
        inputMultiplexer = InputMultiplexer()
        input = Gdx.input
        input.inputProcessor = inputMultiplexer

        //		debugCamController = new CameraInputController(renderSystem.camera);
        //		debugCamController.rotateAngle = -180;

        input.isCursorCatched = true

        controller = Controllers.getControllers()
            .filter{ c -> isXbox360Controller(c) }
            .firstOrNull()
    }

    override fun processSystem() {
        // Toggle debug camera
        if (input.isKeyJustPressed(Keys.C)) {
            enableDebugCamera = !enableDebugCamera
        }

        if (enableDebugCamera && !isDebugCamEnabled) {
            inputMultiplexer.addProcessor(debugCamController!!)
        }
        else if (!enableDebugCamera && isDebugCamEnabled) {
            inputMultiplexer.removeProcessor(debugCamController)
        }

        if (enableDebugCamera) {
            debugCamController!!.update()
        }

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
                playerSystem.requestedSlide = true
            }

            val x = controller.getAxis(AXIS_LEFT_X)
            val y = controller.getAxis(AXIS_LEFT_Y)
            playerSystem.dirX = if (Math.abs(x) > 0.5f) Math.signum(x).toInt() else 0
            playerSystem.dirY = if (Math.abs(y) > 0.5f) -Math.signum(y).toInt() else 0
        }


        if (input.isKeyPressed(Keys.LEFT))
            playerSystem.dirX = -1
        else if (input.isKeyPressed(Keys.RIGHT))
            playerSystem.dirX = +1
        else if (controller == null)
            playerSystem.dirX = 0

        if (input.isKeyPressed(Keys.UP))
            playerSystem.dirY = +1
        else if (input.isKeyPressed(Keys.DOWN))
            playerSystem.dirY = -1
        else if (controller == null)
            playerSystem.dirY = 0

        if (input.isKeyJustPressed(Keys.SPACE))
            playerSystem.requestedSlide = true
    }
}
