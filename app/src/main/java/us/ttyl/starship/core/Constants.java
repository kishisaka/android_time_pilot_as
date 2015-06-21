package us.ttyl.starship.core;

public class Constants {
	public static final int EXPLOSION_PARTICLE = 1;
	public static final int CLOUD_SMALL = 2;
	public static final int CLOUD_BIG = 17;
	public static final int PARACHUTE = 3;
	public static final int MISSILE_SMOKE = 4;
	public static final int BOSS_SMOKE = 5;
	public static final int MISSILE_PLAYER = 6;
	public static final int MISSILE_ENEMY = 7;
	public static final int GUN_PLAYER = 8;
	public static final int ENEMY_FIGHTER = 9;
	public static final int ENEMY_BOSS = 10;
	public static final int PLAYER = 11;
	public static final int GUN_ENEMY = 12;
	public static final int MISSILE = 13;
	public static final int PARACHUTE_SMALL = 14;
	public static final int MISSILE_SMALL = 15;
	public static final int TEXT = 16;
	public static final int GUN_ENEMY_BOSS = 17;

	//player initialization
	public static final int START_MISSILE_COUNT = 20;

	//explosion stuff
	public static final int EXPLOSION_ENDURANCE = 45;
	public static final int EXPLOSION_PARTICLE_COUNT = 8;

	//smoke stuff
	public static final int SMOKE_ENDURANCE_BOSS = 30;
	public static final int SMOKE_ENDURANCE_MISSLE = 10;

	//various release intervals (in ms)
	public static final int PARACHUTE_RELEASE_INTERVAL = 200;
	public static final int PARACHUTE_PICKUP_INTERVAL = 200;
	public static final int DEATH_INTERVAL = 200;
	public static final int BOSS_EXPLOSION_INTERVAL = 100;

	//boss initialization
	public static final int BOSS_STARTING_HITPOINT = 100;

	public static final int ENEMY_BOSS_FIRE_RADIUS = 700;
	public static final int ENEMY_FIGHTER_FiRE_RADIUS = 800;

	//minimum frame rate allowed before fire rate limitation imposed
	public static final int FRAME_RATE_MIN = 45;

    // frames max for animation
    public static int sMaxFrame = 4;
}
