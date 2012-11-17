package ro.zuvasoft.derdiedas.core;

import ro.zuvasoft.derdiedas.articlesubject.IArticleSubjectModel;
import ro.zuvasoft.derdiedas.core.Constants.Article;

public interface IArticleSubjectListener
{

	void onChosenArticleChanged(IArticleSubjectModel source, Article newArticle);

	void onSessionChanged(IArticleSubjectModel source, String newSubject,
			Article[] newCorrectArticles);
}
