package us.ttyl.starship.core;

import us.ttyl.asteroids.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * missile sound: https://www.freesound.org/people/smcameron/sounds/51468/
 * parachute pickup: https://www.freesound.org/people/fins/sounds/133280/
 * helipcopter https://www.freesound.org/people/fridobeck/sounds/194250/
 *
 *
 *
 */
public class AudioPlayer 
{
	public static int sPlayerGunSoundId;
    public static int sPlayerGunSoundStreamId;
    public static int sMissileSoundId;
    public static int sShipDeathSoundId;
    public static int sParachutePickup;
    public static int sLevelChange;
    public static int sEnemyGunSound;
    public static int sHelicopterBoss;
    public static int sHelicopterBossStreamId;

    public static SoundPool sSoundPool;
	
	/**
	 * do this once, load all sounds into pool and obtain sounds ids for them. 
	 * @param context
	 */
	public static void initSound(Context context)
	{
		sSoundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 0);
		sPlayerGunSoundId = sSoundPool.load(context, R.raw.gun3, 1);
        sHelicopterBoss = sSoundPool.load(context, R.raw.helicopter, 1);
		sShipDeathSoundId = sSoundPool.load(context, R.raw.enemy_death, 2);
		sMissileSoundId = sSoundPool.load(context, R.raw.missile_launch2, 2);
		sParachutePickup = sSoundPool.load(context, R.raw.parachute_pickup, 2);
		sLevelChange = sSoundPool.load(context, R.raw.level_change, 2);
		sEnemyGunSound = sSoundPool.load(context, R.raw.enemy_gun, 2);
	}
	
	/**
	 * play the ship death sound
	 */
	public static void playShipDeath()
	{
		sSoundPool.play(sShipDeathSoundId, .25f, .25f, 2, 0, 1.0f);
	}
	
	/**
	 * play the missile sounds 
	 */
	public static void playMissileSound()
	{
		sSoundPool.play(sMissileSoundId, .25f, .25f, 2, 0, 1.0f);
	}
	
	/**
	 * stop the player gun sounds
	 */
	public static void resumePlayerGun()
	{
		sSoundPool.resume(sPlayerGunSoundStreamId);
	}
	
	/**
	 * pause the player gun sound
	 */
	public static void pausePlayerGun()
	{
		sSoundPool.pause(sPlayerGunSoundStreamId);
	}
	
	/**
	 * play the player gun sound
	 */
	public static void playPlayerGun()
	{
        sPlayerGunSoundStreamId = sSoundPool.play(sPlayerGunSoundId, .5f, .5f, Integer.MAX_VALUE, -1, 1.0f);
	}
	
	/**
	 * only kill the player gun audio, all others will end very quickly. 
	 */
	public static void killAllAudio()
	{		
		sSoundPool.stop(sPlayerGunSoundId);
	}
	
	/**
	 * play parachute pickup sounds
	 */
	public static void playParachutePickup()
	{
		sSoundPool.play(sParachutePickup, .5f, .5f, 2, 0, 1.0f);
	}

    /**
     * play level change sound
     */
	public static void playLevelChange()
	{
		sSoundPool.play(sLevelChange, 1.0f, 1.0f, 2, 0, 1.0f);
	}

    /**
     * play enemy gun sound
     */
	public static void playEnemyGun()
	{
        sSoundPool.play(sEnemyGunSound, 1.0f, 1.0f, 2, 0, 1.0f);
	}

    /**
     * start helicopter sound
     * @return helicopter stream id
     */
    public static void playHelicopter()
    {
        sHelicopterBossStreamId = sSoundPool.play(sHelicopterBoss, 0.1f, 0.1f, 2, -1, 1.0f);
    }

    /**
     * pause helicopter sound
     */
    public static void stopHelicopter()
    {
        sSoundPool.stop(sHelicopterBossStreamId);
    }

    /**
     * change helicopter volume
     * @param volume
     */
    public static void changeHelicopterVolume(float volume)
    {
        sSoundPool.setVolume(sHelicopterBossStreamId, volume, volume);
    }

    public static void pauseAll()
    {
        sSoundPool.autoPause();
    }

    public static void resumeAll()
    {
        sSoundPool.autoResume();
    }
}
