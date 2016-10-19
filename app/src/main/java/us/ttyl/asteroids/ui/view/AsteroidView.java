package us.ttyl.asteroids.ui.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import us.ttyl.asteroids.R;
import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.Constants;
import us.ttyl.starship.core.DBHelper;
import us.ttyl.starship.core.GameState;
import us.ttyl.starship.core.GameUtils;
import us.ttyl.starship.core.MainLoop;
import us.ttyl.starship.core.Score;
import us.ttyl.starship.env.EnvBuilder;
import us.ttyl.starship.listener.GameStateListener;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.Bullet;
import us.ttyl.starship.movement.ships.Missile;
import us.ttyl.starship.movement.ships.PlayerFighter;
import us.ttyl.starship.movement.ships.TextItem;
import us.ttyl.starship.movement.ships.TextItemLine;

/**
 * custom view to render the player view. Uses data in GameState holder which is updated
 * by the MainLoop. 
 * 
 * @author kurt ishisaka
 *
 */
public class AsteroidView extends SurfaceView implements SurfaceHolder.Callback {
    AsteroidViewThread mAsteroidViewThread = null;
    Bitmap mBackground = null;
    int mControllerCircleX = 0;
    int mControllerCircleY = 0;

    int mSpecialWeaponX = 0;
    int mSpecialWeaponY = 0;

    int _scale = 1;
    static final int SELECTED = 0;

    long mMissileLastLaunch = 0;

    int mControllerX = 0;
    int mControllerY = 0;

    private static int[] _degrev = null;

    private static String TAG ="AsteroidView";

    private GameStateListener mGameStateListener = new GameStateListener() {
        @Override
        public void onPlayerDied() {
            GameState._lives = GameState._lives - 1;
            if (GameState._lives >= 0) {
                MovementEngine player = new PlayerFighter(0, 0, 0d, 0d, 2.5d, 2.5d, 5, .1d, 0, Constants.PLAYER, -1, this);
                player.setMissileCount(Constants.START_MISSILE_COUNT);
                if (GameState._weaponList.isEmpty() == false) {
                    GameState._weaponList.set(0, player);
                    GameState.sShownLevelName = false;
                }
                AudioPlayer.resumePlayerGun();
            } else {
                GameState._weaponList.remove(0);
                GameState.sParachutePickupCount = 0;
                GameState.sShownLevelName = false;
                DBHelper helper = new DBHelper(getContext());
                helper.writeScore(GameState._playerScore, GameState.sCurrentLevel, GameState.sWaveLevel);
                GameState._highScore = helper.getTopScore();
                GameState._highScores = helper.getTop10Scores();
            }

            //remove all enemy guns and missiles
            for (int i = 0; i < GameState._weaponList.size(); i++) {
                MovementEngine enemyWeapon = GameState._weaponList.get(i);
                if (enemyWeapon.getWeaponName() == Constants.GUN_ENEMY
                        || enemyWeapon.getWeaponName() == Constants.ENEMY_FIGHTER
                        || enemyWeapon.getWeaponName() == Constants.TEXT
                        || enemyWeapon.getWeaponName() == Constants.PLAYER_OPTION) {
                    GameState._weaponList.remove(i);
                }
            }

            //clear explosions list
            GameState._explosionParticleList.clear();
            GameState._cloudListLarge.clear();
            GameState._cloudListSmall.clear();
        }
    };

    public AsteroidView(Context context, AttributeSet attr) {
        super(context);
        SurfaceHolder holder = getHolder();
        if (holder != null) {
            holder.addCallback(this);
            // mBackground = BitmapFactory.decodeResource(getResources(), R.drawable.helicopter);

            // do game initialization (load sprites, start sounds, etc.)
            init(context);

            // create view thread only; it will be started in surfaceCreated()
            mAsteroidViewThread = new AsteroidViewThread(context, holder);
            setFocusable(true); // make sure we get key events
        }
        else {
            Log.e(TAG, "holder is null");
        }
    }

