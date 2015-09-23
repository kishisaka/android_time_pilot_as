package us.ttyl.starship.movement.ships;

import android.util.Log;
import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.env.EnvBuilder;
import us.ttyl.starship.listener.GameStateListener;
import us.ttyl.starship.movement.FreeEngine;
import us.ttyl.starship.movement.MovementEngine;

public class PlayerFighter extends FreeEngine
{
	private boolean mWaiting = false;
	
	private GameStateListener mGameStateListener;
	private static final String TAG = "PlayerFighter";
	
	public PlayerFighter(int direction, int currentDirection, double currentX,
			double currentY, double currentSpeed, double desiredSpeed,
			double maxSpeed, double acceleration, int turnMode, int name,
			int endurance, GameStateListener gameStateListener) {
		super(direction, currentDirection, currentX, currentY, currentSpeed,
				desiredSpeed, maxSpeed, acceleration, turnMode, name, endurance);
		mGameStateListener = gameStateListener;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCollision(MovementEngine engine2)
	{
		//on collision with enemy, enemy missile or enemy gun, kill player fighter, show explosion
		if (getWeaponName() != engine2.getWeaponName() && mWaiting == false && GameState.sIsInvulnerable == false)
		{
			if (engine2.getWeaponName() == Constants.ENEMY_FIGHTER || engine2.getWeaponName() == Constants.ENEMY_BOSS 
					|| engine2.getWeaponName() == Constants.MISSILE_ENEMY || engine2.getWeaponName() == Constants.GUN_ENEMY
					&& getDestroyedFlag() == false)
			{
				long deathInterval = System.currentTimeMillis() - GameState.sLastDeath;
				if (deathInterval > Constants.DEATH_INTERVAL)
				{
					GameState.sLastDeath = System.currentTimeMillis();

					// remove player fighter from list
					decrementHitPoints(1);
					checkDestroyed();

					//remove player options and text items if any from weapon list
					for(int i = 0; i < GameState._weaponList.size(); i ++)
					{
						MovementEngine engine = GameState._weaponList.get(i);
						if (engine.getWeaponName() == Constants.PLAYER_OPTION || engine.getWeaponName() == Constants.TEXT)
						{
							try {
								GameState._weaponList.remove(i);
							}
							catch(Exception e)
							{
								// do nothing, maan loop might have done this already.
							}
						}
					}

					// play death sound
					AudioPlayer.playShipDeath();

					// create particle explosion for shot down aircraft
					// TODO move all explosion instances to utils.
					for (int particleCount = 0; particleCount < Constants.EXPLOSION_PARTICLE_COUNT; particleCount++) {
						int particleDirection = (int) (Math.random() * 360);
						int particleSpeed = (int) (Math.random() * 10);
						int particleEndurance = (int) (Math.random() * Constants.EXPLOSION_ENDURANCE);
						MovementEngine explosionParticle = new ExplosionParticle(particleDirection, particleDirection
								, getX(), getY(), particleSpeed, 1, 1, 1, Constants.EXPLOSION_PARTICLE
								, null, particleEndurance, 1);
						GameState._explosionParticleList.add(explosionParticle);
					}
					//pause the player gun sound
					try {
						AudioPlayer.pausePlayerGun();
					} catch (Exception e) {
						Log.e(TAG, "MainLoop.checkCollision() most likely sound does not exist", e);
					}

					if (getDestroyedFlag() == true) {
						// check if player ship was destroyed, wait for 2 seconds, then restart the game
						// allow player to see their ship explode
						Thread deadWait = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									mWaiting = true;
									// wait 2 seconds to show the explosion.
									Thread.sleep(2000);
									mGameStateListener.onPlayerDied();
									mWaiting = false;
								} catch (InterruptedException ie) {
									// ignore and continue!
								}
							}
						});
						deadWait.start();
					}
				}
			}
		}
	}
}
