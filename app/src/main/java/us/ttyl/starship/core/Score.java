package us.ttyl.starship.core;

import java.util.Calendar;

/**
 * Created by test on 5/14/2016.
 */
public class Score {
    int score;
    int wave;
    int level;
    long timeStamp;

    public int getScore() {
        return score;
    }
    public int getWave() {
        return wave;
    }
    public int getLevel() {
        return level;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public String toString() {
        return score + ":" + wave + ":" + level + ":" + timeStamp;
    }
    public String formattedScore() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        return score + "        " + wave + "    " + level + "    " + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" +cal.get(Calendar.MINUTE);
    }
}
