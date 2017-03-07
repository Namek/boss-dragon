package net.bossdragon.system.base.physics

import com.artemis.PooledComponent
import com.badlogic.gdx.physics.box2d.Body

/**
 *
 */
class Physical : PooledComponent() {
    lateinit var body: Body

    override fun reset() {

    }

}
