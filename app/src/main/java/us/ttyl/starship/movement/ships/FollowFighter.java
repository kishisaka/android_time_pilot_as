package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.FollowEngine;
import us.ttyl.starship.movement.MovementEngine;

/**
 * Created by kishisak on 6/9/15.
 */
public class FollowFighter extends FollowEngine
{
    public FollowFighter(int direction, int currentDirection,
                        double currentX, double currentY, double currentSpeed,
                        double maxSpeed, double acceleration, double turnMode, int name,
                        int endurance) {
        super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
                acceleration, 1, name, GameState._weaponList.get(0),null, -1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCollision(MovementEngine engine2) {
        if (getWeaponName() != engine2.getWeaponName()) {
            //on collision with player, player missile or player gun, kill fighter, show explosion
            if (engine2.getWeaponName() == Constants.PLAYER || engine2.getWeaponName() == Constants.MISSILE_PLAYER
                    || engine2.getWeaponName() == Constants.GUN_PLAYER) {
                // show explosion
                // remove enemy fighter from list
                decrementHitPoints(1);
                checkDestroyed();
                GameState._playerScore = GameState._playerScore + 200;
                MovementEngine textItem = new TextItem(0, 0, getX(), getY(), "200");
                GameState._weaponList.add(textItem);

                // play death sound
                AudioPlayer.playShipDeath();
                // create particle explosion for shot down aircraft
                for (int particleCount = 0; particleCount < Constants.EXPLOSION_PARTICLE_COUNT; particleCount++) {
                    int particleDirection = (int) (Math.random() * 360);
                    int particleSpeed = (int) (Math.random() * 10);
                    int particleEndurance = (int) (Math.random() * Constants.EXPLOSION_ENDURANCE);
                    MovementEngine explosionParticle = new ExplosionParticle(particleDirection, particleDirection
                            , getX(), getY(), particleSpeed, 1, 1, 1, Constants.EXPLOSION_PARTICLE
                            , null, particleEndurance, 1);
                    GameState._explosionParticleList.add(explosionParticle);
                }
            }
        }
    }
}