package net.bossdragon.component.render.anim

import com.artemis.PooledComponent
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.system.view.render.anim.KeyFrameAnimationsMap
import net.bossdragon.util.data.Tuple

/**
 * This component needs {@link Progressing} component!
 */
class KeyFrameAnimations : PooledComponent() {
    var currentAnimation: Animation<*>? = null
        private set

    @JvmField var animations: KeyFrameAnimationsMap? = null
    var width: Float = 0.toFloat()
    var height: Float = 0.toFloat()
    var stateTime: Float = 0f


    init {
        this.animations = KeyFrameAnimationsMap()
    }

    fun setup(animation: Tuple<String, Animation<*>>, vararg otherAnimations: Tuple<String, Animation<*>>): KeyFrameAnimations {
        this.animations = KeyFrameAnimationsMap()
        this.currentAnimation = animation.Item2

        animations!!.put(animation.Item1, animation.Item2)
        for (tuple in otherAnimations) {
            animations!!.put(tuple.Item1, tuple.Item2)
        }
        return this
    }

    fun setup(animations: KeyFrameAnimationsMap): KeyFrameAnimations {
        this.animations = animations
        val firstFrame = animations.get(0).keyFrames[0] as TextureRegion
        this.width = firstFrame.getRegionWidth().toFloat()
        this.height = firstFrame.getRegionHeight().toFloat()
        currentAnimation = this.animations!![0]

        return this
    }

    fun setAnimation(index: Int): Boolean {
        val newAnim = animations!![index]
        val isChanging = newAnim != currentAnimation

        currentAnimation = newAnim
        return isChanging
    }

    fun setAnimation(name: String): Boolean {
        val newAnim = animations!![name]
        val isChanging = newAnim != currentAnimation

        currentAnimation = animations!![name]
        return isChanging
    }


    override fun reset() {
        currentAnimation = null
        animations = null
        height = 0f
        width = 0f
    }
}
