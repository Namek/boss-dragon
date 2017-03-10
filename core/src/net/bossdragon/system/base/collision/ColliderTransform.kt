package net.bossdragon.system.base.collision

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

/**
 *
 */
class ColliderTransform : PooledComponent() {
    val pos = Vector2()

    override fun reset() {
        pos.setZero()
    }
}
