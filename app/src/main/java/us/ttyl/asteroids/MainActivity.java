package us.ttyl.asteroids;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
