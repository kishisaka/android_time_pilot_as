package us.ttyl.starship.core;

import android.util.Log;

import us.ttyl.starship.env.EnvBuilder;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.BossBullet;
import us.ttyl.starship.movement.ships.Bullet;
import us.ttyl.starship.movement.ships.ExplosionParticle;
import us.ttyl.starship.movement.ships.Missile;

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
	
	public MainLoop(float density)
	{		
		_density = density;
		// SpeedController controller = new SpeedController();
		// controller.start();
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
		long startTimeBoss = startTime;

		long startTimeBossBullet = startTime;
		long startTimeEnemyGun = startTime;
		long startTimeEnemyMissile = startTime;
		
		//start player gun sound, this will run till player dies
		if (GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER) && GameState._weaponList.get(0).getDestroyedFlag() == false)
		{
			AudioPlayer.resumePlayerGun();
		}
		
		//main game loop
		while(GameState.mIsRunning == true)
		{	
			long currentTime = System.currentTimeMillis();
			try
			{	
				//create clouds 
				int cloudCount = GameUtils.getTypeCount(Constants.CLOUD_BIG); 
				cloudCount = cloudCount + GameUtils.getTypeCount(Constants.CLOUD_SMALL); 
				long currentTimeClouds = currentTime;
				if (currentTimeClouds - startTimeClouds > 250 && cloudCount < 18)
				{
					startTimeClouds = currentTimeClouds;
					int random = (int)(Math.random() * 100);
					if (random > 50)
					{
						EnvBuilder.generateSmallClouds(GameState._weaponList.get(0).getX(), GameState._weaponList.get(0).getY(), GameState._weaponList.get(0).getCurrentDirection());
					}
					else
					{
						EnvBuilder.generateLargeCloud(GameState._weaponList.get(0).getX(), GameState._weaponList.get(0).getY(), GameState._weaponList.get(0).getCurrentDirection());
					}
				}

		    	//generate a enemy fighter.
		    	int enemyCount = GameUtils.getTypeCount(Constants.ENEMY_FIGHTER); 
		    	// System.out.println("enemyCount: " + enemyCount);
				if (GameState.mWaitTimeBetweenLevels == false && (currentTime - startTime) > 300 && enemyCount < 12)
				{
			    	EnvBuilder.generateEnemy(GameState._weaponList.get(0).getX()
			    			, GameState._weaponList.get(0).getY());
			    	startTime = currentTime;
				}
				
				//generate a boss ship
				if (GameState.mWaitTimeBetweenLevels == false && (currentTime - startTimeBoss) > 6000)
				{
					EnvBuilder.generateEnemyBoss(GameState._weaponList.get(0).getX()
					 		, GameState._weaponList.get(0).getY());
					startTimeBoss = currentTime;
				}
				
				// fire enemy guns constantly
				if (GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER) == true
						&& GameState.sFireEnemyGuns == true)
				{
					for(int i = 0; i < GameState._weaponList.size(); i ++)
					{
						// Log.d("MainLoop", "current time: "+ currentTime + " | startTimeBossBullet: " + startTimeBossBullet + " diff: " + (currentTime - startTimeBossBullet));
						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_BOSS
								&& (currentTime - startTimeBossBullet > Constants.ENEMY_BOSS_FIRE_INTERVAL))
						{
							Log.d("MainLoop", "fire boss");
							int targetTrack = (int)GameUtils.getTargetTrack(GameState._weaponList.get(i), GameState._weaponList.get(0));
							MovementEngine bossBullet = new BossBullet(targetTrack, targetTrack
									, (int)GameState._weaponList.get(i).getX()
									, (int)GameState._weaponList.get(i).getY()
									, 3, 3, 1, 1
									, Constants.GUN_ENEMY, GameState._weaponList.get(i), 50, 1);
							GameState._weaponList.add(bossBullet);
							if (GameState._muted == false)
							{
								AudioPlayer.playEnemyGun();
							}
							startTimeBossBullet = currentTime;
						}

						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_FIGHTER
								&& GameUtils.getRange(GameState._weaponList.get(i), GameState._weaponList.get(0)) > 100
								&& GameUtils.fireWeapon()
								&& (currentTime - startTimeEnemyGun  > Constants.ENEMY_GUN_FIRE_INTERVAL))
						{
							Log.d("MainLoop", "fire enemy gun");
							// get player track
							int targetTrack = (int) GameUtils.getTargetTrack(GameState._weaponList.get(i), GameState._weaponList.get(0));
							MovementEngine bullet = new Bullet(targetTrack, targetTrack
									, (int) GameState._weaponList.get(i).getX()
									, (int) GameState._weaponList.get(i).getY()
									, 3, 3, 1, 1
									, Constants.GUN_ENEMY, GameState._weaponList.get(i), 100, 1);
							GameState._weaponList.add(bullet);
							if (GameState._muted == false)
							{
								AudioPlayer.playEnemyGun();
							}
							startTimeEnemyGun = currentTime;
						}

						if (GameState._weaponList.get(i).getWeaponName() == Constants.ENEMY_FIGHTER
								&& GameUtils.getRange(GameState._weaponList.get(i), GameState._weaponList.get(0)) > 100
								&& GameUtils.fireWeapon()
								&& (currentTime - startTimeEnemyMissile  > Constants.ENEMY_MISSILE_FIRE_INTERVAL))
						{
							Log.d("MainLoop", "fire enemy missile");
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
				// System.out.println("gunModifier:" + _gunModifier);
				if (GameState._weaponList.get(0).getDestroyedFlag() == false && (GameState._weaponList.get(0).getWeaponName()==(Constants.PLAYER)))
				{
					long currentTimeGun = currentTime;
					if (currentTimeGun - startTimeGun > 30)
					{
						startTimeGun = currentTimeGun;
						MovementEngine bullet = new Bullet(GameState._weaponList.get(0).getCurrentDirection() + _gunModifier
								, GameState._weaponList.get(0).getCurrentDirection() + _gunModifier
								, (int)GameState._weaponList.get(0).getX()
								, (int)GameState._weaponList.get(0).getY(),10, 10, 1, 1, Constants.GUN_PLAYER, GameState._weaponList.get(0), 30, 1);
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
		    			MovementEngine missileSmokeTrail = new ExplosionParticle(ship.getCurrentDirection(), ship.getCurrentDirection()
								, (int)ship.getX()
								, (int)ship.getY(),0, 0, 0, 0, Constants.MISSILE_SMOKE, null, Constants.SMOKE_ENDURANCE_MISSLE, 1);
		    			GameState._explosionParticleList.add(missileSmokeTrail);
					}

		    		// make boss damage smoke trail
		    		if (ship.getWeaponName()==(Constants.ENEMY_BOSS))
		    		{
			    		if (ship.getHitpoints() < 7 )
			    		{
							int particleDirection = (int)(Math.random() * 360);
							int particleSpeed = (int)(Math.random() * 10);
							int particleEndurance = (int)(Math.random() * Constants.SMOKE_ENDURANCE_BOSS);
							MovementEngine explosionParticle = new ExplosionParticle(particleDirection, particleDirection
									, ship.getX(), ship.getY(), particleSpeed, 1, 1, 1, Constants.BOSS_SMOKE
									, null, particleEndurance, 1);
							GameState._explosionParticleList.add(explosionParticle);
			    		}
		    		}
		    		ship.run();

		    		//TODO O(n^2) function unfortunately, any other way to make this faster? 
		    		checkCollisions(ship);
		    	}

				//run the explosions particles also!
				for(int i = 0; i < GameState._explosionParticleList.size(); i ++)
				{
					MovementEngine ship = GameState._explosionParticleList.get(i);
					ship.run();
				}

				//run the clouds (large)
				for(int i = 0; i < GameState._cloudListLarge.size(); i ++)
				{
					MovementEngine ship = GameState._cloudListLarge.get(i);
					ship.run();
				}

				//run the clouds (small)
				for(int i = 0; i < GameState._cloudListSmall.size(); i ++)
				{
					MovementEngine ship = GameState._cloudListSmall.get(i);
					ship.run();
				}
				
				//check destroyed and remove from weapon list if so and remove all objects that are over 700 units away from the player
				for(int i = 1; i < GameState._weaponList.size(); i ++)
		    	{
					try
					{
						if ((i > 0 && GameUtils.getRange(GameState._weaponList.get(0), GameState._weaponList.get(i)) >(GameState.sObjectCreationRadius + 100))
								|| (GameState._weaponList.get(i).getDestroyedFlag() == true))
						{
							GameState._weaponList.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						// ignore and continue
					}
		    	}

				//check destroyed and remove from explosion list if so and remove if over 700 units away from the player
				for(int i = 0; i < GameState._explosionParticleList.size(); i ++)
				{
					try
					{
						if (( GameUtils.getRange(GameState._weaponList.get(0), GameState._explosionParticleList.get(i)) > (GameState.sObjectCreationRadius + 100))
								|| (GameState._explosionParticleList.get(i).getDestroyedFlag() == true))
						{
							GameState._explosionParticleList.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						// ignore and continue
					}
				}

				//check cloud(large) list and remove if over 700 units away from the player
				for(int i = 0; i < GameState._cloudListLarge.size(); i ++)
				{
					try
					{
						if (GameUtils.getRange(GameState._weaponList.get(0), GameState._cloudListLarge.get(i)) > (GameState.sObjectCreationRadius + 100))
						{
							GameState._cloudListLarge.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						// ignore and continue
					}
				}

				//check cloud(small) list if so and remove if 700 units away from the player
				for(int i = 0; i < GameState._cloudListSmall.size(); i ++)
				{
					try
					{
						if (GameUtils.getRange(GameState._weaponList.get(0), GameState._cloudListSmall.get(i)) > (GameState.sObjectCreationRadius + 100))
						{
							GameState._cloudListSmall.remove(i);
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
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

				//calculate the framerate
				if (singleLoopTime < 16)
				{
					singleLoopTime = 16;
				}

				//calculate sleep time based on 60fps
				if (singleLoopTime > 1) {
					GameState.sFramerate = (int) (1000 / singleLoopTime);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}       
	
	/**
	 * move player gun up/down to get a limited firing arc spread of a couple of deg up and down. 
	 */
	private void gunModifier()
	{		
		if (_gunModifier == 0)
		{
			_gunModifier = (int)(_gunModifierSwivel * GameState._density);
		}
		else
		{
			if (_gunModifier == _gunModifierSwivel * GameState._density)
			{
				_gunModifier = (int)(-1 * (_gunModifierSwivel* GameState._density));
			}
			else
			{
				if (_gunModifier == -1 * (_gunModifierSwivel* GameState._density))
				{
					_gunModifier = 0;
				}
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
								&& ship.getDestroyedFlag() == false)
						{
							int diffX = Math.abs((int)(currentShip.getX() - ship.getX())); 
							int diffY = Math.abs((int)(currentShip.getY() - ship.getY()));

							if (ship.getWeaponName() == Constants.ENEMY_BOSS || currentShip.getWeaponName() == Constants.ENEMY_BOSS)
							{
								// boss ship collision on some side, use 30 X 10
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
