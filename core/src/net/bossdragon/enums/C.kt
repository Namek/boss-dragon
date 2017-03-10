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

        val MaxSpeedOnSlide = MaxSpeed * 1.5f
        val AccelerationOnSlide = Acceleration * 2f

        val SlideDuration = 0.4f
        val SlideCooldownDuration = 0.3f
    }

    object Fireball {
        val MaxSpeed = 600f
        val Size = 60f
    }
}
