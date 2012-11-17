package ro.zuvasoft.derdiedas.core;

import ro.zuvasoft.derdiedas.core.Constants.Article;

public class SubjectElement
{

	private String subjectValue;
	private Article[] articles;

	public SubjectElement(String subjectValue, Article[] articles)
	{
		this.subjectValue = subjectValue;
		this.articles = articles;
	}

	public String getSubjectValue()
	{
		return subjectValue;
	}

	public Article[] getCorrectArticles()
	{
		return articles;
	}

	@Override
	public String toString()
	{

		return "SubjectElement[subject=" + getSubjectValue() + "; articles = "
				+ getArticlesAsString() + "]";
	}

	private String getArticlesAsString()
	{
		String retval = "";
		if (articles != null)
		{
			for (Article article : articles)
			{
				retval = article.getDisplayValue() + " ";
			}
		}
		return retval;
	}
}
