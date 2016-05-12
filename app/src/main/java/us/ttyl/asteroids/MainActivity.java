package us.ttyl.asteroids;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import us.ttyl.starship.core.AudioPlayer;
import us.ttyl.starship.core.GameState;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.title);
	}


	@Override
	public boolean onTouchEvent(MotionEvent motionEvent) {
		// start the game
		Intent intent = new Intent(this, TimefightersActivity.class);
		startActivity(intent);
		return true;
	}
	
	@Override
	protected void onResume()
    {
	    // Register a listener for the sensor.
		super.onResume();
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
	}
}
