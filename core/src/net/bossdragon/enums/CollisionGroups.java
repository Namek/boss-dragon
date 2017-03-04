package net.bossdragon.enums;

public class CollisionGroups {
	public static final long PLAYER = 1;
	public static final long ENEMY = 1 << 1;
	public static final long BULLET = 1 << 2; //made of enemy
	public static final long WALL = 1 << 3;
    public static final long DRAGON = 1 << 4;
    public static final long FIREBALL = 1 << 5;
    public static final long BURNING_AREA = 1 << 6;
}