    /**
     * initialize the game resources (sprites, etc) and prepare to run game.
     *
     * @param context
     */
    public void init(Context context) {

        GameState.clearAll();

        long startTime = System.currentTimeMillis();
        //initialize sprite array
        GameState._cloudSprites = GameUtils.getCloudTiles(context);
        GameState._bossBullet = GameUtils.getBossBullet(context);
        GameState._explosionSprites = GameUtils.getAnimatedExplosionSprites(context);
        GameState._explosionBossSprites = GameUtils.getAnimatedExplosionSpritesBoss(context);
        GameUtils.getOther(context);
        GameUtils.getBoltBitmaps(context);
        GameUtils.getEnemyBoltBitmaps(context);
        GameState._playerSprites = GameUtils.getPlayerSprites(context);
        GameState._1917_fighters_1 = GameUtils.getFighters1917FromFile(context, 1);
        GameState._1917_fighters_2 = GameUtils.getFighters1917FromFile(context, 2);
        GameState._1941_fighters_1 = GameUtils.getFighters1941FromFile(context, 1);
        GameState._1941_fighters_2 = GameUtils.getFighters1941FromFile(context, 2);
        GameState._1971_fighters_1 = GameUtils.getFighters1971FromFile(context, 1);
        GameState._1971_fighters_2 = GameUtils.getFighters1971FromFile(context, 2);
        GameState._1984_fighters_1 = GameUtils.getFighters1984FromFile(context, 1);
        GameState._1984_fighters_2 = GameUtils.getFighters1984FromFile(context, 2);
        GameState._1917_fighters_1 = GameUtils.getFighters1917FromFile(context, 1);
        GameState._1917_fighters_2 = GameUtils.getFighters1917FromFile(context, 2);
        GameState._1941_fighters_1 = GameUtils.getFighters1941FromFile(context, 1);
        GameState._1941_fighters_2 = GameUtils.getFighters1941FromFile(context, 2);
        GameState._1971_fighters_1 = GameUtils.getFighters1971FromFile(context, 1);
        GameState._1971_fighters_2 = GameUtils.getFighters1971FromFile(context, 2);
        GameState._1984_fighters_1 = GameUtils.getFighters1984FromFile(context, 1);
        GameState._1984_fighters_2 = GameUtils.getFighters1984FromFile(context, 2);
        GameState._bossSprites1 = GameUtils.getBossTilesFromFile(context);
        GameState._missileSprites = GameUtils.getMissileBitmaps(context);

        //initalize sound
        AudioPlayer.initSound(context);

        // wipe weapon list and add player to ship list if not already there
        if (GameState._weaponList.isEmpty() == false) {
            GameState._weaponList.clear();
            GameState._cloudListLarge.clear();
            GameState._cloudListSmall.clear();
            GameState._explosionParticleList.clear();
        }
        // MovementEngine player = new PlayerFighter(0, 0, 0d, 0d, 2d, 2d, 5, .1d, 0, Constants.PLAYER, -1, mGameStateListener);
        EnvBuilder.generateEnemy(0, 0, false, getResources().getDisplayMetrics().density);
        // player.setMissileCount(Constants.START_MISSILE_COUNT);
        // GameState._weaponList.add(player);

        // start the game engine!
        float density = getResources().getDisplayMetrics().density;
        int height = getResources().getDisplayMetrics().heightPixels;
        int width = getResources().getDisplayMetrics().widthPixels;
        GameState.sObjectCreationRadius = (int)(GameUtils.getRangeBetweenCoords(width / 2, height / 2, 0, (width - height) / 2) / density);
        Log.i("kurt_test" , "object creation radius: " + GameState.sObjectCreationRadius);
        Log.i("kurt_test" , "init before main loop start time: " + (System.currentTimeMillis() - startTime));

        //initialize deg map (reversed)
        _degrev = new int[360];
        int counter = 359;
        for (int i = 0; i < 360; i++) {
            _degrev[i] = counter;
            counter = counter - 1;
        }
    }

    /**
     * the game rendering thread
     *
     * @author test
     */
    public class AsteroidViewThread extends Thread {
        SurfaceHolder mSurfaceHolder = null;
        Context mAppContext = null;
        boolean mIsRunning = true;

        int opacityLevel = 255;
        boolean opacityLevelUP = true;

        Paint mGunEnemy = new Paint();
        Paint mGunPlayer = new Paint();
        Paint mParticleExplosion = new Paint();
        Paint mMissileSmoke1 = new Paint();
        Paint mMissileSmoke2 = new Paint();
        Paint mMissileSmoke3 = new Paint();
        Paint mTextColor = new Paint();
        Paint mHighScoreColor = new Paint();
        Paint mControllerColor = new Paint();
        Paint mParticleExplosionBoss = new Paint();
        Paint mBorderColor = new Paint();
        Paint mTextItemColor = new Paint();
        Paint mTextItemLineColor = new Paint();

        private static final String TAG = "AsteroidViewThread";

