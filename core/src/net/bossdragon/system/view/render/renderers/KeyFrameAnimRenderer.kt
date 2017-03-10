package net.bossdragon.system.view.render.renderers

import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.base.Size
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.system.view.render.RenderBatchingSystem

class KeyFrameAnimRenderer(
    private val world: World,
    private val batch: SpriteBatch
)
    : RenderBatchingSystem.EntityProcessAgent {

    private var mFrames: ComponentMapper<KeyFrameAnimations>
    private var mTransform: ComponentMapper<Transform>
    private var mSize: ComponentMapper<Size>

    init {
        mFrames = world.getMapper(KeyFrameAnimations::class.java)
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
        val frames = mFrames[e]
        val transform = mTransform.getSafe(e, null)
        val size = mSize.getSafe(e, null)

        var x = 0f
        var y = 0f
        var w = frames.width
        var h = frames.height

        if (size != null) {
            w = size.width
            h = size.height
            x += -size.origin.x * w
            y += -size.origin.y * h
        }

        if (transform != null) {
            x += transform.position.x + transform.displacement.x
            y += transform.position.y + transform.displacement.y
        }

        frames.stateTime += world.getDelta()

        val tex = frames.currentAnimation!!.getKeyFrame(frames.stateTime, true) as TextureRegion
        batch.draw(tex, x, y, w, h)
    }

    override val type: Int
        get() = Renderable.ANIM
}
