package net.bossdragon.component

import com.artemis.PooledComponent

class Enemy : PooledComponent() {
    override fun reset() {
        lyingCooldown = 0f
    }

    var lyingCooldown: Float = 0f
}
