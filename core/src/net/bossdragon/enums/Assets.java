package net.bossdragon.enums;

/**
 *
 */
public interface Assets {
    interface Character {
        int Width = 64;
        int Height = 64;
        float ColliderRadius = 25f;
    }

    interface Soldier {
        int Width = 64;
        int Height = 64;
        float ColliderRadius = 15f;
    }
}
