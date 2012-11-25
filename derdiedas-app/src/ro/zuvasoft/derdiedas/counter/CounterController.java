package ro.zuvasoft.derdiedas.counter;

import ro.zuvasoft.derdiedas.R;
import ro.zuvasoft.derdiedas.articlesubject.IArticleSubjectModel;
import ro.zuvasoft.derdiedas.core.Constants.Article;
import ro.zuvasoft.derdiedas.core.IArticleFailureResponse;
import ro.zuvasoft.derdiedas.core.IArticleSubjectListener;
import android.content.res.Resources;
import android.widget.TextView;

public class CounterController implements ICounterListener,
		IArticleSubjectListener, IArticleFailureResponse
{

	private final TextView counterView;
	private final ICounter counter;

	public CounterController(TextView counterView, ICounter counter)
	{
		this.counterView = counterView;
		this.counter = counter;
	}

	public ICounter getCounter()
	{
		return counter;
	}

	public TextView getCounterView()
	{
		return counterView;
	}

	@Override
	public void onMaximumValueChanged(int newMaximumValue)
	{
		updateCounterView();
	}

	@Override
	public void onCurrentValueChanged(int newCurrentValue)
	{
		updateCounterView();
	}

	public void updateCounterView()
	{
		ICounter counter = getCounter();
		TextView counterView = getCounterView();
		Resources resources = counterView.getResources();
		String newValue = resources.getString(R.string.textCounter,
				counter.getMaximumCounted(), counter.getCounted());
		counterView.setText(" " + newValue + " ");
	}

	@Override
	public void onChosenArticleChanged(IArticleSubjectModel source,
			Article newArticle)
	{
		if (source.isCorrectArticle())
		{
			getCounter().increment();
		}
	}

	@Override
	public void onSessionChanged(IArticleSubjectModel source,
			String newSubject, Article[] newCorrectArticles)
	{
	}

	@Override
	public void executeFailureResponse()
	{
		//counter.resetCurrent();
	}
}
