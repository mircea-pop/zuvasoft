/* Created on Dec 1, 2012
/* Copyright e.Pages GmbH
 */

package ro.zuvasoft.derdiedas.articlesubject;

import java.util.ArrayList;
import java.util.List;

import ro.zuvasoft.derdiedas.R;
import ro.zuvasoft.derdiedas.core.Constants.Article;
import android.app.Activity;
import android.widget.Button;

/**
 * @author mpop (mircea.pop@epages.com)
 */
public class ButtonController {
    private final List<Button> buttons = new ArrayList<Button>();

    public ButtonController(Activity activity) {
        buttons.add((Button) activity.findViewById(R.id.derButton));
        buttons.add((Button) activity.findViewById(R.id.dieButton));
        buttons.add((Button) activity.findViewById(R.id.dasButton));
    }

    public void enableAll() {
       for (Button button : buttons) {
        button.setEnabled(true);
    }
    }

    public void disable(Article article) {
       findButton(article.getDisplayValue()).setEnabled(false);
    }

    private Button findButton(String text) {
        Button button = null;
        for (Button currentButton : buttons) {
            if (text.equalsIgnoreCase(currentButton.getText().toString())) {
                button = currentButton;
                break;
            }
        }
        return button;
    }

}
