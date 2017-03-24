package net.bossdragon.system.view;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import net.bossdragon.component.Player;
import net.bossdragon.component.base.Transform;
import net.bossdragon.component.base.Velocity;
import net.bossdragon.component.render.anim.KeyFrameAnimations;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

import static net.bossdragon.enums.Animations.StickMan.*;

/**
 * Defines animation for player character on each frame.
 */
public class PlayerCharacterAnimSystem extends EntityProcessingSystem {
    M<KeyFrameAnimations> mKeyFramedAnim;
    M<Velocity> mVelocity;
    M<Player> mPlayer;
    M<Transform> mTransform;

    public PlayerCharacterAnimSystem() {
        super(Aspect.all(
            KeyFrameAnimations.class,
            Velocity.class,
            Transform.class,
            Player.class
        ));
    }

    @Override
    protected void process(Entity e) {
        Velocity vel = mVelocity.get(e);
        KeyFrameAnimations anim = mKeyFramedAnim.get(e);
        Player player = mPlayer.get(e);
        Transform transform = mTransform.get(e);

        String animName = IDLE;

        if (vel.isMoving()) {
            if (vel.velocity.x > 0)
                animName = WALK_RIGHT;
            else if (vel.velocity.x < 0)
                animName = WALK_LEFT;
            else if (vel.velocity.y > 0)
                animName = WALK_RIGHT;
            else if (vel.velocity.y < 0)
                animName = WALK_LEFT;
        }

        if (player.isSliding()) {
            animName = JUMP;

            int dirX = player.slideDirX;
            int dirY = player.slideDirY;

            transform.rotation =
                dirY == 1 ? (180f - dirX * 45f)
              : (dirY == -1 ? dirX * 45f : 90f * dirX);
        }
        else {
            transform.rotation = 0f;
        }

        if (anim.setAnimation(animName)) {
            anim.stateTime = 0f;
        }
    }
}
