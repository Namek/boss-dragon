package net.bossdragon.util.ai

import com.badlogic.gdx.math.Vector2

/**
 *
 */
class BlendedSteering(owner: SteerableEntity) : com.badlogic.gdx.ai.steer.behaviors.BlendedSteering<Vector2>(owner) {
    fun clear() {
        this.list.clear()
    }
}
