package net.bossdragon.util.ai

import com.badlogic.gdx.ai.utils.Location
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Velocity

/**
 *
 */
class LocationableEntity(val pos: Position, val vel: Velocity) : Location<Vector2> {


    override fun setOrientation(orientation: Float) {

    }

    override fun vectorToAngle(vector: Vector2): Float {
        TODO("not implemented")
    }

    override fun getPosition(): Vector2 {
        return pos.currentPos
    }

    override fun angleToVector(outVector: Vector2, angle: Float): Vector2 {
        TODO("not implemented")
    }

    override fun newLocation(): Location<Vector2> {
        TODO("not implemented")
    }

    override fun getOrientation(): Float {
        // TODO
        return 0f
    }

}
