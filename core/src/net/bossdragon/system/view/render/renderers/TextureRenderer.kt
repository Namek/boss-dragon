package net.bossdragon.system.view.render.renderers

import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Colorized
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.render.TextureComponent
import net.bossdragon.system.view.render.RenderBatchingSystem.EntityProcessAgent

class TextureRenderer(world: World, private val batch: SpriteBatch) : EntityProcessAgent {
    private var mTexture: ComponentMapper<TextureComponent>
    private var mTransform: ComponentMapper<Transform>
    private var mColorized: ComponentMapper<Colorized>

    private val oldColor = Color()


    init {
        mTexture = world.getMapper(TextureComponent::class.java)
        mTransform = world.getMapper(Transform::class.java)
        mColorized = world.getMapper(Colorized::class.java)
    }

    override fun begin() {
        oldColor.set(batch.color)
        batch.begin()
    }

    override fun end() {
        batch.end()
        batch.setColor(oldColor)
    }

    override fun process(e: Entity) {
        val texture = mTexture[e]
        val transform = mTransform[e]
        val colorized = mColorized.getSafe(e, null)

        val width = texture.texture!!.regionWidth.toFloat()
        val height = texture.texture!!.regionHeight.toFloat()

        val originX = transform.originX * width
        val originY = transform.originY * height
        val x = transform.position.x + transform.displacement.x - originX
        val y = transform.position.y + transform.displacement.y - originY

        // TODO size?.scale
        val scaleX = if (transform.flipX) -1f else 1f
        val scaleY = if (transform.flipY) -1f else 1f

        batch.setBlendFunction(texture.blendSrcFunc, texture.blendDestFunc)

        if (colorized != null)
            batch.setColor(colorized.color)
        else
            batch.setColor(oldColor)

        batch.draw(
            texture.texture!!,
            x, y, originX, originY,
            width, height,
            scaleX, scaleY, transform.rotation
        )
    }

    override val type: Int
        get() = Renderable.TEXTURE
}
