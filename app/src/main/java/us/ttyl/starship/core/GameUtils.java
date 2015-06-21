package us.ttyl.starship.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import us.ttyl.asteroids.R;
import us.ttyl.starship.movement.MovementEngine;
import us.ttyl.starship.movement.ships.Parachute;

public class GameUtils {
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
            e.printStackTrace();
        }
        return tileList;
    }

    /**
     * load boss ship sprites, row 8, 72 x 36
     *
     * @param context
     * @return
     */
    public static ArrayList<Bitmap> getBossTilesFromFile(Context context, int drawableId) {
        ArrayList<Bitmap> tileList = new ArrayList<Bitmap>();
        try {
            Bitmap tileMap = BitmapFactory.decodeResource(context.getResources(), drawableId);
            int y = 8;
            for (int x = 0; x < 6; x++) {
                int density = (int) context.getResources().getDisplayMetrics().density;
                tileList.add(Bitmap.createBitmap(tileMap, x * density * 72, y * density * 36, density * 72, density * 36));
            }
            y = 14;
            for (int x = 0; x < 6; x++) {
                int density = (int) context.getResources().getDisplayMetrics().density;
                tileList.add(Bitmap.createBitmap(tileMap, x * density * 72, y * density * 36, density * 72, density * 36));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileList;
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
            Bitmap tileMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprites);
            int density = (int) context.getResources().getDisplayMetrics().density;
            tileList.add(Bitmap.createBitmap(tileMap, 0, 7 * density * 36, density * 36, density * 36));
            tileList.add(Bitmap.createBitmap(tileMap, 0, 9 * density * 36, density * 144, density * 72));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileList;
    }

    public static Bitmap getBossBullet(Context context) {
        Bitmap tileMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprites);
        int density = (int) context.getResources().getDisplayMetrics().density;
        return Bitmap.createBitmap(tileMap, 0, 13 * density * 36, density * 36, density * 36);
    }

    /**
     * given a track and a ship type, return the appropriate sprite for rendering
     *
     * @param track
     * @param type
     * @return sprite (BufferedImage)
     */
    public static Bitmap getImageType(int track, int type) {
        int frame = GameState.sFrame;
        int missileRow = 12 * 12;
        if (type == Constants.CLOUD_SMALL) {
            return GameState._cloudSprites.get(0);
        }
        if (type == Constants.CLOUD_BIG) {
            return GameState._cloudSprites.get(1);
        } else if (type == Constants.PARACHUTE_SMALL) {
            return GameState._sprites1.get((7 * 12) + 2);
        } else if (type == Constants.MISSILE_SMALL) {
            return GameState._sprites1.get((12 * 13) + 1);
        } else if (type == Constants.PARACHUTE) {
            return GameState._sprites1.get((7 * 12) + 1);
        } else if (type == Constants.ENEMY_BOSS && track == 0) {
            if (frame == 1) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(1);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(2);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(0);
                } else {
                    return GameState._bossSprites1.get(6);
                }
            } else {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites2.get(1);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites2.get(2);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites2.get(0);
                } else {
                    return GameState._bossSprites2.get(6);
                }
            }
        } else if (type == (Constants.ENEMY_BOSS) && track == 180) {
            if (frame == 1) {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites1.get(5);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites1.get(4);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites1.get(3);
                } else {
                    return GameState._bossSprites1.get(7);
                }
            } else {
                if (GameState.sCurrentLevel == 1) {
                    return GameState._bossSprites2.get(5);
                } else if (GameState.sCurrentLevel == 2) {
                    return GameState._bossSprites2.get(4);
                } else if (GameState.sCurrentLevel == 3) {
                    return GameState._bossSprites2.get(3);
                } else {
                    return GameState._bossSprites2.get(7);
                }
            }
        } else if (track >= 0 && track < 30) {
            return getFrameBitmap(frame, type, missileRow, 12, 3);
        } else if (track >= 30 && track < 60) {
            return getFrameBitmap(frame, type, missileRow, 12, 2);
        } else if (track >= 60 && track < 90) {
            return getFrameBitmap(frame, type, missileRow, 12, 1);
        } else if (track >= 90 && track < 120) {
            return getFrameBitmap(frame, type, missileRow, 12, 0);
        } else if (track >= 120 && track < 150) {
            return getFrameBitmap(frame, type, missileRow, 12, 11);
        } else if (track >= 150 && track < 180) {
            return getFrameBitmap(frame, type, missileRow, 12, 10);
        } else if (track >= 180 && track < 210) {
            return getFrameBitmap(frame, type, missileRow, 12, 9);
        } else if (track >= 210 && track < 240) {
            return getFrameBitmap(frame, type, missileRow, 12, 8);
        } else if (track >= 240 && track < 270) {
            return getFrameBitmap(frame, type, missileRow, 12, 7);
        } else if (track >= 270 && track < 300) {
            return getFrameBitmap(frame, type, missileRow, 12, 6);
        } else if (track >= 300 && track < 330) {
            return getFrameBitmap(frame, type, missileRow, 12, 5);
        } else if (track >= 330 && track < 360) {
            return getFrameBitmap(frame, type, missileRow, 12, 4);
        }
        return null;
    }

    public static Bitmap getFrameBitmap(int frame, int type, int missleRow, int row, int offset) {
        if (frame < 3) {
            if (type == (Constants.PLAYER)) {
                return GameState._sprites1.get(offset);
            } else if (type == (Constants.ENEMY_FIGHTER)) {
                return GameState._sprites1.get((GameState.sCurrentLevel * row) + offset);
            } else if (type == (Constants.MISSILE)) {
                return GameState._sprites1.get(missleRow + offset);
            }
        } else {
            if (type == (Constants.PLAYER)) {
                return GameState._sprites2.get(offset);
            } else if (type == (Constants.ENEMY_FIGHTER)) {
                return GameState._sprites2.get((GameState.sCurrentLevel * row) + offset);
            } else if (type == (Constants.MISSILE)) {
                return GameState._sprites2.get(missleRow + offset);
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
			return 500;
		}
		else if (GameState.sWaveLevel == 1)
		{
			return 400;
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
			return 1500;
		}
		else if (GameState.sWaveLevel == 1)
		{
			return 1000;
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
						return 2;
					case 4:
						return 3.5f;
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
						return 2;
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
						return 1.4f;
					case 4:
						return 3;
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
						return 2.3f;
					case 4:
						return 4;
					default:
						return 2;
				}
			}
		}
		return 1;
	}


}
