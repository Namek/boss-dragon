package net.bossdragon.system.view.render.renderers

import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.render.TextureComponent
import net.bossdragon.system.view.render.RenderBatchingSystem.EntityProcessAgent

class TextureRenderer(world: World, private val batch: SpriteBatch) : EntityProcessAgent {
    private var mTexture: ComponentMapper<TextureComponent>
    private var mTransform: ComponentMapper<Transform>
    private var mSize: ComponentMapper<Size>

    private var lastBlendDstFunc: Int = 0
    private var lastBlendSrcFunc: Int = 0


    init {
        mTexture = world.getMapper(TextureComponent::class.java)
        mTransform = world.getMapper(Transform::class.java)
        mSize = world.getMapper(Size::class.java)
    }

    override fun begin() {
        lastBlendSrcFunc = batch.blendSrcFunc
        lastBlendDstFunc = batch.blendDstFunc
        batch.begin()
    }

    override fun end() {
        batch.end()
        batch.setBlendFunction(lastBlendSrcFunc, lastBlendDstFunc)
    }

    override fun process(e: Entity) {
        val texture = mTexture[e]
        val transform = mTransform[e]
        val size = mSize.getSafe(e, null)

        val width = size?.width ?: texture.texture!!.regionWidth.toFloat()
        val height = size?.height ?: texture.texture!!.regionHeight.toFloat()

        val originX = if (size != null) size.origin.x * width else 0f
        val originY = if (size != null) size.origin.y * height else 0f
        val x = transform.currentPos.x + transform.displacement.x - originX
        val y = transform.currentPos.y + transform.displacement.y - originY

        // TODO size?.scale
        val scaleX = 1f
        val scaleY = 1f

        batch.setBlendFunction(texture.blendSrcFunc, texture.blendDestFunc)

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
