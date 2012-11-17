/* 
 * Date: MenuActivity.java created on Aug 28, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.activities;

import ro.zuvasoft.derdiedas.R;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class should be extended by other activities which want to have the same
 * menu available. The class holds the main menu for the entire application.
 * 
 * @author mIRCEA
 */
public class MenuActivity extends Activity
{

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.pref :
			{
				Intent prefIntent = new Intent(this, PreferencesActivity.class);
				startActivity(prefIntent);

				return true;
			}
			default :
				return super.onOptionsItemSelected(item);
		}
	}
}