        public AsteroidViewThread(Context context, SurfaceHolder holder) {
            mAppContext = context;
            mSurfaceHolder = holder;

            //setup the color for all objects
            mGunEnemy.setColor(Color.BLUE);
            mGunPlayer.setColor(Color.GREEN);
            mParticleExplosion.setColor(0xd0928380);
            mParticleExplosionBoss.setColor(0xffd3d3d3);
            mMissileSmoke1.setColor(0xffffaa00);
            mMissileSmoke2.setColor(0xffffff80);
            mMissileSmoke3.setColor(0xffff0000);
            mTextColor.setColor(Color.WHITE);
            mTextColor.setTextSize(20 * context.getResources().getDisplayMetrics().scaledDensity);
            mTextColor.setShadowLayer(2f, 2f, 2f, Color.GRAY);

            mHighScoreColor.setColor(Color.WHITE);
            mHighScoreColor.setTextSize(15 * context.getResources().getDisplayMetrics().scaledDensity);
            mHighScoreColor.setShadowLayer(2f, 2f, 2f, Color.GRAY);

            mControllerColor.setColor(0x7f00988a);
            mBorderColor.setColor(Color.BLACK);
            mTextItemColor.setColor(Color.DKGRAY);
            mTextItemLineColor.setColor(Color.DKGRAY);
            mTextItemColor.setTextSize(15 * context.getResources().getDisplayMetrics().scaledDensity);
            mTextItemLineColor.setTextSize(25 * context.getResources().getDisplayMetrics().scaledDensity);
        }

