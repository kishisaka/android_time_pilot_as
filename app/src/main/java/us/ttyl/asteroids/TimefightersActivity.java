package us.ttyl.asteroids;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.DBHelper;
import us.ttyl.starship.core.GameState;

public class TimefightersActivity  extends FragmentActivity {

    private static String TAG = "TimeFighterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        GameState._highScore = helper.getTopScore();
        GameState._highScores = helper.getTop10Scores();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new TimeFightersFragment()).commit();
        try {
            AudioPlayer.resumeAll();
            Log.d(TAG, "we are started");
        }
        catch(Exception e) {
        }
    }

    @Override
    public void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        try {
            AudioPlayer.pauseAll();
            Log.d(TAG, "we are stopped");
        }
        catch(Exception e) {
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TimeFightersFragment extends Fragment {

        public TimeFightersFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            GameState.sScreenDensity = getResources().getDisplayMetrics().density;
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            return rootView;
        }
    }
}
