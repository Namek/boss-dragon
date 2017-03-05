package net.bossdragon.component.base

import com.artemis.Component
import com.badlogic.gdx.math.Vector2
import net.bossdragon.system.base.collision.Collider

/**
 * Defines graphical size and optionally size of [Collider] if not configured otherwise.
 * [origin] is graphical only.
 *
 * @see Collider.SpatialCalculation
 */
class Size : Component() {
    var width = 1f
    var height = 1f

    /** Percentile displacement of origin zeroPoint from bottom left edge. X/Y value range = [0, 1] */
    val origin = Vector2(0f, 0f)

    fun set(w: Float, h: Float): Size {
        width = w
        height = h
        return this
    }

    fun origin(x: Float, y: Float): Size {
        origin.set(x, y)
        return this
    }
}
