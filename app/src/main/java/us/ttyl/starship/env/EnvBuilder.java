package us.ttyl.starship.env;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.core.GameUtils;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.Cloud;
import us.ttyl.starship.movement.ships.EnemyBoss;
import us.ttyl.starship.movement.ships.EnemyFighter;
import us.ttyl.starship.movement.ships.FollowFighter;

/**
 * weapon builder
 * TODO add missile and explosion builders here as well
 * @author kurt ishisaka
 *
 */
public class EnvBuilder 
{
	/**
	 * generate enemy fighter
	 * @param fighterStartPositionX
	 * @param fighterStartPositionY
	 * @param turnmode
	 * @param speed
	 */
	private static void generateShip(double fighterStartPositionX, double fighterStartPositionY, int turnmode, float speed, boolean follow) {
		if (follow == false) {
			MovementEngine circleEngine = new EnemyFighter(0, 0, fighterStartPositionX, fighterStartPositionY, speed
					, speed, speed, turnmode, (Constants.ENEMY_FIGHTER), -1);
			GameState._weaponList.add(circleEngine);
		}
		else{
			MovementEngine followEngine = new FollowFighter(0, 0, fighterStartPositionX, fighterStartPositionY, speed
					, speed, speed, turnmode, (Constants.ENEMY_FIGHTER), -1);
			GameState._weaponList.add(followEngine);
		}
	}
	
	/**
	 * generate enemy boss
	 * @param playerPositionX
	 * @param playerPositionY
	 * @param turnmode
	 * @param speed
	 */
	private static void generateShipBoss(double playerPositionX, double playerPositionY, int turnmode, float speed)
	{
		//determine direction of boss
		int direction = 180;
		int xOffset = GameState.sObjectCreationRadius;
		if (Math.random() * 100 > 50)
		{
			direction = 0;
			xOffset = -GameState.sObjectCreationRadius;
		}

		if (GameUtils.getTypeCount(Constants.ENEMY_BOSS, GameState._weaponList) <= 0)
		{
			// generate the boss
			GameState._weaponList.add(new EnemyBoss(direction, direction, playerPositionX + xOffset
					, playerPositionY, speed
					, speed, speed, turnmode, Constants.ENEMY_BOSS, null, -1));
		}
	}
	
	/**
	 * generate enemy fighter
	 * @param playerPositionX
	 * @param playerPositionY
	 */
	public static void generateEnemy(double playerPositionX, double playerPositionY, boolean follow)
	{
		// the enemy fighter
		int track = ((int) (Math.random() * 359));
		float speed = GameUtils.getPlaneSpeed(Constants.ENEMY_FIGHTER);
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius);
		generateShip((int) playerPositionX + coord[0], (int) playerPositionY + coord[1], 0, speed, follow);
	}
	
	public static void generateEnemyBoss(double playerPositionX, double playerPositionY)
	{
		// the enemy boss
		float speed = GameUtils.getPlaneSpeed(Constants.ENEMY_BOSS);
		generateShipBoss((int)playerPositionX, (int)playerPositionY, 1, speed);
	}
	
	/**
	 * generate small cloud layer
	 * @param playerPositionX
	 * @param playerPositionY
	 * @param playerTrack
	 */
	public static void generateSmallClouds(double playerPositionX, double playerPositionY, int playerTrack)
	{		
		// the cloud
		int track = ((int)(Math.random() * 359));
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius);
		int direction = 180;
//		if (Math.random() * 100 > 50)
//		{
//			direction = 180;
//		}
		double speed = (Math.random() * .5) + .5;
		GameState._cloudListSmall.add(new Cloud(direction, direction, coord[0] + playerPositionX
				, coord[1] + playerPositionY, speed
				, speed, .1d, 0, Constants.CLOUD_SMALL, null, -1, 1));
	}
	
	/**
	 * generate large cloud layer
	 * @param playerPositionX
	 * @param playerPositionY
	 * @param playerTrack
	 */
	public static void generateLargeCloud(double playerPositionX, double playerPositionY, int playerTrack)
	{
		// the cloud
		int track = ((int)(Math.random() * 359));
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius);
		int direction = 180;
//		if (Math.random() * 100 > 50)
//		{
//			direction = 180;
//		}
		double speed = (Math.random() * .5)+.2;
		GameState._cloudListLarge.add(new Cloud(direction, direction, coord[0] + playerPositionX
				, coord[1] + playerPositionY, speed
				, speed, .1d, 0, Constants.CLOUD_BIG, null, -1, 1));
	}
}
