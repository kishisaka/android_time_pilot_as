package us.ttyl.starship.core;

import us.ttyl.asteroids.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * missile sound: https://www.freesound.org/people/smcameron/sounds/51468/
 * parachute pickup: https://www.freesound.org/people/fins/sounds/133280/
 *
 * helicopter: https://www.freesound.org/people/fridobeck/sounds/194250/
 * jets: https://www.freesound.org/people/qubodup/sounds/162445/
 * ww2 https://www.freesound.org/people/treyc/sounds/123919/
 * airship https://www.freesound.org/people/litewerx/sounds/113633/
 *
 */
public class AudioPlayer 
{
    // sound ids
	public static int sPlayerGunSoundId;
    public static int sMissileSoundId;
    public static int sShipDeathSoundId;
    public static int sParachutePickup;
    public static int sLevelChange;
    public static int sEnemyGunSound;

    public static int sWW2Boss;
    public static int sHelicopterBoss;
    public static int sJetBoss;
    public static int sBlimpBoss;

    //stream ids (looping sounds)
    public static int sPlayerGunSoundStreamId;
    public static int sHelicopterBossStreamId;
    public static int sJetBossStreamId;
    public static int sBlimpBossStreamId;
    public static int sWW2BossStreamId;

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
        sJetBoss = sSoundPool.load(context, R.raw.jet, 1);
        sBlimpBoss = sSoundPool.load(context, R.raw.blimp, 1);
        sWW2Boss = sSoundPool.load(context, R.raw.ww2, 1);
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
    public static void playBoss()
    {
        if (GameState.sCurrentLevel == 1) {
            sSoundPool.stop(sBlimpBossStreamId);
            sBlimpBossStreamId = sSoundPool.play(sBlimpBoss, 0.1f, 0.1f, 2, -1, 1.0f);
        }
        else if (GameState.sCurrentLevel == 2) {
            sSoundPool.stop(sWW2BossStreamId);
            sWW2BossStreamId = sSoundPool.play(sWW2Boss, 0.1f, 0.1f, 2, -1, 1.0f);
        }
        else if (GameState.sCurrentLevel == 3) {
            sSoundPool.stop(sHelicopterBossStreamId);
            sHelicopterBossStreamId = sSoundPool.play(sHelicopterBoss, 0.1f, 0.1f, 2, -1, 1.0f);
        }
        else {
            sSoundPool.stop(sJetBossStreamId);
            sJetBossStreamId = sSoundPool.play(sJetBoss, 0.1f, 0.1f, 2, -1, 1.0f);
        }
    }

    /**
     * pause helicopter sound
     */
    public static void stopBoss()
    {
        if (GameState.sCurrentLevel == 1) {
            sSoundPool.stop(sBlimpBossStreamId);
        }
        else if (GameState.sCurrentLevel == 2) {
            sSoundPool.stop(sWW2BossStreamId);
        }
        else if (GameState.sCurrentLevel == 3) {
            sSoundPool.stop(sHelicopterBossStreamId);
        }
        else
        {
            sSoundPool.stop(sJetBossStreamId);
        }
    }

    /**
     * change helicopter volume
     * @param volume
     */
    public static void changeBossVolume(float volume) {
        if (GameState.sCurrentLevel == 1) {
            sSoundPool.setVolume(sBlimpBossStreamId, volume, volume);
        }
        else if (GameState.sCurrentLevel == 2) {
            sSoundPool.setVolume(sWW2BossStreamId, volume, volume);
        }
        else if (GameState.sCurrentLevel == 3) {
            sSoundPool.setVolume(sHelicopterBossStreamId, volume, volume);
        }
        else
        {
            sSoundPool.setVolume(sJetBossStreamId, volume, volume);
        }
    }

    public static void pauseAll()
    {
        // sSoundPool.autoPause();
        sSoundPool.release();
    }

    public static void resumeAll()
    {
        sSoundPool.autoResume();
    }
}