        public void run() {
            Log.d("kurt_test", "started asteroid view thread");
            //draw the player window to the screen
            Canvas canvas = null;

            while (GameState.mIsRunning == true) {
                //draw to the onscreen buffer using onDraw(), lock the canvas first
                try {
                    if (mSurfaceHolder != null) {
                        canvas = mSurfaceHolder.lockCanvas();
                        if (canvas != null) {
                            synchronized (canvas) {
                                doDraw(canvas);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "canvas or surface null, forget this one and continue", e);
                } finally {
                    if (canvas != null) {
                        //post the canvas to the surface
                        mSurfaceHolder.unlockCanvasAndPost(canvas);

                        // this is weird, we need to wait for a bit for post to finish before we
                        // lock the canvas and write to it. See stack overflow below for possible explanation.
                        // http://stackoverflow.com/questions/34305937/anr-in-surfaceview-on-specific-devices-only-the-only-fix-is-a-short-sleep-tim
                        // if we don't do this, app will lock up on restart for a while. on Nexus 5x it may never start again. Samsung devices don't
                        // seem to have this problem. Seems like this is happening only on > level 19 api devices (post kitkat)
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            try {
                                sleep(0, 1);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            Log.d("kurt_test", "ending asteroid view thread");
        }

        @Override
        public void setContextClassLoader(ClassLoader cl) {
            super.setContextClassLoader(cl);
        }

        /**
         * draw the tiles to the onscreen buffer here!
         *
         * @param canvas
         */
        public void doDraw(Canvas canvas) {
            // draw background
            // canvas.drawBitmap(mBackground, 0, 0, null);

            if (GameState.mWaitTimeBetweenLevels == false) {
                opacityLevel = 255;
                opacityLevelUP = true;
            } else {
                if (opacityLevelUP == true) {
                    opacityLevel = opacityLevel - 3;
                }
                if (opacityLevelUP == false) {
                    opacityLevel = opacityLevel + 3;
                }
                if (opacityLevel < 0) {
                    opacityLevelUP = false;
                    opacityLevel = 0;
                }
            }
            switch (GameState.sCurrentLevel) {
                case 1:
                    canvas.drawARGB(opacityLevel, 54, 176, 237);
                    break;
                case 2:
                    canvas.drawARGB(opacityLevel, 189, 173, 233);
                    break;
                case 3:
                    //canvas.drawARGB(opacityLevel, 99, 36, 99);
                    canvas.drawARGB(opacityLevel, 54, 176, 237);
                    break;
                case 4:
                    canvas.drawARGB(opacityLevel, 0, 0, 0);
                    break;
            }

            MovementEngine me;

            //draw the objects
            //set center target
            try {
                me = GameState._weaponList.get(SELECTED);

                double centerX = (int) me.getX();
                double centerY = (int) me.getY();

                int centerXCanvas = (int) (mAppContext.getResources().getDisplayMetrics().widthPixels / 2);
                int centerYCanvas = (int) (mAppContext.getResources().getDisplayMetrics().heightPixels / 2);

                // draw the small clouds
                for (int i = 0; i < GameState._cloudListSmall.size(); i++) {
                    me = GameState._cloudListSmall.get(i);
                    double x = GameUtils.getA(centerX, me.getX()) / _scale;
                    double y = GameUtils.getB(centerY, me.getY()) / _scale;
                    double track = GameUtils.track(centerX, centerY, me.getX(), me.getY());
                    double range = GameUtils.getRange(x, y);
                    x = range * Math.cos(Math.toRadians(track));
                    y = range * Math.sin(Math.toRadians(track));
                    canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.CLOUD_SMALL), (int) ((centerXCanvas + x) / 1.7), (int) ((centerYCanvas - y) / 1.7), null);
                }

                me = GameState._weaponList.get(SELECTED);

                int density = (int) getContext().getResources().getDisplayMetrics().density;

                int pixelSize = 3 * density;
                int misslePixel = 2 * density;

                for (int i = 0; i < GameState._weaponList.size(); i++) {
                    try {
                        me = GameState._weaponList.get(i);
                        double x = GameUtils.getA(centerX, me.getX()) / _scale;
                        double y = GameUtils.getB(centerY, me.getY()) / _scale;
                        double track = GameUtils.track(centerX, centerY, me.getX(), me.getY());
                        double range = GameUtils.getRange(x, y);
                        x = range * Math.cos(Math.toRadians(track));
                        y = range * Math.sin(Math.toRadians(track));

                        if (me.getWeaponName() == Constants.PLAYER && me.getDestroyedFlag() == false) {
                            int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.PLAYER).getWidth();
                            int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.PLAYER).getHeight();
                            canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.PLAYER), centerXCanvas - (spriteXSize / 2), centerYCanvas - (spriteYSize / 2), null);
                        }
                        if (me.getWeaponName() == Constants.PLAYER_OPTION) {
                            int spriteXSize = GameState._bossBullet.getWidth();
                            int spriteYSize = GameState._bossBullet.getHeight();
                            canvas.drawBitmap(GameState._bossBullet, (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        }
                        if (me.getWeaponName() == (Constants.ENEMY_FIGHTER)) {
                            // int rotorRandom = (int)(Math.random() * 360);
//                            int rotorDeg = ((FollowFighter)me).getRotor();
//                            int spriteXSizeHeli = GameState._enemy1971Sprites.get(me.getCurrentDirection()).getWidth();
//                            int spriteYSizeHeli = GameState._enemy1971Sprites.get(me.getCurrentDirection()).getHeight();
//                            int spriteXSizeRotor = GameState._enemy1971RotorSprites.get(rotorDeg).getWidth();
//                            int spriteYSizeRotor = GameState._enemy1971RotorSprites.get(rotorDeg).getHeight();
//                            canvas.drawBitmap(GameState._enemy1971Sprites.get(me.getCurrentDirection()),
//                                    (int) (centerXCanvas - (spriteXSizeHeli / 2) + x), (int) (centerYCanvas - (spriteYSizeHeli / 2) - y), null);
//                            canvas.drawBitmap(GameState._enemy1971RotorSprites.get(rotorDeg),
//                                    (int) (centerXCanvas - (spriteXSizeRotor / 2) + x), (int) (centerYCanvas - (spriteYSizeRotor / 2) - y), null);
//                            canvas.drawBitmap(GameState._enemy1971RotorSprites.get(rotorDeg),
//                                    (int) (centerXCanvas - (spriteXSizeRotor / 2) + x), (int) (centerYCanvas - (spriteYSizeRotor / 2) - y), null);
                            int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_FIGHTER).getWidth();
                            int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_FIGHTER).getHeight();
                            canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_FIGHTER), (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        }
                        if (me.getWeaponName() == (Constants.PARACHUTE)) {
                            int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.PARACHUTE).getWidth();
                            int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.PARACHUTE).getHeight();
                            canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.PARACHUTE), (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        }
                        if (me.getWeaponName() == (Constants.ANIMATED_EXPLOSION)) {
                            Bitmap animatedExplosion = GameUtils.getImageType(me.getEndurance(), Constants.ANIMATED_EXPLOSION);
                            int spriteXSize = animatedExplosion.getWidth();
                            int spriteYSize = animatedExplosion.getHeight();
                            canvas.drawBitmap(animatedExplosion, (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        }

                        if (me.getWeaponName() == (Constants.ANIMATED_BOSS_EXPLOSION)) {
                            Bitmap animatedExplosion = GameUtils.getImageType(me.getEndurance(), Constants.ANIMATED_BOSS_EXPLOSION);
                            int spriteXSize = animatedExplosion.getWidth();
                            int spriteYSize = animatedExplosion.getHeight();
                            canvas.drawBitmap(animatedExplosion, (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        }
                        if (me.getWeaponName() == (Constants.ENEMY_BOSS)) {
//                            int spriteXSize = GameState._awacs.get(0).getWidth();
//                            int spriteYSize = GameState._awacs.get(0).getHeight();
//                            canvas.drawBitmap(GameState._awacs.get(0), (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                            int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_BOSS).getWidth();
                            int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_BOSS).getHeight();
                            canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.ENEMY_BOSS), (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                        } else if (me.getWeaponName() == (Constants.GUN_ENEMY)) {
                            if (me.getOrigin().getWeaponName() == Constants.ENEMY_FIGHTER) {
                                int spriteXSize = GameState._enemy_bolts.get(me.getCurrentDirection()).getWidth();
                                int spriteYSize = GameState._enemy_bolts.get(me.getCurrentDirection()).getHeight();
                                canvas.drawBitmap(GameState._enemy_bolts.get(me.getCurrentDirection())
                                        , (int) (centerXCanvas - (spriteXSize / 2) + x)
                                        , (int) (centerYCanvas - (spriteYSize / 2) - y)
                                        , null);
                                // canvas.drawRect((int) (centerXCanvas + x), (int) (centerYCanvas - y), (int) (centerXCanvas + pixelSize + x), (int) (centerYCanvas + pixelSize - y), mGunEnemy);
                        } else {
                            if (me instanceof Bullet) {
                                int spriteXSize = GameState._enemy_bolts.get(me.getCurrentDirection()).getWidth();
                                int spriteYSize = GameState._enemy_bolts.get(me.getCurrentDirection()).getHeight();
                                canvas.drawBitmap(GameState._enemy_bolts.get(me.getCurrentDirection())
                                        , (int) (centerXCanvas - (spriteXSize / 2) + x)
                                        , (int) (centerYCanvas - (spriteYSize / 2) - y)
                                        , null);
                                // canvas.drawRect((int) (centerXCanvas + x), (int) (centerYCanvas - y), (int) (centerXCanvas + pixelSize + x), (int) (centerYCanvas + pixelSize - y), mGunEnemy);
                            } else {
                                int spriteXSize = GameState._bossBullet.getWidth();
                                    int spriteYSize = GameState._bossBullet.getHeight();
                                    canvas.drawBitmap(GameState._bossBullet, (int) (centerXCanvas - (spriteXSize / 2) + x), (int) (centerYCanvas - (spriteYSize / 2) - y), null);
                                }
                            }
                        } else if (me.getWeaponName() == (Constants.GUN_PLAYER)) {
                            int spriteXSize = GameState._bolts.get(me.getCurrentDirection()).getWidth();
                            int spriteYSize = GameState._bolts.get(me.getCurrentDirection()).getHeight();
                            canvas.drawBitmap(GameState._bolts.get(me.getCurrentDirection())
                                    , (int) (centerXCanvas - (spriteXSize / 2) + x)
                                    , (int) (centerYCanvas - (spriteYSize / 2) - y)
                                    , null);
                            // canvas.drawRect((int) (centerXCanvas + x), (int) (centerYCanvas - y), (int) (centerXCanvas + pixelSize + x), (int) (centerYCanvas + pixelSize - y), mGunPlayer);

                        } else if (me.getWeaponName() == (Constants.MISSILE_PLAYER) || me.getWeaponName() == (Constants.MISSILE_ENEMY)) {
                            int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.MISSILE).getWidth();
                            int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.MISSILE).getHeight();
                            canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.MISSILE)
                                    , (int) (centerXCanvas - (spriteXSize / 2) + x)
                                    , (int) (centerYCanvas - (spriteYSize / 2) - y)
                                    , null);
                        } else if (me.getWeaponName() == (Constants.TEXT)) {
                            canvas.drawText(((TextItem) me).getText(), (int) (centerXCanvas + x), (int) (centerYCanvas - y), mTextItemColor);
                        } else if (me.getWeaponName() == (Constants.TEXT_ITEM_LINE)) {
                            canvas.drawText(((TextItemLine) me).getText(), (int) (centerXCanvas + x), (int) (centerYCanvas - y), mTextItemLineColor);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.e(TAG, e.getMessage(), e);
                        // do nothing, simply continue to next weapon. this item might have already been destroyed by main loop
                    }
                }

