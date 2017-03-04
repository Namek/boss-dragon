package net.bossdragon.system.base

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.base.Transform
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

class MovementSystem : EntityProcessingSystem(
    Aspect.all(Transform::class.java)
) {
    lateinit var mTransform: M<Transform>

    override fun process(e: Entity) {
        val t = mTransform[e]
        t.currentPos.set(t.desiredPos)
    }
}
