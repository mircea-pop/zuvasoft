package ro.zuvasoft.derdiedas.articlesubject;

import ro.zuvasoft.derdiedas.core.Constants.Article;
import android.view.View;
import android.view.View.OnClickListener;
import ro.zuvasoft.derdiedas.R;

public class ArticleButtonListener implements OnClickListener
{

	private IArticleSubjectModel articleSubjectModel;

	public ArticleButtonListener(IArticleSubjectModel articleSubjectModel)
	{
		this.articleSubjectModel = articleSubjectModel;
	}

	@Override
	public void onClick(View view)
	{
		int id = view.getId();

		switch (id)
		{
		case R.id.derButton:
			articleSubjectModel.setChosenArticle(Article.DER);
			break;
		case R.id.dieButton:
			articleSubjectModel.setChosenArticle(Article.DIE);
			break;
		case R.id.dasButton:
			articleSubjectModel.setChosenArticle(Article.DAS);
			break;
		}
	}
}
