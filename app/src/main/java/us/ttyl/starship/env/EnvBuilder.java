package us.ttyl.starship.env;

import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.core.GameUtils;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.Cloud;
import us.ttyl.starship.movement.ships.EnemyBoss;
import us.ttyl.starship.movement.ships.EnemyFighter;
import us.ttyl.starship.movement.ships.FollowFighter;
import us.ttyl.starship.movement.ships.PlayerOption;
import us.ttyl.starship.movement.ships.TextItemLine;

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
	private static void generateShipBoss(double playerPositionX, double playerPositionY, int turnmode, float speed, float density)
	{
		//determine direction of boss
		int direction = 180;
		int xOffset = (int)(GameState.sObjectCreationRadius + (100 * density));
		if (Math.random() * 100 > 50)
		{
			direction = 0;
			xOffset = -1 * (xOffset);
		}

		if (GameUtils.getTypeCount(Constants.ENEMY_BOSS, GameState._weaponList) <= 0)
		{
			// generate the boss
			GameState._weaponList.add(new EnemyBoss(direction, direction, playerPositionX + xOffset
					, playerPositionY, speed
					, speed, speed, turnmode, Constants.ENEMY_BOSS, null, -1));
		}
	}

	public static int generateDirection() {
		int track = (int)((Math.random()) * 360);
		return track;
	}

	/**
	 * generate enemy fighter
	 * @param playerPositionX
	 * @param playerPositionY
	 */
	public static void generateEnemy(double playerPositionX, double playerPositionY, boolean follow, float density)
	{
		// the enemy fighter
		// int track = ((int) (Math.random() * 359));
		int track = generateDirection();
		float speed = GameUtils.getPlaneSpeed(Constants.ENEMY_FIGHTER);
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius + (int)(100 * density ));
		generateShip((int) playerPositionX + coord[0], (int) playerPositionY + coord[1], 0, speed, follow);
	}
	
	public static void generateEnemyBoss(double playerPositionX, double playerPositionY, float density)
	{
		// the enemy boss
		float speed = GameUtils.getPlaneSpeed(Constants.ENEMY_BOSS);
		generateShipBoss((int) playerPositionX, (int) playerPositionY, 1, speed, density);
	}
	
	/**
	 * generate small cloud layer
	 * @param playerPositionX
	 * @param playerPositionY
	 * @param playerTrack
	 */
	public static void generateSmallClouds(double playerPositionX, double playerPositionY, int playerTrack, float density)
	{		
		// the cloud
		int track = ((int)(Math.random() * 359));
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius + (int)(Constants.SMALL_CLOUD_OFFSET * density));
		int direction = 0;
		if (Math.random() * 100 > 50)
		{
			direction = 180;
		}
		double speed = (Math.random() * Constants.SMALL_CLOUD_SPEED) + Constants.SMALL_CLOUD_SPEED;
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
	public static void generateLargeCloud(double playerPositionX, double playerPositionY, int playerTrack, float density)
	{
		// the cloud
		int track = ((int)(Math.random() * 359));
		double[] coord = GameUtils.getCoordsGivenTrackAndDistance(track, GameState.sObjectCreationRadius +  (int)(Constants.LARGE_CLOUD_OFFSET * density));
		int direction = 0;
		if (Math.random() * 100 > 50)
		{
			direction = 180;
		}
		double speed = (Math.random() * Constants.LARGE_CLOUD_SPEED) + Constants.LARGE_CLOUD_SPEED;
		GameState._cloudListLarge.add(new Cloud(direction, direction, coord[0] + playerPositionX
				, coord[1] + playerPositionY, speed
				, speed, .1d, 0, Constants.CLOUD_BIG, null, -1, 1));
	}

	public static void generatePlayerOption(int startingDegree, int speed, int radius)
	{
		GameState._weaponList.add(new PlayerOption(GameState._weaponList.get(0),
				Constants.PLAYER_OPTION, startingDegree, radius, speed));
	}

	public static void generateTextLine(String text, int speed, int endurance, double x, double y, int direction)
	{
		GameState._weaponList.add(new TextItemLine(text, speed, endurance, x, y, direction ));
	}

}
