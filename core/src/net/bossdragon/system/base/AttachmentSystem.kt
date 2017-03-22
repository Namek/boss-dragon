package net.bossdragon.system.base

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.math.Vector2
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 *
 */
class AttachmentSystem : EntityProcessingSystem(
    Aspect.all(Attached::class.java, Position::class.java)
) {
    lateinit var mAttached: M<Attached>
    lateinit var mPosition: M<Position>

    val tmpPos = Vector2()

    override fun process(e: Entity) {
        val att = mAttached[e]
        val pos = mPosition[e]
        val attPos = mPosition[att.entityId]

        tmpPos.set(attPos.desiredPos).add(att.displace)
        pos.xy(tmpPos)
    }

}
