package net.bossdragon.component;

import com.artemis.Component;
import net.bossdragon.util.ActionTimer;

/**
 *
 */
public class Player extends Component {
    public final ActionTimer slide = new ActionTimer(C.Player.SlideDuration);
    public final ActionTimer slideCooldown = new ActionTimer(C.Player.SlideCooldownDuration);

    public int slideDirX = 0;
    public int slideDirY = 0;

    public int carriedSoldierId = -1;

    public boolean isSliding() {
        return slide.isRunning();
    }

    public boolean canStartSlide() {
        return !isSliding() && !slideCooldown.isRunning();
    }

    public boolean isCarryingAnySoldier() {
        return carriedSoldierId >= 0;
    }
}
