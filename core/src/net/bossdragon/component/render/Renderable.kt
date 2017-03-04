package net.bossdragon.component.render

import com.artemis.PooledComponent

class Renderable : PooledComponent() {
    companion object {
        const val NONE = 0
        const val TEXTURE = 2
        const val ANIM = 4
    }

    /**
     * Layer: higher is in front, lower is behind.
     */
    var layer: Int = 0

    /**
     * Mask for combination of renderer types: NONE, DECAL, TEXTURE, MODEL.
     */
    var type: Int = NONE


    fun type(t: Int): Renderable {
        type = t
        return this
    }

    override fun reset() {
        layer = 0
        type = 0
    }
}
