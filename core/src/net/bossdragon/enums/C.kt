package net.bossdragon.enums

import com.badlogic.gdx.math.Vector2

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

        val SpawnFlyDirections = arrayOf(
            Vector2(1f, -1f).nor(),
            Vector2(-1f, -1f).nor(),
            Vector2(-1f, 1f).nor(),
            Vector2(1f, 1f).nor()
        )

        // place those points around map circle
        // assume: map center is placed on (0, 0)
        val SpawnLandPoints = SpawnFlyDirections.map { dir ->
            Vector2(dir).scl(-(LogicalRadius - Assets.Enemy.ColliderRadius))
        }
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

    object Enemy {
        val MaxEnemyCount = 8
        val SpawnCooldown = 1f
        val StunCooldown = 3f

        /** distance to spawn point */
        val SpawnFlyDistance = Map.LogicalRadius / 2
        val MaxSpeed = Player.MaxSpeed/5
        val Friction = Player.Friction

        object AI {
            /** Time between recalculations about who's going to attack */
            val StrategyCooldown = 1f

            val PlayerCloseRangeRadius = Assets.Character.ColliderRadius + Assets.Enemy.ColliderRadius
            val AvoidanceRadius = Assets.Enemy.ColliderRadius * 5 // between each other

            val DistanceWhenWatching = 150f
            val EnemyCountForStabbingDirectly = 1
        }
    }
}
