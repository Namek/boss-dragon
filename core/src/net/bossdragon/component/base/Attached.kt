package net.bossdragon.component.base

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2
import net.bossdragon.system.base.AttachmentSystem

/**
 * @see AttachmentSystem
 */
class Attached : PooledComponent() {
    var entityId: Int = -1

    val displace = Vector2()

    override fun reset() {
        entityId = -1
        displace.setZero()
    }

    fun attachTo(entityId: Int, x: Float, y: Float) {
        this.entityId = entityId
        displace.set(x, y)
    }

    fun attachTo(entityId: Int, displace: Vector2) {
        this.entityId = entityId
        this.displace.set(displace)
    }
}
