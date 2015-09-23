package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.OrbitEngine;

/**
 * Created by kishisaka on 9/2/2015.
 */
public class PlayerOption extends OrbitEngine {
    public PlayerOption(MovementEngine origin, int name, int startingDegree, int radius, int speed) {
        super(origin, name, 15, startingDegree, radius, speed);
    }

    @Override
    public void onCollision(MovementEngine engine2) {
        if (engine2.getWeaponName() == Constants.ENEMY_BOSS
                || engine2.getWeaponName() == Constants.ENEMY_FIGHTER
                || engine2.getWeaponName() == Constants.MISSILE_ENEMY
                || engine2.getWeaponName() == Constants.GUN_ENEMY) {
            decrementHitPoints(1);
            checkDestroyed();
        }
    }
}
