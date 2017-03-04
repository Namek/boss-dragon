package net.bossdragon.system.view.render.renderers

import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.base.Progressing
import net.bossdragon.component.base.Size
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.system.view.render.RenderBatchingSystem

class KeyFrameAnimRenderer(world: World, private val batch: SpriteBatch)
    : RenderBatchingSystem.EntityProcessAgent {

    private var mFrames: ComponentMapper<KeyFrameAnimations>
    private var mProgressing: ComponentMapper<Progressing>
    private var mTransform: ComponentMapper<Transform>
    private var mSize: ComponentMapper<Size>

    init {
        mFrames = world.getMapper(KeyFrameAnimations::class.java)
        mProgressing = world.getMapper(Progressing::class.java)
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
        val progress = mProgressing[e]
        val transform = mTransform.getSafe(e, null)
        val size = mSize.getSafe(e, null)

        var x = 0f
        var y = 0f

        if (size != null) {
            x += -size.origin.x * frames.width
            y += -size.origin.y * frames.height
        }

        if (transform != null) {
            x += transform.currentPos.x + transform.displacement.x
            y += transform.currentPos.y + transform.displacement.y
        }

        val tex = frames.currentAnimation!!.getKeyFrame(progress.stateTime, true) as TextureRegion
        batch.draw(tex, x, y)
    }

    override val type: Int
        get() = Renderable.ANIM
}
