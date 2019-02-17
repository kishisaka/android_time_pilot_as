package us.ttyl.starship.movement.ships;

import android.util.Log;

import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.core.Node;
import us.ttyl.starship.env.EnvBuilder;
import us.ttyl.starship.movement.LineEngine;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.pools.ParticlePool;

/**
 * The parachute pilot from enemy bosses, gives 10 missiles, pickup 4 pilots to get to next level
 * @author kurt ishisaka
 *
 */
public class Parachute extends LineEngine
{
	private static final String TAG = "LineEngine";

	public Parachute(int direction, int currentDirection, double currentX,
			double currentY, double currentSpeed, double maxSpeed,
			double acceleration, int turnMode, int name, MovementEngine origin,
			int endurance, int hitpoints) 
	{
		super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
				acceleration, turnMode, name, origin, endurance, hitpoints);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollision(MovementEngine engine2)
	{
		// player collects parachute, add 10 missiles to inventory, if player 
		// collects 4 parachutes, kill all enemies and move to next level
		long pickupInterval = System.currentTimeMillis() - GameState.sLastParachutePickupInterval;
		if (pickupInterval > Constants.PARACHUTE_PICKUP_INTERVAL
				&& engine2.getWeaponName() == Constants.PLAYER && engine2.getDestroyedFlag() == false) {
			GameState.sLastParachutePickupInterval = System.currentTimeMillis();

			//generate player options or give player missiles
			if ((int) (Math.random() * 100) > 40) {
				engine2.setMissileCount(engine2.getMissileCount() + 20);
			} else {
				EnvBuilder.generatePlayerOption(0, (int)(3 * GameState.sScreenDensity), (int)(50 * GameState.sScreenDensity));
				EnvBuilder.generatePlayerOption(120,(int)(3 * GameState.sScreenDensity), (int)(50 * GameState.sScreenDensity));
				EnvBuilder.generatePlayerOption(240,(int)(3 * GameState.sScreenDensity), (int)(50 * GameState.sScreenDensity));
			}

			decrementHitPoints(1);
			checkDestroyed();
			GameState._playerScore = GameState._playerScore + 1000;
			
			GameState.sParachutePickupCount = GameState.sParachutePickupCount + 1;
			
			// if we got 4 parachutes, remove all ships, change level! 
			if (GameState.sParachutePickupCount > 3) {
				AudioPlayer.playLevelChange();
				GameState._playerScore = GameState._playerScore + 10000;
				Thread levelWait = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							//notify the main loop not to build more enemies for 3500 ms
							GameState.mWaitTimeBetweenLevels = true;
							Thread.sleep(3500);
							GameState.mWaitTimeBetweenLevels = false;
						} catch (Exception e) {
							Log.e(TAG, e.getMessage(), e);
						}
					}
				});
				levelWait.start();

				GameState.sParachutePickupCount = 0;
				GameState.sCurrentLevel = GameState.sCurrentLevel + 1;
				if (GameState.sCurrentLevel > 4) {
					GameState.sCurrentLevel = 1;
					GameState.sWaveLevel = GameState.sWaveLevel + 1;
                    GameState.sStartTimeBoss = System.currentTimeMillis();
				}

				//destroy all enemy fighters
				for (int i = 1; i < GameState._weaponList.size(); i++) {
					MovementEngine ship = GameState._weaponList.get(i);
					if (ship.getWeaponName() == Constants.ENEMY_FIGHTER || ship.getWeaponName() == Constants.ENEMY_BOSS) {
						ship.decrementHitPoints(ship.getHitpoints());
						ship.checkDestroyed();

						// create particle explosion for shot down aircraft
						for (int particleCount = 0; particleCount < Constants.EXPLOSION_PARTICLE_COUNT; particleCount++) {
							int particleDirection = (int) (Math.random() * 360);
							int particleSpeed = (int) (Math.random() * 10);
							int particleEndurance = (int) (Math.random() * Constants.EXPLOSION_ENDURANCE);
							MovementEngine explosionParticle = ParticlePool.obtain(particleDirection, particleDirection
									, ship.getX(), ship.getY(), particleSpeed, 1, 1, 1, Constants.EXPLOSION_PARTICLE
									, null, particleEndurance, 1);
							Node<MovementEngine> node = new Node(explosionParticle);
							GameState._explosionParticleList.add(node);
						}
					}
				}

				// reset the show game level name flag
				GameState.sShownLevelName = false;
			}
			else
			{
				AudioPlayer.playParachutePickup();
			}
		}
	}
}
