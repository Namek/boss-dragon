package net.bossdragon.component.base

import com.artemis.PooledComponent

class Progressing : PooledComponent() {
    var stateTime: Float = 0f

    override fun reset() {
    }

}
