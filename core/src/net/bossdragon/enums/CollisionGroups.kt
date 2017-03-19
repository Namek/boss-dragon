package net.bossdragon.enums

object CollisionGroups {
    val CHARACTER: Long = 1
    val SOLDIER = (1 shl 1).toLong()
    val BULLET = (1 shl 2).toLong() //made of enemy
    val WALL = (1 shl 3).toLong()
    val DRAGON = (1 shl 4).toLong()
    val FIREBALL = (1 shl 5).toLong()
    val BURNING_AREA = (1 shl 6).toLong()
}
