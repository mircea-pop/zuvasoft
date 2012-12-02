/* Created on Dec 2, 2012
/* Copyright e.Pages GmbH
 */

package ro.zuvasoft.derdiedas.pref;

import ro.zuvasoft.derdiedas.core.Constants;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author mpop (mircea.pop@epages.com)
 */
public class Preferences {

    private final String TRY_AGAIN_PROPERTY = "tryAgainProperty";
    private final SharedPreferences pref;

    public Preferences(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
    }

    public void setTryAgain(boolean tryAgain) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(TRY_AGAIN_PROPERTY, tryAgain);

        editor.commit();
    }

    public boolean isTryAgain() {
        return pref.getBoolean(TRY_AGAIN_PROPERTY, true);
    }
}
