package net.bossdragon.system.base.collision

import java.util.*

/**
 * @author Namek
 * *
 * @todo **Pleease**, optimizee meee!
 */
class CollisionPhases {
    companion object Phase {
        val None = 1
        val Entered = 2
        val Existing = 3
    }

    /** Maps entity1Id to entity2d which maps to phase  */
    private val collisionPhases = TreeMap<Int, MutableMap<Int, Int>>()


    operator fun set(entity1Id: Int, entity2Id: Int, phase: Int) {
        var entity1Id = entity1Id
        var entity2Id = entity2Id
        if (entity2Id < entity1Id) {
            val tmp = entity1Id
            entity1Id = entity2Id
            entity2Id = tmp
        }

        var relations: MutableMap<Int, Int>? = collisionPhases[entity1Id]

        if (relations == null) {
            relations = TreeMap<Int, Int>()
            collisionPhases.put(entity1Id, relations)
        }

        relations.put(entity2Id, phase)
    }

    operator fun get(entity1Id: Int, entity2Id: Int): Int {
        var entity1Id = entity1Id
        var entity2Id = entity2Id
        if (entity2Id < entity1Id) {
            val tmp = entity1Id
            entity1Id = entity2Id
            entity2Id = tmp
        }

        val relations = collisionPhases[entity1Id]
        if (relations != null) {
            val phase = relations[entity2Id]

            if (phase != null) {
                return phase
            }
        }

        return Phase.None
    }

    fun clear(entityId: Int) {
        val relations = collisionPhases[entityId]

        relations?.clear()

        for (entry in collisionPhases.values) {
            entry.remove(entityId)
        }
    }
}
