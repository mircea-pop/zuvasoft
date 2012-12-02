/* 
 * Date: PreferencesActivity.java created on Aug 28, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.activities;

import ro.zuvasoft.derdiedas.R;
import ro.zuvasoft.derdiedas.pref.Preferences;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

/**
 * @author mIRCEA
 */
public class PreferencesActivity extends Activity {
    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_pref);
       
        initPreferences(this);
    }

    private void initPreferences(PreferencesActivity preferencesActivity) {
        pref = new Preferences(this);
        CheckBox tryAgainCheckBox = (CheckBox) findViewById(R.id.checkbox_tryAgain);
        tryAgainCheckBox.setChecked(pref.isTryAgain());
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
        case R.id.checkbox_tryAgain:
            pref.setTryAgain(checked);
            break;
        }
    }
}
