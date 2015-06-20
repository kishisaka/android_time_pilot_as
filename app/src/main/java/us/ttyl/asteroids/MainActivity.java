package us.ttyl.asteroids;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import us.ttyl.starship.core.GameState;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
		int id = item.getItemId();
		if (id == R.id.invulnerarbility) {
			if (GameState.sIsInvulnerable == true)
			{
				GameState.sIsInvulnerable = false;
			}
			else
			{
				GameState.sIsInvulnerable = true;
			}
			String invulnerabilityString = getString(R.string.invulnerability);
			String invulnerabilityStringSetting = getString(R.string.off);
			if (GameState.sIsInvulnerable == false)
			{
				invulnerabilityStringSetting = getString(R.string.on);
			}
			item.setTitle(String.format(invulnerabilityString, invulnerabilityStringSetting));
		}
		if (id == R.id.enemy_fire)
		{
			if (GameState.sIsFireEnemyGuns == true)
			{
				GameState.sIsFireEnemyGuns = false;
			}
			else
			{
				GameState.sIsFireEnemyGuns = true;
			}
			String enemyFireString = getString(R.string.enemy_fire);
			String enemyFireStringSetting = getString(R.string.off);
			if (GameState.sIsFireEnemyGuns == false)
			{
				enemyFireStringSetting = getString(R.string.on);
			}
			item.setTitle(String.format(enemyFireString, enemyFireStringSetting));
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	  protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
	}

	  @Override
	  protected void onPause() {
	    // Be sure to unregister the sensor when the activity pauses.
	    super.onPause();
	  }
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
