package net.bossdragon.system.view.render.renderers

import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.World
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


    init {
        mTexture = world.getMapper(TextureComponent::class.java)
        mTransform = world.getMapper(Transform::class.java)
        mSize = world.getMapper(Size::class.java)
    }

    override fun begin() {
        batch.begin()
    }

    override fun end() {
        batch.end()
    }

    override fun process(e: Entity) {
        val texture = mTexture[e]
        val transform = mTransform[e]
        val size = mSize[e]

        val x = transform.currentPos.x + transform.displacement.x
        val y = transform.currentPos.y + transform.displacement.y
        val originX = 0f
        val originY = 0f
        val scaleX = 1f
        val scaleY = 1f

        batch.setBlendFunction(texture.blendSrcFunc, texture.blendDestFunc)

        batch.draw(
            texture.texture!!,
            x, y, originX, originY,
            size.width, size.height,
            scaleX, scaleY, transform.rotation
        )
    }

    override val type: Int
        get() = Renderable.TEXTURE
}