                // draw the large clouds
                for (int i = 0; i < GameState._cloudListLarge.size(); i++) {
                    me = GameState._cloudListLarge.get(i);
                    double x = GameUtils.getA(centerX, me.getX()) / _scale;
                    double y = GameUtils.getB(centerY, me.getY()) / _scale;
                    double track = GameUtils.track(centerX, centerY, me.getX(), me.getY());
                    double range = GameUtils.getRange(x, y);
                    x = range * Math.cos(Math.toRadians(track));
                    y = range * Math.sin(Math.toRadians(track));
                    canvas.drawBitmap(GameUtils.getImageType(me.getCurrentDirection(), Constants.CLOUD_BIG), (int) (centerXCanvas + x), (int) (centerYCanvas - y), null);
                }

                // draw all explosion particles (explosions, smoke, etc)
                for (int i = 0; i < GameState._explosionParticleList.size(); i++) {
                    me = GameState._explosionParticleList.get(i);
                    double x = GameUtils.getA(centerX, me.getX()) / _scale;
                    double y = GameUtils.getB(centerY, me.getY()) / _scale;
                    double track = GameUtils.track(centerX, centerY, me.getX(), me.getY());
                    double range = GameUtils.getRange(x, y);
                    x = range * Math.cos(Math.toRadians(track));
                    y = range * Math.sin(Math.toRadians(track));
                    if (me.getWeaponName() == Constants.EXPLOSION_PARTICLE) {
                        canvas.drawRect((int) (centerXCanvas + x), (int) (centerYCanvas - y),
                                (int) (centerXCanvas + pixelSize + x), (int) (centerYCanvas + pixelSize - y), mParticleExplosion);
                    } else if (me.getWeaponName() == (Constants.MISSILE_SMOKE)) {
                        Paint smokeColor = null;
                        if (me.getEndurance() > 10) {
                            smokeColor = mMissileSmoke1;
                        }
                        if (me.getEndurance() >= 7 && me.getEndurance() <= 10) {
                            smokeColor = mMissileSmoke2;
                        }
                        if (me.getEndurance() < 7) {
                            smokeColor = mMissileSmoke3;
                        }

                        int[] smokeOffset = GameUtils.getSmokeTrailXY(me.getCurrentDirection());
                        int x1 = (int) (centerXCanvas + x) + (density * smokeOffset[0]);
                        int y1 = (int) (centerYCanvas - y) + (density * smokeOffset[1]);
                        int x2 = x1 + misslePixel;
                        int y2 = y1 + misslePixel;
                        canvas.drawRect(x1, y1, x2, y2, smokeColor);
                    } else if (me.getWeaponName() == (Constants.BOSS_SMOKE)) {
                        canvas.drawRect((int) (centerXCanvas + x), (int) (centerYCanvas - y),
                                (int) (centerXCanvas + pixelSize + x), (int) (centerYCanvas + pixelSize - y), mParticleExplosionBoss);
                    }
                }

