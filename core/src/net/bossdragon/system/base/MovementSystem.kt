package net.bossdragon.system.base

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.base.Position
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

class MovementSystem : EntityProcessingSystem(
    Aspect.all(Position::class.java)
) {
    lateinit var mPosition: M<Position>

    override fun process(character: Entity) {
        val t = mPosition[character]
        t.currentPos.set(t.desiredPos)
    }
}
