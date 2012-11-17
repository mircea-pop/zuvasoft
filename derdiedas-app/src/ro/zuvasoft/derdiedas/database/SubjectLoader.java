package ro.zuvasoft.derdiedas.database;

import ro.zuvasoft.derdiedas.articlesubject.IArticleSubjectModel;
import ro.zuvasoft.derdiedas.core.Constants;
import ro.zuvasoft.derdiedas.core.LimitedQueue;
import ro.zuvasoft.derdiedas.core.SubjectElement;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class SubjectLoader implements LoaderCallbacks<Cursor>, AnimatorListener
{

	private static final String[] SUBJECT_SUMMARY_PROJECTION = null;
	private LimitedQueue<SubjectElement> subjectQueue;
	private IArticleSubjectModel articleSubjectModel;
	private String mCurFilter;
	private Context context;

	public SubjectLoader(Context context,
			IArticleSubjectModel articleSubjectModel)
	{
		this.articleSubjectModel = articleSubjectModel;
		this.context = context;
		subjectQueue = new LimitedQueue<SubjectElement>(10);
	}

	public IArticleSubjectModel getArticleSubjectModel()
	{
		return articleSubjectModel;
	}

	public Context getContext()
	{
		return context;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		Uri baseUri;
		if (mCurFilter != null)
		{
			baseUri = Uri.withAppendedPath(
					Constants.CONTENT_PROVIDER_FILTER_URI,
					Uri.encode(mCurFilter));
		}
		else
		{
			baseUri = Constants.CONTENT_PROVIDER_URI;
		}

		String select = "(("
				+ Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN
				+ " NOTNULL) AND ("
				+ Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN
				+ " != '' ))";
		return new CursorLoader(context, baseUri, SUBJECT_SUMMARY_PROJECTION,
				select, null,
				Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN
						+ " COLLATE LOCALIZED ASC");
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data)
	{
		// TODO
		// subjectQueue.offer(e);
	}

	public void onLoaderReset(Loader<Cursor> loader)
	{
		// TODO
	}

	@Override
	public void onAnimationCancel(Animator animation)
	{
	}

	@Override
	public void onAnimationEnd(Animator animation)
	{
		SubjectElement poll = subjectQueue.poll();
		IArticleSubjectModel articleSubjectModel = getArticleSubjectModel();
		articleSubjectModel.setSubject(poll.getSubjectValue());
		articleSubjectModel.setCorrectArticles(poll.getCorrectArticles());
	}

	@Override
	public void onAnimationRepeat(Animator animation)
	{
	}

	@Override
	public void onAnimationStart(Animator animation)
	{
	}
}
