package ro.zuvasoft.derdiedas.articlesubject;

import java.util.ArrayList;
import java.util.List;

import ro.zuvasoft.derdiedas.core.Constants.Article;
import ro.zuvasoft.derdiedas.core.IArticleSubjectListener;

public class DefaultArticleSubjectModel implements IArticleSubjectModel
{

	private String subject;
	private Article[] correctArticles = new Article[0];
	private Article chosenArticle;
	private List<IArticleSubjectListener> listeners = new ArrayList<IArticleSubjectListener>();
	private boolean isSubjectChanged;
	private boolean isCorrectArticlesChanged;

	public DefaultArticleSubjectModel(String subject, Article[] correctArticles, Article chosenArticle)
	{
		this.subject = subject;
		this.correctArticles = correctArticles;
		this.chosenArticle = chosenArticle;
	}

	public DefaultArticleSubjectModel()
	{
	}

	@Override
	public void setSubject(String subject)
	{
		this.subject = subject;
		isSubjectChanged = true;
		notifySessionChanged();
	}

	@Override
	public String getSubject()
	{
		return subject;
	}

	@Override
	public void setChosenArticle(Article article)
	{
		chosenArticle = article;
		notifyChosenChanged(article);
	}

	@Override
	public Article getChosenArticle()
	{
		return chosenArticle;
	}

	@Override
	public void setCorrectArticles(Article[] correctArticles)
	{
		isCorrectArticlesChanged = true;
		this.correctArticles = correctArticles.clone();
		notifySessionChanged();
	}

	@Override
	public Article[] getCorrectArticles()
	{
		return correctArticles.clone();
	}

	@Override
	public boolean isCorrectArticle()
	{
		boolean retval = false;
		for (Article article : correctArticles)
		{
			if (article.equals(getChosenArticle()))
			{
				retval = true;
				break;
			}
		}
		return retval;
	}

	@Override
	public void addArticleSubjectListener(IArticleSubjectListener asListener)
	{
		if (!listeners.contains(asListener) && asListener != null)
		{
			listeners.add(asListener);
		}
	}

	private void notifySessionChanged()
	{
		if (isSubjectChanged && isCorrectArticlesChanged)
		{
			for (IArticleSubjectListener listener : listeners)
			{
				listener.onSessionChanged(this, getSubject(), getCorrectArticles());
			}
			isSubjectChanged = false;
			isCorrectArticlesChanged = false;
		}
	}

	private void notifyChosenChanged(Article article)
	{
		for (IArticleSubjectListener listener : listeners)
		{
			listener.onChosenArticleChanged(this, article);
		}
	}
}
