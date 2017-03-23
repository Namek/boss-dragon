package net.bossdragon.system.base.collision;

import java.util.TreeMap;

/**
 *
 * @author Namek
 * @todo **Pleease**, optimizee meee!
 */
public class CollisionPhases {
    public static final int None = 0;
    public static final int Entered = 1;
    public static final int Existing = 2;

    /** Maps entity1Id to entity2d which maps to phase  */
    private TreeMap<Integer, TreeMap<Integer, Integer>> collisionPhases = new TreeMap<>();


    public void set(int entity1Id, int entity2Id, int phase) {
        if (entity2Id < entity1Id) {
            int tmp = entity1Id;
            entity1Id = entity2Id;
            entity2Id = tmp;
        }

        TreeMap<Integer, Integer> relations = collisionPhases.get(entity1Id);

        if (relations == null) {
            relations = new TreeMap<Integer, Integer>();
            collisionPhases.put(entity1Id, relations);
        }

        relations.put(entity2Id, phase);
    }

    public int get(int entity1Id, int entity2Id) {
        if (entity2Id < entity1Id) {
            int tmp = entity1Id;
            entity1Id = entity2Id;
            entity2Id = tmp;
        }

        TreeMap<Integer, Integer> relations = collisionPhases.get(entity1Id);
        if (relations != null) {
            Integer phase = relations.get(entity2Id);

            if (phase != null) {
                return phase;
            }
        }

        return None;
    }

    public void clear(int entityId) {
        TreeMap<Integer, Integer> relations = collisionPhases.get(entityId);
        relations.clear();

        for (TreeMap<Integer, Integer> entry : collisionPhases.values()) {
            entry.remove(entityId);
        }
    }
}
