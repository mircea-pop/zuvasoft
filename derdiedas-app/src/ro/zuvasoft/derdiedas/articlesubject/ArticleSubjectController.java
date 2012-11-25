package ro.zuvasoft.derdiedas.articlesubject;

import java.util.ArrayList;
import java.util.List;

import ro.zuvasoft.derdiedas.R;
import ro.zuvasoft.derdiedas.core.Constants.Article;
import ro.zuvasoft.derdiedas.core.IArticleFailureResponse;
import ro.zuvasoft.derdiedas.core.IArticleSubjectListener;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.Html;
import android.widget.TextView;

public class ArticleSubjectController implements IArticleSubjectListener, OnClickListener {

    private final TextView subjectView;
    private final IArticleSubjectModel articleSubjectModel;
    private final ObjectAnimator oa;
    private final AlertDialog dialog;
    private final IArticleFailureResponse vibratorFailureResponse;

    private final List<IArticleFailureResponse> responses = new ArrayList<IArticleFailureResponse>();
    private static final String TAG = "ro.zuvasoft.derdiedas.articlesubject.ArticleSubjectController";

    public ArticleSubjectController(Activity activity, TextView subjectView, IArticleSubjectModel articleSubjectModel) {
        this.subjectView = subjectView;
        this.articleSubjectModel = articleSubjectModel;
        articleSubjectModel.addArticleSubjectListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(subjectView.getContext());
        builder.setTitle(R.string.titleWrongArticle).setPositiveButton(R.string.messagePlayAgain, this);

        dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
              closeDialog(dialog);
            }
        });
        dialog.setOwnerActivity(activity);

        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        vibratorFailureResponse = new VibratorFailureResponse(vibrator, 500);

        oa = ObjectAnimator.ofInt(subjectView, "ThumbAlpha", 255);
        oa.setDuration(500);
        oa.setRepeatCount(1);
        oa.setRepeatMode(ValueAnimator.REVERSE);
    }

    public void addAnimatorListener(AnimatorListener animationListener) {
        oa.addListener(animationListener);
    }

    private Resources getResources() {
        return subjectView.getResources();
    }

    public TextView getSubjectView() {
        return subjectView;
    }

    public IArticleSubjectModel getArticleSubjectModel() {
        return articleSubjectModel;
    }

    public void addArticleFailureResponse(IArticleFailureResponse articleFailureResponse) {

        if (!responses.contains(articleFailureResponse) && articleFailureResponse != null) {
            responses.add(articleFailureResponse);
        }
    }

    private void displayFailure() {
        vibratorFailureResponse.executeFailureResponse();

        IArticleSubjectModel articleSubjectModel = getArticleSubjectModel();
        Article chosenArticle = articleSubjectModel.getChosenArticle();

        dialog.setMessage(Html.fromHtml(getResources().getString(R.string.messageResult, chosenArticle.getDisplayValue(),
                getCorrectArticlesString())));
        dialog.show();
    }

    private void displaySuccess() {
        oa.start();
    }

    private String getCorrectArticlesString() {
        IArticleSubjectModel articleSubjectModel = getArticleSubjectModel();
        String retval = "";
        for (Article article : articleSubjectModel.getCorrectArticles()) {
            retval += article.getDisplayValue() + " ";
        }

        return retval;
    }

    @Override
    public void onChosenArticleChanged(IArticleSubjectModel source, Article newArticle) {
        if (oa.isRunning()) {
            oa.cancel();
        }

        String subjectText = "";
        IArticleSubjectModel articleSubjectModel = getArticleSubjectModel();
        if (articleSubjectModel.isCorrectArticle()) {
            subjectText += getCorrectArticlesString();
            getSubjectView().setText(subjectText + articleSubjectModel.getSubject());
            displaySuccess();
        } else {
            displayFailure();
        }
    }

    @Override
    public void onSessionChanged(IArticleSubjectModel source, String newSubject, Article[] newCorrectArticles) {
        getSubjectView().setText(newSubject);
    }

    /**
     * This should be called to close the dialog window.
     * @param dialog
     */
    private void closeDialog(DialogInterface dialog) {
        dialog.dismiss();

        for (IArticleFailureResponse response : responses) {
            response.executeFailureResponse();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        closeDialog(dialog);
    }
}
