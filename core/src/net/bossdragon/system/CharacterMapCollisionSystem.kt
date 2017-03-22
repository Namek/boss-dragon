package net.bossdragon.system

import com.artemis.Aspect
import com.artemis.Entity
import com.artemis.managers.TagManager
import com.artemis.systems.EntityProcessingSystem
import com.badlogic.gdx.math.Circle
import net.bossdragon.enums.Tags
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M

/**
 * Makes sure that character is inside of the ellipse (the map).
 */
class CharacterMapCollisionSystem : EntityProcessingSystem(
    Aspect.all(Player::class.java)
) {
    lateinit var mPosition: M<Position>

    lateinit var tags: TagManager
    lateinit var collisions: CollisionSystem

    val circleMap = Circle()
    val circleChar = Circle()


    override fun process(e: Entity) {
        val pos = mPosition[e]
        val map = tags.getEntity(Tags.Map)

        collisions.calculateColliderCircle(e, circleChar, true)
        collisions.calculateColliderCircle(map, circleMap)

        // is player still inside the map?
        if (circleMap.contains(circleChar)) {
            // apply desired position since it's fully acceptable
            pos.currentPos.set(pos.desiredPos)
        }
        else {
            // TODO correct position instead of ignoring it
            pos.desiredPos.set(pos.currentPos)
        }
    }

}
