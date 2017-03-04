package net.bossdragon.system.view.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.bossdragon.component.Player
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Renderable
import net.bossdragon.enums.Tags
import net.bossdragon.system.view.render.renderers.KeyFrameAnimRenderer
import net.bossdragon.system.view.render.renderers.TextureRenderer

/**
 * Basic renderer supporting layers and different renderers.

 * @author Namek
 * *
 * @see Renderable
 */
@Wire(injectInherited = true)
class RenderSystem : RenderBatchingSystem() {
    lateinit var mDimensions: ComponentMapper<Size>
//    internal var mShaders: ComponentMapper<Shaders>

    lateinit var mTransform: ComponentMapper<Transform>
    lateinit var mPlayer: ComponentMapper<Player>

    lateinit var tagManager: TagManager

    lateinit var camera: OrthographicCamera
    lateinit internal var spriteBatch: SpriteBatch
    lateinit internal var textureRenderer: TextureRenderer
    lateinit internal var keyFrameAnimRenderer: KeyFrameAnimRenderer


    override fun initialize() {
        super.initialize()

        world.aspectSubscriptionManager.get(Aspect.all(Renderable::class.java))
            .addSubscriptionListener(this)

        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera = OrthographicCamera(w, h)

        spriteBatch = SpriteBatch()
        textureRenderer = TextureRenderer(world, spriteBatch)
        keyFrameAnimRenderer = KeyFrameAnimRenderer(world, spriteBatch)
    }

    override fun getRendererByType(type: Int): RenderBatchingSystem.EntityProcessAgent {
        when (type) {
            Renderable.TEXTURE -> return textureRenderer
            Renderable.ANIM -> return keyFrameAnimRenderer
            else -> throw RuntimeException("Getting unknown renderer type: " + type)
        }
    }

    override fun processSystem() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)

        camera.update()
        spriteBatch.projectionMatrix = camera.combined
        super.processSystem()

//        val entity = tagManager.getEntity(Tags.Player)
//        val transform = mTransform.get(entity)
//        val player = mPlayer.get(entity)

        /*
        camera.position
                .set(transform.currentPos)
                .add(transform.displacement)
                .add(0, player.eyeAltitude, 0)

        transform.toDirection(camera.direction)
        transform.toUpDir(camera.up)

        camera.update()

        super.processSystem()
        shaderProvider.updateAll(world.getDelta())*/
    }

}
