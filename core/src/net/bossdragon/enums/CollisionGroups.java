package net.bossdragon.enums;

public interface CollisionGroups {
    long CHARACTER = 1;
    long SOLDIER = 1 << 1;
    long BULLET = 1 << 2;
    long WALL = 1 << 3;
    long DRAGON = 1 << 4;
    long FIREBALL = 1 << 5;
    long BURNING_AREA = 1 << 6;
}
