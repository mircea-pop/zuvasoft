package ro.zuvasoft.derdiedas.articlesubject;

import ro.zuvasoft.derdiedas.core.Constants.Article;
import ro.zuvasoft.derdiedas.core.IArticleSubjectListener;

public class StatefullArticleSubjectModel implements IArticleSubjectModel
{

	enum State
	{
		CHOOSING_ARTICLE, SUBJECT_CHANGED, RETRY;

		public boolean isSubjectChanged()
		{
			return this.equals(SUBJECT_CHANGED);
		}
		
		public boolean isRetry(){
		    return this.equals(RETRY);
		}
	}

	private final IArticleSubjectModel targetModel;
	private State currentState = State.SUBJECT_CHANGED;

	public StatefullArticleSubjectModel(IArticleSubjectModel targetModel)
	{
		this.targetModel = targetModel;
	}

	@Override
	public void setSubject(String subject)
	{
		targetModel.setSubject(subject);
		currentState = State.SUBJECT_CHANGED;
	}

	@Override
	public String getSubject()
	{
		return targetModel.getSubject();
	}

	@Override
	public void setChosenArticle(Article article)
	{
	//	if (currentState.isSubjectChanged() || currentState.isRetry())
		{
			targetModel.setChosenArticle(article);
			currentState = State.CHOOSING_ARTICLE;
		}
	}

	@Override
	public Article getChosenArticle()
	{
		return targetModel.getChosenArticle();
	}

	@Override
	public void setCorrectArticles(Article[] correctArticles)
	{
		targetModel.setCorrectArticles(correctArticles);
		currentState = State.SUBJECT_CHANGED;
	}

	@Override
	public Article[] getCorrectArticles()
	{
		return targetModel.getCorrectArticles();
	}

	@Override
	public boolean isCorrectArticle()
	{
		return targetModel.isCorrectArticle();
	}

	@Override
	public void addArticleSubjectListener(IArticleSubjectListener asListener)
	{
		targetModel.addArticleSubjectListener(asListener);
	}

}
