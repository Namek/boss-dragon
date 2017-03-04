package net.bossdragon.system.base

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.bossdragon.component.base.Progressing
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

class TimeProgressingSystem : EntityProcessingSystem(Aspect.all(Progressing::class.java)) {
    lateinit var mProgressing: M<Progressing>

    override fun process(e: Entity) {
        mProgressing[e].stateTime += world.getDelta()
    }
}
