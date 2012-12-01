/* 
 * Date: MenuActivity.java created on Aug 28, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.activities;

import ro.zuvasoft.derdiedas.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class should be extended by other activities which want to have the same
 * menu available. The class holds the main menu for the entire application.
 * 
 * @author mIRCEA
 */
public abstract class MenuActivity extends Activity {
    private AlertDialog resetDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        resetDialog =  createResetDialog();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.pref: {
            Intent prefIntent = new Intent(this, PreferencesActivity.class);
            startActivity(prefIntent);

            return true;
        }
        case R.id.resetCounter: {
            if (resetDialog != null) {
                resetDialog.show();
            }
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public abstract void resetCounter();

    public AlertDialog createResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.resetScoreTitle).setMessage(R.string.resetScoreMessage)
                .setPositiveButton(R.string.yes, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetCounter();
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.no, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.setOwnerActivity(this);
        return dialog;
    }
}
