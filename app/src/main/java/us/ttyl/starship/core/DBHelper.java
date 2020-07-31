package us.ttyl.starship.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * save the game scores here! saves score, time of game, level and wave achieved
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG ="DBHelper";

    private static final String DATABASE_NAME = "high_score";
    private static final int DATABASE_VERSION = 1;
    private static final String HIGHSCORE_TABLE = "highscore";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ HIGHSCORE_TABLE +" (" +
                    "id INTEGER PRIMARY KEY, " +
                    "score INTEGER ," +
                    "time LONG, "+
                    "level INTEGER, " +
                    "wave INTEGER " +
            " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS highscore";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // initalize the score DB
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * save a score to the DB
     * @param score
     * @param level
     * @param wave
     */
    public void writeScore(int score, int level, int wave) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("score", score);
            values.put("level", level);
            values.put("wave", wave);
            values.put("time", System.currentTimeMillis());
            db.insert(HIGHSCORE_TABLE, null, values);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * return top 10 highest scores from the DB
     * @return top 10 high sccres
     */
    public List<Score> getTop10Scores() {
        List<Score> scores = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        String[] select = {"score", "level", "wave", "time"};
        String sortOrder = "score DESC";
        String limit = "10";
        try {
            Cursor cursor = db.query(
                    HIGHSCORE_TABLE
                    , select
                    , null
                    , null
                    , null
                    , null
                    , sortOrder
                    , limit
            );
            while (cursor.moveToNext()) {
                Score score = new Score();
                score.score = cursor.getInt(0);
                score.level = cursor.getInt(1);
                score.wave = cursor.getInt(2);
                score.timeStamp = cursor.getLong(3);
                scores.add(score);
            }
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
        return scores;
    }

    /**
     * return the highest score
     * @return highest score
     */
    public Score getTopScore() {
        SQLiteDatabase db = getWritableDatabase();
        String[] select = {"score", "level", "wave", "time"};
        String sortOrder = "score DESC";
        String limit = "1";
        try {
            Cursor cursor = db.query(
                    HIGHSCORE_TABLE
                    , select
                    , null
                    , null
                    , null
                    , null
                    , sortOrder
                    , limit
            );
            cursor.moveToFirst();
            Score score = new Score();
            score.score = cursor.getInt(0);
            score.level = cursor.getInt(1);
            score.wave = cursor.getInt(2);
            score.timeStamp = cursor.getLong(3);
            return score;
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
