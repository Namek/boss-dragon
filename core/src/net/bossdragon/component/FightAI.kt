package net.bossdragon.component

import com.artemis.PooledComponent

/**
 *
 */
class FightAI : PooledComponent() {
    var trackPlayer: Boolean = false

    override fun reset() {
        trackPlayer = false
    }
}
