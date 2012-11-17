package ro.zuvasoft.derdiedas.articlesubject;

import ro.zuvasoft.derdiedas.core.IArticleFailureResponse;
import ro.zuvasoft.derdiedas.core.ISubjectContainer;
import ro.zuvasoft.derdiedas.core.SubjectElement;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;

public class NewSessionStarter extends AnimatorListenerAdapter implements IArticleFailureResponse
{

	private static final String TAG = "ro.zuvasoft.derdiedas.articlesubject.StartNewSessionListener";

	private ISubjectContainer subjectContainer;
	private IArticleSubjectModel articleSubjectModel;

	public NewSessionStarter(
			ISubjectContainer subjectContainer,
			IArticleSubjectModel articleSubjectModel)
	{
		this.subjectContainer = subjectContainer;
		this.articleSubjectModel = articleSubjectModel;
	}

	public ISubjectContainer getSubjectContainer()
	{
		return subjectContainer;
	}

	public IArticleSubjectModel getArticleSubjectModel()
	{
		return articleSubjectModel;
	}

	@Override
	public void onAnimationEnd(Animator animation)
	{
		startNewSession();
	}

	public void startNewSession()
	{
		SubjectElement subjectElement = getSubjectContainer().pollSubject();
		Log.i(TAG, "Polled element : " + subjectElement);

		getArticleSubjectModel().setSubject(subjectElement.getSubjectValue());
		getArticleSubjectModel().setCorrectArticles(subjectElement.getCorrectArticles());
	}

	@Override
	public void executeFailureResponse()
	{
		startNewSession();
	}
}