                // draw the top and bottom borders
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                height = (height - width) / 2;
                canvas.drawRect(0, 0, width, height, mBorderColor);
                canvas.drawRect(0, getResources().getDisplayMetrics().heightPixels - height, width, getResources().getDisplayMetrics().heightPixels, mBorderColor);

                // draw the score
                canvas.drawText("1-UP: " + GameState._playerScore, (int) (15 * density), (int) (70 * density), mTextColor);
                if (GameState._highScore != null) {
                    canvas.drawText("High Score: " + GameState._highScore.getScore(), (int) (15 * density), (int) (100 * density), mTextColor);
                }

                // draw the frame rate
                if (GameState.sShowFrameRate == true) {
                    canvas.drawText("" + GameState.sFramerate, 500, 500, mTextColor);
                }

                // draw the player life count
                int counter = (getResources().getDisplayMetrics().widthPixels / density) - (50);
                for (int i = 0; i < GameState._lives; i++) {
                    canvas.drawBitmap(GameUtils.getImageType(150, Constants.PLAYER), counter * density, 0 * density, null);
                    counter = counter - 30;
                }

                //draw the parachute captured count
                counter = (getResources().getDisplayMetrics().widthPixels / density) - (50);
                for (int i = 0; i < GameState.sParachutePickupCount; i++) {
                    canvas.drawBitmap(GameUtils.getImageType(0, Constants.PARACHUTE_SMALL), counter * density, 40 * density, null);
                    counter = counter - 30;
                }

                // draw player missle count
                int missileCountOnes = GameState._weaponList.get(0).getMissileCount() % 10;
                int missileCountTens = GameState._weaponList.get(0).getMissileCount() / 10;
                counter = 0;
                // draw 10s missle count
                for (int i = 0; i < missileCountTens; i++) {
                    canvas.drawBitmap(GameUtils.getImageType(90, Constants.MISSILE), counter * density, 0 * density, null);
                    counter = counter + 8;
                }
                // draw ones missile count
                for (int i = 0; i < missileCountOnes; i++) {
                    canvas.drawBitmap(GameUtils.getImageType(90, Constants.MISSILE_SMALL), counter * density, 3 * density, null);
                    counter = counter + 8;
                }

                //draw the controller circle
                int directionControllerSize = height / 2;
                mControllerCircleX = getContext().getResources().getDisplayMetrics().widthPixels / 2;
                mControllerCircleY = getContext().getResources().getDisplayMetrics().heightPixels - (height / 2);
                canvas.drawCircle(mControllerCircleX, mControllerCircleY, directionControllerSize, mControllerColor);

                // draw the controller direction circle
                canvas.drawCircle(mControllerCircleX + mControllerX, mControllerCircleY + mControllerY, 15 * density, mGunPlayer);

