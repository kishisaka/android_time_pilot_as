package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.LineEngine;

/**
 * Created by test on 5/7/2016.
 */
public class ExplosionBossAnimated extends LineEngine {
    public ExplosionBossAnimated(double currentX, double currentY, double speed, int currentDirection) {
        super(currentDirection, currentDirection, currentX, currentY, speed, speed,
                0, 0, Constants.ANIMATED_BOSS_EXPLOSION, null, GameState._explosionBossSprites.size() - 1, 1);
    }
}
