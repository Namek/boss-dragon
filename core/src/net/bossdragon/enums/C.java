package net.bossdragon.enums;

import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

/**
 * Constants.
 */
public interface C {
    interface Map {
        float Width = 600;
        float Height = 440;
        float LogicalRadius = Width/2;

        /** Logical -> Graphical */
        float RescaleAxisY = Height / Width;

        Vector2[] SpawnFlyDirections = new Vector2[] {
            new Vector2(+1f, -1f).nor(),
            new Vector2(-1f, -1f).nor(),
            new Vector2(-1f, +1f).nor(),
            new Vector2(+1f, +1f).nor(),
        };

        // place those points around map circle
        // assume: map center is placed on (0, 0)
        Vector2[] SpawnLandPoints = new Vector2[] {
            SpawnFlyDirections[0].cpy().scl(-(LogicalRadius - Assets.Soldier.ColliderRadius)),
            SpawnFlyDirections[1].cpy().scl(-(LogicalRadius - Assets.Soldier.ColliderRadius)),
            SpawnFlyDirections[2].cpy().scl(-(LogicalRadius - Assets.Soldier.ColliderRadius)),
            SpawnFlyDirections[3].cpy().scl(-(LogicalRadius - Assets.Soldier.ColliderRadius)),
        };
    }

    interface Player {
        float MaxSpeed = 400f;
        float Acceleration = MaxSpeed * 12;
        float Friction = MaxSpeed * 10;

        float MaxSpeedOnSlide = MaxSpeed * 1.5f;
        float AccelerationOnSlide = Acceleration * 2f;

        float SlideDuration = 0.4f;
        float SlideCooldownDuration = 0.3f;
    }

    interface Fireball {
        float MaxSpeed = 600f;
        float Size = 60f;
    }

    interface Soldier {
        int MaxEnemyCount = 8;
        float SpawnCooldown = 1f;
        float StunCooldown = 3f;

        /** distance to spawn point */
        float SpawnFlyDistance = Map.LogicalRadius / 2;
        float MaxSpeed = Player.MaxSpeed/5;
        float Friction = Player.Friction;

        interface AI {
            /** Time between recalculations about who's going to attack */
            float StrategyCooldown = 1f;

            float PlayerCloseRangeRadius = Assets.Character.ColliderRadius + Assets.Soldier.ColliderRadius;
            float AvoidanceRadius = Assets.Soldier.ColliderRadius * 5; // between each other

            float DistanceWhenWatching = 150f;
            int EnemyCountForStabbingDirectly = 1;
        }

        float MaxSpeedWhenThrown = Player.MaxSpeed * 5;
        float AccelWhenThrown = MaxSpeedWhenThrown * 12;
    }
}
