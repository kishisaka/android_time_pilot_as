package us.ttyl.starship.core;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Pair;

import us.ttyl.starship.movement.MovementEngine;

public class GameState 
{
	public static double sScreenDensity = 0;
	public static boolean mIsRunning = false;
	public static boolean mIsThrottlePressed = false;
	public static boolean mWaitTimeBetweenLevels = false;
	
	//turn off enemy weapon systems (testing)
	public static boolean sIsFireEnemyGuns = true;
	//turn on player invulnerability (testing)
	public static boolean sIsInvulnerable = true;
	//turn on main loop frame rate
	public static boolean sShowFrameRate = true;
	
	// guns, missiles, player ship, enemy ships, parachutes, etc.
	public static List <MovementEngine> _weaponList = new ArrayList<MovementEngine>();
	//explosion particles
	public static List <MovementEngine> _explosionParticleList = new ArrayList<MovementEngine>();
	//small clouds
	public static List <MovementEngine> _cloudListSmall = new ArrayList<MovementEngine>();
	//large clouds
	public static List <MovementEngine> _cloudListLarge = new ArrayList<MovementEngine>();

	//other images
	public static Bitmap _smallParachute;
	public static Bitmap _bigParachute;
	public static Bitmap _smallMissile;
	public static Bitmap _bossBullet;
	public static Bitmap _land;
	public static Bitmap _water;

	//sprites
	public static ArrayList <Bitmap> _1917_fighters_1;
	public static ArrayList <Bitmap> _1917_fighters_2;
	public static ArrayList <Bitmap> _1941_fighters_1;
	public static ArrayList <Bitmap> _1941_fighters_2;
	public static ArrayList <Bitmap> _1971_fighters_1;
	public static ArrayList <Bitmap> _1971_fighters_2;
	public static ArrayList <Bitmap> _1984_fighters_1;
	public static ArrayList <Bitmap> _1984_fighters_2;
	public static List <Bitmap> _bolts;
	public static List <Bitmap> _enemy_bolts;

	public static ArrayList <Bitmap> _bossSprites1;
	public static ArrayList <Bitmap> _cloudSprites;
	public static ArrayList <Bitmap> _explosionSprites;
	public static ArrayList <Bitmap> _explosionBossSprites;

	public static List<Bitmap> _missileSprites;
	public static List<Bitmap> _playerSprites;
	
	//player score
	public static int _playerScore = 0;
	public static int _playerBulletsShot = 0;
	public static int _playerEnemyShot = 0;

	// missiles per volley
	public static int _missilesPerVolley = 4;
	
	//sound settings
	public static boolean _muted = false;
	
	// player life counter
	public static int _lives = 2;
	
	//screen density
	public static float _density = 1;
	
	//game level indicator
	public static int sParachutePickupCount = 0;
	public static int sCurrentLevel = 1; //1 ww1 planes, 2 ww2 planes, 3 helicopters, 4 jets
	public static int sWaveLevel = 0; // after fighting all 4 levels, this one increases by 1

	// other stuff
	public static int sFramerate = 0;

	//various release intervals(in ms)
	public static long sLastReleasedParachute = 0;
	public static long sLastDeath = 0;
	public static long sLastParachutePickupInterval = 0;
	public static long sLastBossExplosion = 0;

	// Enemy Boss hitpoints
	public static int sBossHitPoints = Constants.BOSS_STARTING_HITPOINT;

	// object creation radius
	public static int sObjectCreationRadius = 0;

    // current frame (only two, so far)
    public static int sFrame = 1;

    // start time between bosses
    public static long sStartTimeBoss = System.currentTimeMillis();

	public static boolean sShownLevelName = false;

	public static int sCurrentGameState = Constants.CURRENT_GAME_STATE_TITLE;

	public static Score _highScore;
	public static List <Score> _highScores;

	public static final int MAX_CLOUDS = 50;

    // land mass stuff
	public static int[][] _landmass;

	public static void clearAll() {
		_weaponList.clear();
		_cloudListLarge.clear();
		_cloudListSmall.clear();
		_explosionParticleList.clear();
		_lives = 2;
		sFrame = 1;
		sStartTimeBoss = System.currentTimeMillis();
		sBossHitPoints = Constants.BOSS_STARTING_HITPOINT;
		sParachutePickupCount = 0;
		sCurrentLevel = 1;
		sWaveLevel = 0;
		_playerScore = 0;
		_playerBulletsShot = 0;
		_playerEnemyShot = 0;
	}
}

