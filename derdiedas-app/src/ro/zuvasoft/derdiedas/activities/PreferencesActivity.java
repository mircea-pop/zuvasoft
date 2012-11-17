/* 
 * Date: PreferencesActivity.java created on Aug 28, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.activities;

import ro.zuvasoft.derdiedas.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author mIRCEA
 */
public class PreferencesActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		setContentView(R.layout.activity_pref);

	}
}