                //draw the special weapon circle
                int specialWeaponSize = height / 4;
                mSpecialWeaponX = (int) (getContext().getResources().getDisplayMetrics().widthPixels * .85);
                mSpecialWeaponY = getContext().getResources().getDisplayMetrics().heightPixels - (height / 2);
                canvas.drawCircle(mSpecialWeaponX, mSpecialWeaponY, specialWeaponSize, mControllerColor);
                int spriteXSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.MISSILE).getWidth();
                int spriteYSize = GameUtils.getImageType(me.getCurrentDirection(), Constants.MISSILE).getHeight();
                canvas.drawBitmap(GameUtils.getImageType(90, Constants.MISSILE)
                        , (int) (mSpecialWeaponX - (spriteXSize / 2))
                        , (int) (mSpecialWeaponY - (spriteYSize / 2))
                        , null);

                // game over, game title and high score list
                if (GameState._weaponList.get(0).getWeaponName() != (Constants.PLAYER)) {
                    canvas.drawText(getContext().getString(R.string.timefighters)
                            , centerXCanvas - (100 * density)
                            , centerYCanvas - (160 * density)
                            , mTextColor);
                    canvas.drawText(getContext().getString(R.string.mfr_name)
                            , centerXCanvas - (140 * density)
                            , centerYCanvas - (130 * density)
                            , mTextColor);
                    if (GameState._highScores != null) {
                        int y = centerYCanvas - (100 * density);

                        canvas.drawText(getResources().getString(R.string.score)
                                , centerXCanvas - (140 * density)
                                , y
                                , mHighScoreColor);
                        canvas.drawText(getResources().getString(R.string.level)
                                , centerXCanvas - (140 * density) + (80 * density)
                                , y
                                , mHighScoreColor);
                        canvas.drawText(getResources().getString(R.string.wave)
                                , centerXCanvas - (140 * density) + (130 * density)
                                , y
                                , mHighScoreColor);
                        canvas.drawText(getResources().getString(R.string.time)
                                , centerXCanvas - (140 * density) + (180 * density)
                                , y
                                , mHighScoreColor);
                        y = y + (20 * density);
                        for (Score score : GameState._highScores) {
                            canvas.drawText("" + score.getScore()
                                    , centerXCanvas - (140 * density)
                                    , y
                                    , mHighScoreColor);
                            canvas.drawText("" + GameUtils.getGameLevelNameString(getResources(), score.getLevel())
                                    , centerXCanvas - (140 * density) + (80 * density)
                                    , y
                                    , mHighScoreColor);
                            canvas.drawText("" + (score.getWave() + 1)
                                    , centerXCanvas - (140 * density) + (145 * density)
                                    , y
                                    , mHighScoreColor);
                            Date date = new Date(score.getTimeStamp());
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
                            canvas.drawText(sdf.format(date)
                                    , centerXCanvas - (140 * density) + (180 * density)
                                    , y
                                    , mHighScoreColor);
                            y = y + (20 * density);
                        }
                    }
                    canvas.drawText(getContext().getString(R.string.title)
                            , centerXCanvas - (140 * density)
                            , centerYCanvas + (130 * density)
                            , mTextColor);
                    canvas.drawText(getContext().getString(R.string.gameover)
                            , centerXCanvas - (60 * density)
                            , centerYCanvas + (160 * density)
                            , mTextColor);

                }
                //increment frame counter
                GameState.sFrame = GameState.sFrame + 1;
                if (GameState.sFrame > Constants.sMaxFrame) {
                    GameState.sFrame = 1;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        public void stopAsteroidViewThread() {
            Log.d(TAG, "stopped asteroid view thread");
            mIsRunning = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created    	
        Log.w(this.getClass().getName(), "surface created, starting main game loop");
        if (mAsteroidViewThread.isAlive() == false) {
            mAsteroidViewThread.mIsRunning = true;
            try
            {
                mAsteroidViewThread.start();
                new MainLoop(getContext().getResources().getDisplayMetrics().density, getContext());
                GameState.sShownLevelName = false;
            }
            catch(Exception e)
            {
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(TAG, "surfaceDestroyed() called");

        // stop canvas updater
		mAsteroidViewThread.stopAsteroidViewThread();

		//kill game update thread
		GameState.mIsRunning = false;

        //clear all state.
        GameState.clearAll();

		//no playfield, kill all audio streams, will come back when user returns. 
		AudioPlayer.pauseAll();
	}
	
	/**
	 * return the closest target that is not currently tracked by other missiles. 
	 * @return
	 */
	public MovementEngine findClosestTarget()
	{
		Set <MovementEngine> alreadyTrackedTargets = new HashSet <MovementEngine> ();
		
		//get all targets that are currently being tracked by missiles
		for(int i = 0; i < GameState._weaponList.size(); i ++) {
			try {
				MovementEngine weapon = GameState._weaponList.get(i);
				if (weapon.getWeaponName() == Constants.MISSILE_PLAYER) {
					alreadyTrackedTargets.add(((Missile) weapon).getTarget());
				}

			} catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG, e.getMessage(), e);
			}
		}
		
		int current = Integer.MAX_VALUE;
		MovementEngine closestEngine = null;
		
		// get the closest non tracked target
		for(int i = 0; i < GameState._weaponList.size(); i ++)
		{
			try
			{
				MovementEngine weapon = GameState._weaponList.get(i);
				if (weapon != null && alreadyTrackedTargets.contains(weapon) == false)
				{
					if (weapon.getWeaponName() == Constants.ENEMY_FIGHTER
							|| weapon.getWeaponName() == Constants.GUN_ENEMY
							|| weapon.getWeaponName() == Constants.MISSILE_ENEMY)
					{
						int range = GameUtils.getRange(GameState._weaponList.get(0), weapon);
						if (range < current)
						{
							current = range;
							closestEngine = weapon;
						}
					}
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
                e.printStackTrace();
				// ignore and continue;
			}
				
		}
		return closestEngine;
	}

	@Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
		int count = motionEvent.getPointerCount();
		// get the direction and special weapon motion events if exists.
		for (int countIndex = 0; countIndex < count; countIndex++) {
			int x = 0;
			int y = 0;

			int density = (int) getContext().getResources().getDisplayMetrics().density;

			x = (int) MotionEventCompat.getX(motionEvent, countIndex);
			y = (int) MotionEventCompat.getY(motionEvent, countIndex);

			double rangeController = GameUtils.getRangeBetweenCoords(mControllerCircleX, mControllerCircleY, x, y);
			double rangeSpecialWeapon = GameUtils.getRangeBetweenCoords(mSpecialWeaponX, mSpecialWeaponY, x, y);

			// process the events! 
			if (rangeController <= (100 * density)) {
				x = x - mControllerCircleX;
				y = y - mControllerCircleY;
				mControllerX = x;
				mControllerY = y;
				int track = _degrev[(int) GameUtils.track(0, 0, x, y)];
				GameState._weaponList.get(0).setDirection(track);
			}
			if (rangeSpecialWeapon <= (50 * density)) {
				if ((GameState._weaponList.get(0).getWeaponName() != (Constants.PLAYER))) {
                    //stop boss sounds
                    AudioPlayer.stopBoss();

					//restart game
                    GameState.sCurrentGameState = Constants.CURRENT_GAME_STATE_RUNNING;
					GameState._lives = 2;
					GameState._playerBulletsShot = 0;
					GameState._playerEnemyShot = 0;
					GameState._playerScore = 0;
                    GameState.sCurrentLevel = 1;
                    GameState.sWaveLevel = 0;
					GameState.sParachutePickupCount = 0;
					GameState.sBossHitPoints = Constants.BOSS_STARTING_HITPOINT;
                    GameState.sShownLevelName = false;

					MovementEngine player = new PlayerFighter(0, 0, 0d, 0d, 2.5d, 2.5d, 5, .1d, 0, Constants.PLAYER, -1, mGameStateListener);
					player.setMissileCount(Constants.START_MISSILE_COUNT);

                    //reset timers
                    GameState.sStartTimeBoss = System.currentTimeMillis();

                    //resume player gun sound
					// AudioPlayer.resumePlayerGun();

                    //start player gun sound here.
                    //if (GameState._weaponList.get(0).getWeaponName() == Constants.PLAYER) {
                        AudioPlayer.playPlayerGun();
                    //}

					//wipe the weapon list out
					GameState._weaponList.clear();

					//wipe the explosion list out
					GameState._explosionParticleList.clear();

					//add player to weapon list
					GameState._weaponList.add(player);
				} else {
					//launch missiles
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime - mMissileLastLaunch;
					if (GameState._weaponList.get(0).getDestroyedFlag() == false) {
						if (timeDiff > 250 && GameState._weaponList.get(0).getMissileCount() > 0) {
							// subtract one from missile count
							GameState._weaponList.get(0).decrementMissileCount();

							mMissileLastLaunch = currentTime;
							for (int z = 0; z < 4; z++) {
								// find closest target
								MovementEngine closestTarget = findClosestTarget();
								int targetTrack = (int) (GameUtils.getTargetTrack(GameState._weaponList.get(0), closestTarget));

								// once the closest target is selected, launch the missile. 
								if (closestTarget != null && targetTrack != -1) {
									MovementEngine missile = new Missile(targetTrack
											, targetTrack
											, (int) GameState._weaponList.get(0).getX(), (int) GameState._weaponList.get(0).getY(), .01, 10, .1, 1
											, Constants.MISSILE_PLAYER, closestTarget, GameState._weaponList.get(0), 250);
									GameState._weaponList.add(missile);
								}
							}
							if (GameState._muted == false) {
								AudioPlayer.playMissileSound();
							}
						}
					}
				}
			}
		}
		return true;
	}
}
