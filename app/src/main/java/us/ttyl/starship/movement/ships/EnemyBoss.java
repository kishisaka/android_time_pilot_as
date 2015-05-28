package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.LineEngine;
import us.ttyl.starship.movement.MovementEngine;

public class EnemyBoss extends LineEngine
{
	public EnemyBoss(int direction, int currentDirection, double currentX,
			double currentY, double currentSpeed, double maxSpeed,
			double acceleration, int turnMode, int name, MovementEngine origin,
			int endurance) {
		super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
				acceleration, turnMode, name, origin, endurance, GameState.sBossHitPoints);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCollision(MovementEngine engine2)
	{
		if (getWeaponName() != engine2.getWeaponName())
		{
			//on collision with player, player missile or player gun, kill boss, show explosion 
			if (engine2.getWeaponName() == Constants.PLAYER || engine2.getWeaponName() == Constants.MISSILE_PLAYER
					|| engine2.getWeaponName() == Constants.GUN_PLAYER)
			{
				// show explosion
				// remove enemy boss from list
				decrementHitPoints(1);
				GameState.sBossHitPoints = getHitpoints();

				GameState._playerScore = GameState._playerScore + 221;
				//show score
				MovementEngine textItem = new TextItem(getCurrentDirection(), getCurrentDirection(), getX(), getY(), "221");
				GameState._weaponList.add(textItem);
				if (checkDestroyed())
				{
					// reset boss hitpoints
					GameState.sBossHitPoints = Constants.BOSS_STARTING_HITPOINT;

					// play death sound
					AudioPlayer.playShipDeath();

					//release only one parachute per 200 ms
					long intervalParachute = System.currentTimeMillis() - GameState.sLastReleasedParachute;
					if (intervalParachute > Constants.PARACHUTE_RELEASE_INTERVAL) {
						MovementEngine parachute = new Parachute(270, 270
								, getX(), getY(), .2, 1, 1, 1, Constants.PARACHUTE
								, null, -1, 1);
						GameState._weaponList.add(parachute);
						GameState.sLastReleasedParachute = System.currentTimeMillis();
					}
				}
				// create particle explosion for shot down boss
				long intervalExplosion = System.currentTimeMillis() - GameState.sLastBossExplosion;
				if (intervalExplosion > Constants.BOSS_EXPLOSION_INTERVAL) {
					for (int particleCount = 0; particleCount < Constants.EXPLOSION_PARTICLE_COUNT; particleCount++) {
						int particleDirection = (int) (Math.random() * 360);
						int particleSpeed = (int) (Math.random() * 10);
						int particleEndurance = (int) (Math.random() * Constants.EXPLOSION_ENDURANCE);
						MovementEngine explosionParticle = new ExplosionParticle(particleDirection, particleDirection
								, getX(), getY(), particleSpeed, 1, 1, 1, Constants.EXPLOSION_PARTICLE
								, null, particleEndurance, 1);
						GameState._explosionParticleList.add(explosionParticle);
						GameState.sLastBossExplosion = System.currentTimeMillis();
					}
				}
			}
		}
	}
}
