package us.ttyl.starship.core;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;

import us.ttyl.starship.env.EnvBuilder;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.BossBullet;
import us.ttyl.starship.movement.ships.Bullet;
import us.ttyl.starship.movement.ships.ExplosionParticle;
import us.ttyl.starship.movement.ships.Missile;
import us.ttyl.starship.pools.BulletPool;
import us.ttyl.starship.pools.ParticlePool;

/**
 * The main game loop, game states is updated here. State is updated to the 
 * GameState holder which AsteroidView will use to render the player view. 
 * 
 * @author kurt ishisaka
 *
 */
public class MainLoop extends Thread
{
	private int _gunModifier = 0;
	private int _gunModifierSwivel = 3;
	private float _density;
	private Context mContext;

	public MainLoop(float density, Context context)
	{
		Log.d("kurt_test", "main loop stared");
		mContext = context;
		_density = density;
		initGame();
		start();
	}
	
	/**
	 * the main game loop! 
	 */
	@Override
	public void run()
	{
		long startTime = System.currentTimeMillis();
		long startTimeGun = startTime;
		long startTimeClouds = startTime;

		long startTimeBossBullet = startTime;
		long startTimeEnemyGun = startTime;
		long startTimeEnemyMissile = startTime;
		
		//start player gun sound, this will run till player dies
		if (GameState._weaponList.size() > 0 && GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER) && GameState._weaponList.get(0).getDestroyedFlag() == false)
		{
			AudioPlayer.resumePlayerGun();
		}
		Log.d("kurt_test", "starting main loop");
		//main game loop
		while(GameState.mIsRunning == true)
		{
			long currentTime = System.currentTimeMillis();
			try
			{
				//create clouds 
				int cloudCount = GameUtils.getTypeCount(Constants.CLOUD_BIG, GameState._cloudListLarge);
				cloudCount = cloudCount + GameUtils.getTypeCount(Constants.CLOUD_SMALL, GameState._cloudListSmall);
				long currentTimeClouds = currentTime;
				if (currentTimeClouds - startTimeClouds > 10 && cloudCount < GameState.MAX_CLOUDS)
				{
					startTimeClouds = currentTimeClouds;
					int random = (int)(Math.random() * 100);
					if (random > 30)
					{
						EnvBuilder.generateSmallClouds(GameState._weaponList.get(0).getX(), GameState._weaponList.get(0).getY(), GameState._weaponList.get(0).getCurrentDirection(), _density);
					}
					else
					{
						EnvBuilder.generateLargeCloud(GameState._weaponList.get(0).getX(), GameState._weaponList.get(0).getY(), GameState._weaponList.get(0).getCurrentDirection(), _density);
					}
				}

				if (GameState.sShownLevelName == false && GameState.mWaitTimeBetweenLevels == false)
				{
					if (GameState._weaponList.isEmpty() == false && GameState._weaponList.get(0).getWeaponName() == Constants.PLAYER) {
						String title = GameUtils.getGameLevelName(mContext.getResources());
						EnvBuilder.generateTextLine(title,
								2, 300, GameState._weaponList.get(0).getX(),
								GameState._weaponList.get(0).getY() + 200, 180);
						GameState.sShownLevelName = true;
					}
				}
				//generate an enemy follow fighter.
				int enemyCount = GameUtils.getTypeCount(Constants.ENEMY_FIGHTER, GameState._weaponList);
				if (GameState.mWaitTimeBetweenLevels == false && (currentTime - startTime) > 30 && enemyCount < GameUtils.getFighterCount())
				{
					EnvBuilder.generateEnemy(GameState._weaponList.get(0).getX()
							, GameState._weaponList.get(0).getY(), true, _density);
					startTime = currentTime;
				}
				
				//generate a boss ship
                int bossCount = GameUtils.getTypeCount(Constants.ENEMY_BOSS, GameState._weaponList);
				if (GameState.mWaitTimeBetweenLevels == false && (currentTime - GameState.sStartTimeBoss) > 6000
                        && bossCount < 1)
				{
					EnvBuilder.generateEnemyBoss(GameState._weaponList.get(0).getX()
					 		, GameState._weaponList.get(0).getY(), _density);
                    GameState.sStartTimeBoss = currentTime;
				}
				
				// fire enemy guns constantly
				if (GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER) == true
						&& GameState.sIsFireEnemyGuns == true && GameState.sFramerate < Constants.FRAME_RATE_MIN)
				{
					for(int i = 0; i < GameState._weaponList.size(); i ++)
					{
						// get range to target (from current ship to player)
						int rangeToTarget = GameUtils.getRange(GameState._weaponList.get(i), GameState._weaponList.get(0));

						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_BOSS
								&& rangeToTarget < Constants.ENEMY_BOSS_FIRE_RADIUS
								&& (currentTime - startTimeBossBullet > GameUtils.getEnemyGunFireRate()))
						{
							int targetTrack = (int)GameUtils.getTargetTrack(GameState._weaponList.get(i), GameState._weaponList.get(0));
							if (GameState.sWaveLevel > 1)
							{
								// boss bullet
								MovementEngine bossBullet = new BossBullet(targetTrack, targetTrack
										, (int) GameState._weaponList.get(i).getX()
										, (int) GameState._weaponList.get(i).getY()
										, 3, 3, 1, 1
										, Constants.GUN_ENEMY, GameState._weaponList.get(i), 50, 1);
								GameState._weaponList.add(bossBullet);
								if (GameState._muted == false)
								{
									AudioPlayer.playEnemyGun();
								}
								startTimeBossBullet = currentTime;
							}
							else if (GameState.sWaveLevel == 1)
							{
								// boss big gun
								MovementEngine bullet = BulletPool.obtain(targetTrack, targetTrack
										, (int) GameState._weaponList.get(i).getX()
										, (int) GameState._weaponList.get(i).getY()
										, 3, 3, 1, 1
										, Constants.GUN_ENEMY, GameState._weaponList.get(i), 50);
								GameState._weaponList.add(bullet);
								if (GameState._muted == false)
								{
									AudioPlayer.playEnemyGun();
								}
								startTimeBossBullet = currentTime;
							}
						}

						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_FIGHTER
								&& rangeToTarget > 150 && rangeToTarget < Constants.ENEMY_FIGHTER_FiRE_RADIUS
								&& GameUtils.shouldFireWeapon()
								&& (currentTime - startTimeEnemyGun  > GameUtils.getEnemyGunFireRate()))
						{

							// get player track
							int targetTrack = (int) GameUtils.getTargetTrack(GameState._weaponList.get(i), GameState._weaponList.get(0));
							MovementEngine bullet = BulletPool.obtain(targetTrack, targetTrack
									, (int) GameState._weaponList.get(i).getX()
									, (int) GameState._weaponList.get(i).getY()
									, 3, 3, 1, 1
									, Constants.GUN_ENEMY, GameState._weaponList.get(i), 100);
							GameState._weaponList.add(bullet);
							if (GameState._muted == false)
							{
								AudioPlayer.playEnemyGun();
							}
							startTimeEnemyGun = currentTime;
						}

						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_FIGHTER
								&& rangeToTarget > 150 && rangeToTarget < Constants.ENEMY_FIGHTER_FiRE_RADIUS
								&& GameUtils.shouldFireWeapon()
								&& (currentTime - startTimeEnemyMissile  > GameUtils.getEnemyMissileFireRate()))
						{
							// get player track
							int targetTrack = (int)GameUtils.getTargetTrack(GameState._weaponList.get(i), GameState._weaponList.get(0));

							MovementEngine missile = new Missile(targetTrack, targetTrack
									, (int)GameState._weaponList.get(i).getX()
									, (int)GameState._weaponList.get(i).getY()
									, GameState._weaponList.get(i).getCurrentSpeed(), 3, 1, 1
									, Constants.MISSILE_ENEMY, GameState._weaponList.get(0), GameState._weaponList.get(i), 200);
							GameState._weaponList.add(missile);
							if (GameState._muted == false)
							{
								AudioPlayer.playMissileSound();
							}
							startTimeEnemyMissile = currentTime;
						}
					}
				}
				
				// fire gun constantly
				if (GameState._weaponList.get(0).getDestroyedFlag() == false && (GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER)))
				{
					long currentTimeGun = currentTime;
					if (currentTimeGun - startTimeGun > 25)
					{
						int gunDirection = GameState._weaponList.get(0).getCurrentDirection() + _gunModifier;
						if (gunDirection > 360) {
							gunDirection = gunDirection - 360;
						}
						if (gunDirection < 0) {
							gunDirection = gunDirection + 360;
						}
						startTimeGun = currentTimeGun;
						MovementEngine bullet =  BulletPool.obtain(gunDirection, gunDirection
								, (int)GameState._weaponList.get(0).getX()
								, (int)GameState._weaponList.get(0).getY(),10, 10, 1, 1,
								Constants.GUN_PLAYER, GameState._weaponList.get(0), 30);
						GameState._weaponList.add(bullet);
						gunModifier();
					}
				}

				// move the ships around, check for collisions.
				for(int i = 0; i < GameState._weaponList.size(); i ++)
		    	{
		    		MovementEngine ship = GameState._weaponList.get(i);
		    		if(ship.getWeaponName()==(Constants.MISSILE_PLAYER) || ship.getWeaponName()==(Constants.MISSILE_ENEMY))
					{
		    			// make smoke trail
		    			MovementEngine missileSmokeTrail = ParticlePool.obtain(ship.getCurrentDirection(), ship.getCurrentDirection()
								, (int)ship.getX()
								, (int)ship.getY(),0, 0, 0, 0, Constants.MISSILE_SMOKE, null, Constants.SMOKE_ENDURANCE_MISSLE, 1);
		    			Node node = new Node(missileSmokeTrail);
		    			GameState._explosionParticleList.add(node);
					}

		    		// make boss damage smoke trail
		    		if (ship.getWeaponName()==(Constants.ENEMY_BOSS))
		    		{
			    		if (ship.getHitpoints() < 7 )
			    		{
							int particleDirection = (int)(Math.random() * 360);
							int particleSpeed = (int)(Math.random() * 10);
							int particleEndurance = (int)(Math.random() * Constants.SMOKE_ENDURANCE_BOSS);
							MovementEngine explosionParticle = ParticlePool.obtain(particleDirection, particleDirection
									, ship.getX(), ship.getY(), particleSpeed, 1, 1, 1, Constants.BOSS_SMOKE
									, null, particleEndurance, 1);
							Node node = new Node(explosionParticle);
							GameState._explosionParticleList.add(node);
			    		}
		    		}
		    		ship.run(1);

		    		//TODO O(n^2) function unfortunately, any other way to make this faster? 
		    		checkCollisions(ship);
		    	}

				//run the explosions particles also!
				Node<MovementEngine> head = GameState._explosionParticleList.getHead();
				while (head != null) {
					MovementEngine ship = head.getValue();
					ship.run(1);
					head = head.next();
				}

				//run the clouds (large)
				for(int i = 0; i < GameState._cloudListLarge.size(); i ++)
				{
					MovementEngine ship = GameState._cloudListLarge.get(i);
					ship.run(2);
				}

				//run the clouds (small)
				for(int i = 0; i < GameState._cloudListSmall.size(); i ++)
				{
					MovementEngine ship = GameState._cloudListSmall.get(i);
					ship.run(1);
				}
				
				//check destroyed and remove from weapon list if so and remove all objects that are over screen radius units away from the player
				for(int i = 1; i < GameState._weaponList.size(); i ++)
		    	{
					try
					{
						if ((i > 0 && GameUtils.getRange(GameState._weaponList.get(0), GameState._weaponList.get(i)) >((GameState.sObjectCreationRadius * _density) + 100 * _density))
								|| (GameState._weaponList.get(i).getDestroyedFlag() == true))
						{
							MovementEngine engine = GameState._weaponList.get(i);
							GameState._weaponList.remove(i);
							if (engine != null && engine instanceof Bullet) {
								BulletPool.recycle((Bullet) engine);
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						e.printStackTrace();
						// ignore and continue
					}
		    	}

				//check destroyed and remove from explosion list if so and remove if over screen radius units away from the player
				Node <MovementEngine> explosionStart = GameState._explosionParticleList.getHead();
				while (explosionStart != null) {
					MovementEngine me = explosionStart.getValue();
					Node <MovementEngine> temp = explosionStart;
					explosionStart = explosionStart.next();
					try
					{
						if (( GameUtils.getRange(GameState._weaponList.get(0), me) > ((GameState.sObjectCreationRadius)+ 100 * _density))
								|| (me.getDestroyedFlag() == true))
						{
							GameState._explosionParticleList.remove(temp);
							if (me != null && me instanceof ExplosionParticle) {
								if (me != null ) {
									ParticlePool.recycle((ExplosionParticle) me);
								}
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
					}
				}

				//check cloud(large) list and remove if over some units away from the player
				for(int i = 0; i < GameState._cloudListLarge.size(); i ++)
				{
					try
					{
						if (GameUtils.getRange(GameState._weaponList.get(0), GameState._cloudListLarge.get(i)) > (GameState.sObjectCreationRadius + Constants.LARGE_CLOUD_OFFSET * _density))
						{
							GameState._cloudListLarge.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						e.printStackTrace();
						// ignore and continue
					}
				}

				//check cloud(small) list if so and remove if over some units away from the player
				for(int i = 0; i < GameState._cloudListSmall.size(); i ++)
				{
					try
					{
						if (GameUtils.getRange(GameState._weaponList.get(0), GameState._cloudListSmall.get(i)) > (GameState.sObjectCreationRadius + Constants.SMALL_CLOUD_OFFSET * _density))
						{
							GameState._cloudListSmall.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						e.printStackTrace();
						// ignore and continue
					}
				}

				// get sleep time
				long singleLoopTime = System.currentTimeMillis() - currentTime;

				//sleep if needed (over 60fps)
				if (singleLoopTime < 16)
				{
					sleep(16 - singleLoopTime);
				}

				//calculate sleep time based on 60fps
				GameState.sFramerate = (int) singleLoopTime;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		Log.d("kurt_test", "main loop ended, shutting down");
		GameState.clearAll();
	}       
	
	/**
	 * move player gun up/down to get a limited firing arc spread of a couple of deg up and down. 
	 */
	private void gunModifier()
	{
		if (_gunModifier == 0)
		{
			_gunModifier = (int)(-1 * _gunModifierSwivel * GameState._density);

		}
		else
		{
			if (_gunModifier < 0)
			{
				_gunModifier = (int)(_gunModifierSwivel * GameState._density);
			}
			else
			{
				_gunModifier = 0;
			}
		}
	}

	/**
	 * weapon collision check, if a weapon is within 10 x 10 unit box  of any other units
	 * let the 2 weapons that have collided figure out what to do (call each weapons's 
	 * onCollision() method) , ignore clouds, smoke trails, etc. 
	 * @param currentShip
	 */
	private void checkCollisions(MovementEngine currentShip)
	{
		if (currentShip.getWeaponName() == Constants.PLAYER 
				|| currentShip.getWeaponName() == Constants.ENEMY_FIGHTER
				|| currentShip.getWeaponName() == Constants.ENEMY_BOSS
				|| currentShip.getWeaponName() == Constants.MISSILE_PLAYER
				|| currentShip.getWeaponName() == Constants.MISSILE_ENEMY
				|| currentShip.getWeaponName() == Constants.GUN_ENEMY
				|| currentShip.getWeaponName() == Constants.GUN_PLAYER
				|| currentShip.getWeaponName() == Constants.PARACHUTE
				|| currentShip.getWeaponName() == Constants.TEXT
				|| currentShip.getWeaponName() == Constants.PLAYER_OPTION
				&& currentShip.getDestroyedFlag() == false)
		{
			for(int i = 0; i < GameState._weaponList.size(); i ++)
			{
				if (i < GameState._weaponList.size())
				{
					try
					{
						MovementEngine ship = GameState._weaponList.get(i);
						if (ship.getWeaponName() == Constants.PLAYER 
								|| ship.getWeaponName() == Constants.ENEMY_FIGHTER
								|| ship.getWeaponName() == Constants.ENEMY_BOSS
								|| ship.getWeaponName() == Constants.MISSILE_PLAYER
								|| ship.getWeaponName() == Constants.MISSILE_ENEMY
								|| ship.getWeaponName() == Constants.GUN_ENEMY
								|| ship.getWeaponName() == Constants.GUN_PLAYER
								|| ship.getWeaponName() == Constants.PARACHUTE
								|| ship.getWeaponName() == Constants.TEXT
								|| ship.getWeaponName() == Constants.PLAYER_OPTION
								&& ship.getDestroyedFlag() == false)
						{
							int diffX = Math.abs((int)(currentShip.getX() - ship.getX())); 
							int diffY = Math.abs((int)(currentShip.getY() - ship.getY()));

							if (ship.getWeaponName() == Constants.ENEMY_BOSS || currentShip.getWeaponName() == Constants.ENEMY_BOSS)
							{
								// boss ship collision on some side, use 30 X 20
								if (diffX <= (30 * GameState._density) && diffY <= (20 * GameState._density))
								{
									currentShip.onCollision(ship);
									ship.onCollision(currentShip);
								}
							}
							else
							{
								// anything else, use 10 x 10 bounding box
								if (diffX <= (10 * GameState._density) && diffY <= (10 * GameState._density))
								{
									currentShip.onCollision(ship);
									ship.onCollision(currentShip);
								}
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						e.printStackTrace();
						// ignore and continue
					}
				}
			}
		}
	}
	
	/**
	 * initialize the game! 
	 */
	public void initGame()
	{
		GameState.mIsRunning = true;
		GameState._density = _density;
	}
}
