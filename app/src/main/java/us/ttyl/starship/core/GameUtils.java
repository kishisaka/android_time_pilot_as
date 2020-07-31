package us.ttyl.starship.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import us.ttyl.asteroids.R;
import us.ttyl.starship.movement.MovementEngine;

public class GameUtils {

    private static final String TAG = "GameUtils";

    /**
     * get a deg track of a set of coords from a set of coords
     *
     * @param centerX
     * @param centerY
     * @param targetX
     * @param targetY
     * @return
     */
    public static double track(double centerX, double centerY, double targetX, double targetY) {
        double x = getA(centerX, targetX);
        double y = getB(centerY, targetY);
        return track(x, y);
    }

    /**
     * get a deg track of a target from an origin
     *
     * @param origin
     * @param target
     * @return
     */
    public static double getTargetTrack(MovementEngine origin, MovementEngine target) {
        if (origin != null && target != null) {
            return track(origin.getX(), origin.getY(), target.getX(), target.getY());
        }
        return -1;
    }

    public static double getRange(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }

    public static double getA(double centerX, double targetX) {
        return (centerX * -1) + targetX;
    }

    public static double getB(double centerY, double targetY) {
        return (centerY * -1) + targetY;
    }

    /**
     * get an x,y coord given a track (in deg) and a distance vector
     *
     * @param track
     * @param distance
     * @return x/y coord (double[2])
     */
    public static double[] getCoordsGivenTrackAndDistance(int track, int distance) {
        if (track == 90) {
            return new double[]{0, distance};
        }
        if (track == 180) {
            return new double[]{-1 * distance, 0};
        }
        if (track == 270) {
            return new double[]{0, -1 * distance};
        }
        if (track == 360) {
            return new double[]{distance, 0};
        }

        double[] coord = new double[2];
        coord[0] = Math.cos(Math.toRadians(track)) * distance;
        coord[1] = Math.sin(Math.toRadians(track)) * distance;
        return coord;
    }

    /**
     * returns deg given a x and y
     *
     * @param x
     * @param y
     * @return
     */
    private static double track(double x, double y) {
        double returnDeg = 0;

        if (x > 0 && y > 0) {
            returnDeg = Math.toDegrees(Math.atan(y / x));
        }

        if (x < 0 && y > 0) {
            double convertX = x * -1;
            double deg = 180 - (Math.toDegrees(Math.atan(y / convertX)) + 90);
            returnDeg = deg + 90;
        }

        if (x < 0 && y < 0) {
            double convertX = x * -1;
            double deg = 180 - (Math.toDegrees(Math.atan(y / convertX)) + 90);
            returnDeg = deg + 90;
        }

        if (x > 0 && y < 0) {
            double convertY = y * -1;
            double deg = 180 - (Math.toDegrees(Math.atan(convertY / x)) + 90);
            returnDeg = deg + 270;
        }

        return returnDeg;
    }

    /**
     * get range between 2 ships
     *
     * @param origin
     * @param target
     * @return range to target from origin
     */
    public static int getRange(MovementEngine origin, MovementEngine target) {
        double xFactor = (target.getX() - origin.getX()) * (target.getX() - origin.getX());
        double yFactor = (target.getY() - origin.getY()) * (target.getY() - origin.getY());
        return (int) Math.sqrt(xFactor + yFactor);
    }

