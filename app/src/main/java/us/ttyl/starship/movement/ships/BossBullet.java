package us.ttyl.starship.movement.ships;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.movement.LineEngine;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.pools.BulletPool;

public class BossBullet extends LineEngine
{
	public BossBullet(int direction, int currentDirection, double currentX,
			double currentY, double currentSpeed, double maxSpeed,
			double acceleration, int turnMode, int name, MovementEngine origin,
			int endurance, int hitpoints) {
		super(direction, currentDirection, currentX, currentY, currentSpeed, maxSpeed,
				acceleration, turnMode, name, origin, endurance, hitpoints);
	}
	
	@Override
	public void updateFuelUsage() 
	{
		if (_endurance == 1)
		{
			for(int particleCount = 0; particleCount < 15; particleCount ++)
			{
				int particleDirection = (int)(Math.random() * 360);
				int particleSpeed = (int)(Math.random() * 10);
				int particleEndurance = (int)(Math.random() * 100);
				MovementEngine bullet =  BulletPool.obtain(particleDirection, particleDirection
						, getX(), getY(), particleSpeed, 1, 1, 1, Constants.GUN_ENEMY
						, this.getOrigin(), particleEndurance);
				GameState._weaponList.add(bullet);
			}
			_endurance = 0;
		}
		if (_endurance > 0)
		{
			_endurance = _endurance - 1;
		}
	}
	
	@Override
	public void onCollision(MovementEngine engine2)
	{
		if (getOrigin().getWeaponName() != engine2.getWeaponName())
		{
			if (engine2.getWeaponName() == Constants.ENEMY_BOSS
					|| engine2.getWeaponName() == Constants.ENEMY_FIGHTER
					|| engine2.getWeaponName() == Constants.MISSILE_PLAYER
					|| engine2.getWeaponName() == Constants.MISSILE_ENEMY
					|| engine2.getWeaponName() == Constants.PLAYER_OPTION)
			{				
				if (engine2.getWeaponName() == Constants.ENEMY_BOSS || engine2.getWeaponName() == Constants.ENEMY_FIGHTER)
				{
					decrementHitPoints(1);
					checkDestroyed();
				}
				else if (engine2.getWeaponName() == Constants.MISSILE_ENEMY && getWeaponName() == Constants.GUN_PLAYER)
				{
					decrementHitPoints(1);
					checkDestroyed();
				}
				else if (engine2.getWeaponName() == Constants.MISSILE_PLAYER && getWeaponName() == Constants.GUN_ENEMY)
				{
					decrementHitPoints(1);
					checkDestroyed();
				}
				else if (engine2.getWeaponName() == Constants.PLAYER_OPTION && (getWeaponName() == Constants.MISSILE_ENEMY || getWeaponName() == Constants.GUN_ENEMY))
				{
					decrementHitPoints(1);
					checkDestroyed();
				}
			}
		}
	}
}
