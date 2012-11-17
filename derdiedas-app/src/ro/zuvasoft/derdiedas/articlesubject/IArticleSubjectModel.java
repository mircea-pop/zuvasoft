package ro.zuvasoft.derdiedas.articlesubject;

import ro.zuvasoft.derdiedas.core.IArticleSubjectListener;
import ro.zuvasoft.derdiedas.core.Constants.Article;

public interface IArticleSubjectModel
{

	void setSubject(String subject);

	String getSubject();

	void setChosenArticle(Article article);

	Article getChosenArticle();

	void setCorrectArticles(Article[] correctArticles);

	Article[] getCorrectArticles();

	boolean isCorrectArticle();

	void addArticleSubjectListener(IArticleSubjectListener asListener);
}
