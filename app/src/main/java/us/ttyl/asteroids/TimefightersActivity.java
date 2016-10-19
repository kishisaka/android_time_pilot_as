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

/**
 * Created by test2 on 9/13/2015.
 */
public class TimefightersActivity  extends FragmentActivity {

    private static String TAG = "TimeFighterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem invulnerabilityItem = menu.getItem(0);
        String invulnerabilityString = getString(R.string.invulnerability);
        String invulnerabilityStringSetting = getString(R.string.on);
        if (GameState.sIsInvulnerable == true)
        {
            invulnerabilityStringSetting = getString(R.string.off);
        }
        invulnerabilityItem.setTitle(String.format(invulnerabilityString, invulnerabilityStringSetting));

        MenuItem enemyFire = menu.getItem(1);
        String enemyFireString = getString(R.string.enemy_fire);
        String enemyFireStringSetting = getString(R.string.on);
        if (GameState.sIsFireEnemyGuns == true)
        {
            enemyFireStringSetting = getString(R.string.off);
        }
        enemyFire.setTitle(String.format(enemyFireString, enemyFireStringSetting));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.invulnerarbility) {
//            if (GameState.sIsInvulnerable == true)
//            {
//                GameState.sIsInvulnerable = false;
//            }
//            else
//            {
//                GameState.sIsInvulnerable = true;
//            }
//            String invulnerabilityString = getString(R.string.invulnerability);
//            String invulnerabilityStringSetting = getString(R.string.off);
//            if (GameState.sIsInvulnerable == false)
//            {
//                invulnerabilityStringSetting = getString(R.string.on);
//            }
//            item.setTitle(String.format(invulnerabilityString, invulnerabilityStringSetting));
//        }
//        if (id == R.id.enemy_fire)
//        {
//            if (GameState.sIsFireEnemyGuns == true)
//            {
//                GameState.sIsFireEnemyGuns = false;
//            }
//            else
//            {
//                GameState.sIsFireEnemyGuns = true;
//            }
//            String enemyFireString = getString(R.string.enemy_fire);
//            String enemyFireStringSetting = getString(R.string.off);
//            if (GameState.sIsFireEnemyGuns == false)
//            {
//                enemyFireStringSetting = getString(R.string.on);
//            }
//            item.setTitle(String.format(enemyFireString, enemyFireStringSetting));
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container, new TimeFightersFragment()).commit();
//        return false;
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
