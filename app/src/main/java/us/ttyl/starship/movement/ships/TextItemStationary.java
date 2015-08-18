package us.ttyl.starship.movement.ships;

import us.ttyl.starship.movement.LineEngine;
import us.ttyl.starship.movement.MovementEngine;

/**
 * Created by test on 7/7/2015.
 */
public class TextItemStationary extends LineEngine {
    protected TextItemStationary(int direction, int currentDirection, double currentX,
                                 double currentY, double currentSpeed, double maxSpeed,
                                 double acceleration, int turnMode, int name,
                                 MovementEngine origin, int endurance, int hitpoints) {
        super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
                acceleration, turnMode, name, origin, endurance, hitpoints);
    }
}
