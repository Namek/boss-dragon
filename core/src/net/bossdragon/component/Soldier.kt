package net.bossdragon.component

import com.artemis.PooledComponent

class Soldier : PooledComponent() {
    override fun reset() {
        lyingCooldown = 0f
    }

    var lyingCooldown: Float = 0f

    val isLying: Boolean
        get() = lyingCooldown > 0f
}