    /**
     * get range between 2 coordinates on the plain
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static int getRangeBetweenCoords(double x1, double y1, double x2, double y2) {
        double xFactor = (x2 - x1) * (x2 - x1);
        double yFactor = (y2 - y1) * (y2 - y1);
        return (int) Math.sqrt(xFactor + yFactor);
    }

    /**
     * get ship type count (gun, enemy)
     *
     * @param name
     * @return ship count
     */
    public static int getTypeCount(int name, List<MovementEngine> itemList) {
        int count = 0;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getWeaponName() == (name)) {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * loads sprites from file (15 x 9), 8 and 9 are clouds
     *
     * @return ArrayList of sprites (BufferedImages)
     */
    public static ArrayList<Bitmap> getTilesFromFile(Context context, int drawableId) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        try {
            Bitmap tileMap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            for (int y = 0; y < 15; y++) {
                for (int x = 0; x < 12; x++) {
                    int density = (int) context.getResources().getDisplayMetrics().density;
                    tileList.add(Bitmap.createBitmap(tileMap, x * density * 36, y * density * 36, density * 36, density * 36));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return tileList;
    }

    public static void getOther(Context context) {
        GameState._bigParachute = BitmapFactory.decodeResource(context.getResources(), R.drawable.other_parachute_big);
        GameState._smallParachute = BitmapFactory.decodeResource(context.getResources(), R.drawable.other_parachute_small);
        GameState._smallMissile = BitmapFactory.decodeResource(context.getResources(), R.drawable.other_player_missile_indicator);
    }

    public static ArrayList<Bitmap>getPlayerSprites(Context context) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_3));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_0));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_11));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_10));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_9));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_8));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_7));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_6));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_5));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_player_4));
        return tileList;
    }

    public static ArrayList<Bitmap>getFighters1917FromFile(Context context, int id) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        if (id == 1) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_fighter_4));
        }
        if (id == 2) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_fighter_4));
        }
        return tileList;
    }

    public static ArrayList<Bitmap>getFighters1941FromFile(Context context, int id) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        if (id == 1) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_fighter_4));
        }
        if (id == 2) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_fighter_4));
        }
        return tileList;
    }

    public static ArrayList<Bitmap>getFighters1971FromFile(Context context, int id) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        if (id == 1) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_fighter_4));
        }
        if (id == 2) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_fighter_4));
        }
        return tileList;
    }

    public static ArrayList<Bitmap>getFighters1984FromFile(Context context, int id) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        if (id == 1) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_fighter_4));
        }
        if (id == 2) {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_3));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_2));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_1));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_0));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_11));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_10));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_9));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_8));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_7));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_6));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_5));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_fighter_4));
        }
        return tileList;
    }

    /**
     * load boss ship sprites, row 8, 72 x 36
     *
     * @param context
     * @return
     */
    public static ArrayList<Bitmap> getBossTilesFromFile(Context context) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1917_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1941_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1971_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_1984_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1917_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1941_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1971_boss_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_boss_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite2_1984_boss_2));
        return tileList;
    }

    /**
     * get the explosion sprites (50 x 50, 24 frames)
     * @param context
     * @return
     */
    public static ArrayList<Bitmap> getAnimatedExplosionSprites(Context context) {
        ArrayList<Bitmap> explosionList = new ArrayList<Bitmap>();
        try {
            int width = 150;
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_1a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_1a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_1a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_1a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_2a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_2a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_2a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_2a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_3a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_3a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_3a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_3a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_4a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_4a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_4a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_4a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_5a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_5a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_5a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_5a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_6a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_6a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_6a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_6a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_7a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_7a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_7a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_7a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_8a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_8a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_8a, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.explosion_8a, context, width));
        }
        catch(Exception e) {
            Log.e(TAG, "", e);
        }
        return explosionList;
    }

    /**
     * get the explosion sprites (400 x 400, 24 frames)
     * @param context
     * @return
     */
    public static ArrayList<Bitmap> getAnimatedExplosionSpritesBoss(Context context) {
        ArrayList<Bitmap> explosionList = new ArrayList<Bitmap>();
        try {
            int width = 400;
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_1, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_1, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_2, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_2, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_3, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_3, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_4, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_4, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_5, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_5, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_6, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_6, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_7, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_7, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_8, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_8, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_9, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_9, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_10, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_10, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_11, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_11, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_12, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_12, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_13, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_13, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_14, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_14, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_15, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_15, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_16, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_16, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_17, context, width));
            explosionList.add(getAndScaleBitmap(R.drawable.boss_explosion_17, context, width));
        }
        catch(Exception e) {
            Log.e(TAG, "", e);
        }
        return explosionList;
    }

    public static List<Bitmap> getMissileBitmaps(Context context) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_3));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_2));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_1));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_0));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_11));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_10));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_9));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_8));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_7));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_6));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_5));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite1_missile_4));
        return tileList;
    }

    public static List<Bitmap> getMapTiles(Context context) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.water));
        tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.land));
        return tileList;
    }

    private static int [][] section = new int[6][6];
    private static int [][] world = new int[][]{
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    };
    /**
     * return a 6x6 piece of the world
     * @param x
     * @param y
     * @return
     */
    public static Bitmap getWorldAsBitmap(Context context, int centerX, int centerY) {
        Bitmap bitmap = Bitmap.createBitmap(6*30, 6*30, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float currentX = 0;
        float currentY = 0;
        for(int y = 0; y < 5; y ++) {
            for(int x = 0; x < 5; x ++) {
                Bitmap tile =  GameState._mapTiles.get(world[x][y]);
                canvas.drawBitmap(tile, currentX * 30, currentY * 30, null);
            }
        }
        return bitmap;
    }

    /**
     * get cloud sprites (small 36 x 36, large 144 x 72)
     *
     * @param context
     * @return
     */
    public static ArrayList<Bitmap> getCloudTiles(Context context) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        try {
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.other_cloud_small));
            tileList.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.other_cloud_big));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return tileList;
    }

    public static Bitmap getBossBullet(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.other_boss_bullet);
    }

    private static Bitmap getAndScaleBitmap(int bitmapId, Context context, int width) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapId);
        return Bitmap.createScaledBitmap(bitmap, width, width, false);
    }
    /**
     * given a track and a ship type, return the appropriate sprite for rendering
     *
     * 6103458
     * @param track (can be either track or endurance)
     * @param type
     * @return sprite (BufferedImage)
     */
    public static Bitmap getImageType(int track, int type) {
        int frame = GameState.sFrame;
        //explosions are special, the track is actually the endurance of the explosion
        if (type == Constants.ANIMATED_EXPLOSION) {
            return GameState._explosionSprites.get(track);
        }
        if (type == Constants.ANIMATED_BOSS_EXPLOSION) {
            return GameState._explosionBossSprites.get(track);
        }
        if (type == Constants.CLOUD_SMALL) {
            return GameState._cloudSprites.get(0);
        }
        if (type == Constants.CLOUD_BIG) {
            return GameState._cloudSprites.get(1);
        } else if (type == Constants.PARACHUTE_SMALL) {
            return GameState._smallParachute;
        } else if (type == Constants.MISSILE_SMALL) {
            return GameState._smallMissile;
        } else if (type == Constants.PARACHUTE) {
            return GameState._bigParachute;
        } else if (type == Constants.ENEMY_BOSS && track == 0) {
            if (frame == 1) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(0);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(2);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(4);
                } else {
                    return GameState._bossSprites1.get(6);
                }
            } else {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(8);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(10);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(12);
                } else {
                    return GameState._bossSprites1.get(14);
                }
            }
        } else if (type == (Constants.ENEMY_BOSS) && track == 180) {
            if (frame == 1) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(1);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(3);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(5);
                } else {
                    return GameState._bossSprites1.get(7);
                }
            } else {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(9);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(11);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(13);
                } else {
                    return GameState._bossSprites1.get(15);
                }
            }
        } else if (track >= 0 && track < 30) {
            return getFrameBitmap(frame, type, 0);
        } else if (track >= 30 && track < 60) {
            return getFrameBitmap(frame, type,1);
        } else if (track >= 60 && track < 90) {
            return getFrameBitmap(frame, type, 2);
        } else if (track >= 90 && track < 120) {
            return getFrameBitmap(frame, type, 3);
        } else if (track >= 120 && track < 150) {
            return getFrameBitmap(frame, type, 4);
        } else if (track >= 150 && track < 180) {
            return getFrameBitmap(frame, type, 5);
        } else if (track >= 180 && track < 210) {
            return getFrameBitmap(frame, type, 6);
        } else if (track >= 210 && track < 240) {
            return getFrameBitmap(frame, type, 7);
        } else if (track >= 240 && track < 270) {
            return getFrameBitmap(frame, type, 8);
        } else if (track >= 270 && track < 300) {
            return getFrameBitmap(frame, type, 9);
        } else if (track >= 300 && track < 330) {
            return getFrameBitmap(frame, type, 10);
        } else if (track >= 330 && track < 360) {
            return getFrameBitmap(frame, type, 11);
        }
        return null;
    }

    public static void getBoltBitmaps(Context context) {
        Bitmap bolt = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.bolt);
        bolt = bolt.createScaledBitmap(bolt, (int)(10 * context.getResources().getDisplayMetrics().density) , (int)(10 * context.getResources().getDisplayMetrics().density), false);
        GameState._bolts = rotateBitmaps(context,bolt);
    }

    public static void getEnemyBoltBitmaps(Context context) {
        Bitmap bolt = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.enemy_bolt);
        bolt = bolt.createScaledBitmap(bolt, (int)(10 * context.getResources().getDisplayMetrics().density) , (int)(10 * context.getResources().getDisplayMetrics().density), false);
        GameState._enemy_bolts = rotateBitmaps(context,bolt);
    }

    private static List<Bitmap> rotateBitmaps(Context context, Bitmap bitmap) {
        List<Bitmap> bitmapList = new ArrayList<Bitmap>();
        for(int i = 360; i > -1; i --) {
            bitmapList.add(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix(bitmap, i), false));
        }
        bitmap.recycle();
        bitmap = null;
        return bitmapList;
    }

    private static List<Bitmap> rotateRotorBitmaps(Context context, Bitmap bitmap) {
        List<Bitmap> bitmapList = new ArrayList<Bitmap>();
        for(int i = 10; i > -1; i --) {
            bitmapList.add(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix(bitmap, i), false));
        }
        bitmap.recycle();
        bitmap = null;
        return bitmapList;
    }

    private static Matrix rotateMatrix(Bitmap bitmap, int rotation) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(rotation, bitmap.getWidth()/2, bitmap.getHeight()/2);
        rotateMatrix.postTranslate(bitmap.getWidth(), bitmap.getHeight());
        return rotateMatrix;
    }


    public static Bitmap getFrameBitmap(int frame, int type, int direction) {
        if (type == (Constants.PLAYER)) {
            return GameState._playerSprites.get(direction);
        }
        if (type == (Constants.MISSILE)) {
            return GameState._missileSprites.get(direction);
        }
        if (frame < 3) {
            if (type == (Constants.ENEMY_FIGHTER)) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._1917_fighters_1.get(direction);
                }
                if (GameState.sCurrentLevel == 2) {
                    return GameState._1941_fighters_1.get(direction);
                }
                if (GameState.sCurrentLevel == 3) {
                    return GameState._1971_fighters_1.get(direction);
                }
                if (GameState.sCurrentLevel == 4) {
                    return GameState._1984_fighters_1.get(direction);
                }
            }
        } else {
            if (type == (Constants.ENEMY_FIGHTER)) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._1917_fighters_2.get(direction);
                }
                if (GameState.sCurrentLevel == 2) {
                    return GameState._1941_fighters_2.get(direction);
                }
                if (GameState.sCurrentLevel == 3) {
                    return GameState._1971_fighters_2.get(direction);
                }
                if (GameState.sCurrentLevel == 4) {
                    return GameState._1984_fighters_2.get(direction);
                }
            }
        }
        return null;
    }
	/**
	 * given a missle track, return the appropriate x,y offset for the smoke
	 * @param track
	 * @return sprite (BufferedImage)
	 */
	public static int[] getSmokeTrailXY(int track)
	{
		if (track >= 0 && track < 30)
		{
			// +3
			return new int[]{-9, 0};
		}
		else if (track >= 30 && track < 60)
		{
			// + 2
			return new int[]{-8, 5};
		}

		else if (track >= 60 && track < 90)
		{
			// + 1
			return new int[]{-4,7};
		}
		else if (track >= 90 && track < 120)
		{
			// 0
			return new int[]{0,9};
		}
		else if (track >= 120 && track < 150)
		{
			// + 11
			return new int[]{4,7};
		}
		else if (track >= 150 && track < 180)
		{
			// + 10
			return new int[]{7,4};
		}
		else if (track >= 180 && track < 210)
		{
			// + 9
			return new int[]{9,0};
		}
		else if (track >= 210 && track < 240)
		{
			// + 8
			return new int[]{7,-4};
		}
		else if (track >= 240 && track < 270)
		{
			// + 7
			return new int[]{5,-7};
		}
		else if (track >= 270 && track < 300)
		{
			// + 6
			return new int[]{0,-9};
		}
		else if (track >= 300 && track < 330)
		{
			// + 5
			return new int[]{-4,-7};
		}
		else if (track >= 330 && track < 360)
		{
			// + 4
			return new int[]{-7,-4};
		}
		return new int[]{0,0};
	}

	/**
	 * return the enemy gun fire rate based on the current wave level
	 * increases difficulty for game
	 * @return fire rate interval in ms
	 */
	public static int getEnemyGunFireRate()
	{
		if (GameState.sWaveLevel == 0)
		{
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    return 470;
                case 3:
                    return 450;
                case 4:
                    return 430;
                default:
                    return 500;
            }
		}
		else if (GameState.sWaveLevel == 1)
		{
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    return 380;
                case 3:
                    return 370;
                case 4:
                    return 360;
                default:
                    return 400;
            }
		}
		else if (GameState.sWaveLevel == 2)
		{
			return 300;
		}
		return 200;
	}

	/**
	 * return the enemy missile fire rate based on the current wave level
	 * @return fire rate interval in ms
	 */
	public static int getEnemyMissileFireRate()
	{
		if (GameState.sWaveLevel == 0)
		{
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    // no fire unless user takes too long!
                    return 90000;
                case 3:
                    return 2000;
                case 4:
                    return 1700;
                default:
                    // no fire unless user takes too long!
                    return 90000;
            }
		}
		else if (GameState.sWaveLevel == 1)
		{
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    // no fire unless user takes too long!
                    return 90000;
                case 3:
                    return 1000;
                case 4:
                    return 1000;
                default:
                    // no fire unless user takes too long!
                    return 90000;
            }
		}
		else if (GameState.sWaveLevel == 2)
		{
			return 800;
		}
		return 500;
	}

	/**
	 * should we fire a particular weapon or should we not based on the current wave level
	 * used for the boss gun and missile for fighters.
	 * @return true for fire, false for not fire
	 */
	public static boolean shouldFireWeapon()
	{
		int randomFireFactor = 1;
		if (GameState.sWaveLevel == 2)
		{
			randomFireFactor = 3;
		}
		else if (GameState.sWaveLevel > 2)
		{
			randomFireFactor = 10;
		}
		if (Math.random() * 100 < randomFireFactor)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

    public static int getFighterCount() {
        if (GameState.sWaveLevel == 0)
        {
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    return 6;
                case 3:
                    return 7;
                case 4:
                    return 8;
                default:
                    return 5;
            }
        }
        else
        {
            switch(GameState.sCurrentLevel)
            {
                case 2:
                    return 9;
                case 3:
                    return 10;
                case 4:
                    return 11;
                default:
                    return 8;
            }
        }
    }

    public static int getBossHitpoints() {
	    switch(GameState.sWaveLevel) {
            case 0:
                return Constants.BOSS_STARTING_HITPOINT_0;
            case 1:
                return Constants.BOSS_STARTING_HITPOINT_1;
            case 2:
                return Constants.BOSS_STARTING_HITPOINT_2;
            default:
                return Constants.BOSS_STARTING_HITPOINT_3_PLUS;
        }
    }
	/**
	 * get the current enemy boss speed based on current and wave leveels
	 * @param weaponType
	 * @return speed
	 */
	public static float getPlaneSpeed(int weaponType)
	{
		if (weaponType == Constants.ENEMY_BOSS)
		{
			if (GameState.sWaveLevel == 0)
			{
				switch(GameState.sCurrentLevel)
				{
					case 2:
						return 2;
					case 3:
						return 2.1f;
					case 4:
						return 2.2f;
					default:
						return 1;
				}
			}
			else
			{
				switch(GameState.sCurrentLevel)
				{
					case 2:
						return 3;
					case 3:
						return 4;
					case 4:
						return 5;
					default:
						return 2.25f;
				}
			}
		}
		else if (weaponType == Constants.ENEMY_FIGHTER)
		{
			if (GameState.sWaveLevel == 0)
			{
				switch(GameState.sCurrentLevel)
				{
					case 2:
						return 1.4f;
					case 3:
						return 1.75f;
					case 4:
						return 2.2f;
					default:
						return 1;
				}
			}
			else
			{
				switch(GameState.sCurrentLevel)
				{
					case 2:
						return 2.3f;
					case 3:
						return 2.4f;
					case 4:
						return 4;
					default:
						return 2.3f;
				}
			}
		}
		return 1;
	}

    public static String getGameLevelName(Resources res)
    {
        switch (GameState.sCurrentLevel)
        {
            case 1:
                return getGameLevelName(res, 1);
            case 2:
                return getGameLevelName(res, 2);
            case 3:
                return getGameLevelName(res, 3);
            case 4:
                return getGameLevelName(res, 4);
            default:
                return "";
        }
    }

    public static String getGameLevelName(Resources res, int level)
    {
        switch (level)
        {
            case 1:
                return res.getString(R.string.time_1917);
            case 2:
                return res.getString(R.string.time_1943);
            case 3:
                return res.getString(R.string.time_1972);
            case 4:
                return res.getString(R.string.time_1991);
            default:
                return "";
        }
    }

    public static String getGameLevelNameString(Resources res, int level)
    {
        switch (level)
        {
            case 1:
                return res.getString(R.string.string_1917);
            case 2:
                return res.getString(R.string.string_1941);
            case 3:
                return res.getString(R.string.string_1972);
            case 4:
                return res.getString(R.string.string_1991);
            default:
                return "";
        }
    }

    /**
     * used only for landmass placement
     * @param x
     * @return
     */
    public int wrap(int x) {
        if (x > 10000) {
            return 0;
        } else if (x < 0) {
            return 10000;
        } else {
            return x;
        }
    }
}
