package us.ttyl.asteroids;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
    private static DBHelper dbHelper;
    private static Fragment timeFighterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            GameState.versionCode =  pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(this);
        }
        if (timeFighterFragment == null) {
            timeFighterFragment = new TimeFightersFragment();
        }
        GameState._highScore = dbHelper.getTopScore();
        GameState._highScores = dbHelper.getTop10Scores();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, timeFighterFragment).commit();
        try {
            AudioPlayer.resumeAll();
            Log.d(TAG, "we are started");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
