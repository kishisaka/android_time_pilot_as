package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.movement.MovementEngine;

/**
 * Created by test on 1/20/2018.
 */

public class Fireball extends Bullet{
    public Fireball(int direction,
                    int currentDirection,
                    double currentX,
                    double currentY,
                    double currentSpeed,
                    double maxSpeed,
                    double acceleration,
                    int turnMode,
                    MovementEngine origin,
                    int hitpoints) {
        super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
                acceleration, turnMode, Constants.FIREBALL, origin, 12, hitpoints);
    }
}
