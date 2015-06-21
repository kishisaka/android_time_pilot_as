package us.ttyl.starship.core;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Bitmap;
import android.media.SoundPool;
import us.ttyl.starship.movement.MovementEngine;

public class GameState 
{
	public static boolean mIsRunning = false; 
	public static boolean mIsThrottlePressed = false;
	public static boolean mWaitTimeBetweenLevels = false;
	
	//turn off enemy weapon systems (testing)
	public static boolean sIsFireEnemyGuns = true;
	//turn on player invulnerability (testing)
	public static boolean sIsInvulnerable = false;
	
	// guns, missiles, player ship, enemy ships, parachutes, etc.
	public static Vector <MovementEngine> _weaponList = new Vector<MovementEngine>();
	//explosion particles
	public static Vector <MovementEngine> _explosionParticleList = new Vector<MovementEngine>();
	//small clouds
	public static Vector <MovementEngine> _cloudListSmall = new Vector<MovementEngine>();
	//large clouds
	public static Vector <MovementEngine> _cloudListLarge = new Vector<MovementEngine>();
	
	//sprites
	public static ArrayList <Bitmap> _sprites1;
    public static ArrayList <Bitmap> _sprites2;
	public static ArrayList <Bitmap> _bossSprites1;
    public static ArrayList <Bitmap> _bossSprites2;
	public static ArrayList <Bitmap> _cloudSprites;
	public static Bitmap _bossBullet;
	
	//player score
	public static int _playerScore = 0;
	public static int _playerBulletsShot = 0;
	public static int _playerEnemyShot = 0; 
	
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
}

