package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Transform
import net.bossdragon.enums.C
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 * Transform logical positions from circular space to ellipsoid graphics.
 *
 * Logical center of the tower (game map) is point (0, 0), same as graphical.
 */
class LogicalToGraphicalPositionmentSystem : EntityProcessingSystem(
    Aspect.all(Transform::class.java, Position::class.java)
) {
    lateinit var mTransform: M<Transform>
    lateinit var mPosition: M<Position>

    override fun process(character: Entity) {
        val pos = mPosition[character].currentPos
        val transPos = mTransform[character].position

        transPos.set(pos.x, pos.y * C.Map.RescaleAxisY)
    }
}
