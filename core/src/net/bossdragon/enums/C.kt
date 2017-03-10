package net.bossdragon.enums

/**
 * Constants.
 */
object C {
    object Map {
        val Width = 600f
        val Height = 440f
        val LogicalRadius = Width/2

        /** Logical -> Graphical */
        val RescaleAxisY = Height / Width
    }

    object Player {
        val MaxSpeed = 400f
        val Acceleration = MaxSpeed * 12
        val Friction = MaxSpeed * 10
    }

    object Fireball {
        val MaxSpeed = 600f
        val Size = 60f
    }
}
