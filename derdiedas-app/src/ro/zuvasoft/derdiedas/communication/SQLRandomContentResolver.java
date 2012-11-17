/* 
 * Date: SQLRandomContentResolver.java created on Sep 6, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.communication;

import java.util.Iterator;

import ro.zuvasoft.derdiedas.core.Constants;
import ro.zuvasoft.derdiedas.core.Constants.Article;
import ro.zuvasoft.derdiedas.core.SubjectElement;
import ro.zuvasoft.derdiedas.core.Utils;
import android.content.ContentResolver;
import android.database.Cursor;

/**
 * 
 * @author Vlad Popovici vpo86@yahoo.com
 * 
 */
public class SQLRandomContentResolver implements IRandomContentResolver, Iterator<SubjectElement>
{

	private ContentResolver contentResolver;
	private Cursor resultCursor;

	public SQLRandomContentResolver(ContentResolver contentResolver)
	{
		this.contentResolver = contentResolver;

	}

	public ContentResolver getContentResolver()
	{
		return contentResolver;
	}

	@Override
	public Iterator<SubjectElement> queryIterator(int iteratorSize)
	{
		ContentResolver cr = getContentResolver();
		String[] result_columns = new String[]
		{
				Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN,
				Constants.CONTENT_PROVIDER_KEY_CORRECT_ARTICLES_COLUMN
		};

		String whereArgs[] = null;

		resultCursor = cr.query(
				Constants.CONTENT_PROVIDER_URI,
				result_columns,
				null,
				whereArgs,
				"RANDOM() LIMIT " + iteratorSize);

		return this;
	}

	@Override
	public void close()
	{
		if (resultCursor != null)
		{
			resultCursor.close();
		}
	}

	@Override
	public boolean hasNext()
	{
		return resultCursor != null && !resultCursor.isLast();
	}

	@Override
	public SubjectElement next()
	{
		SubjectElement retval = null;
		if (resultCursor.moveToNext())
		{
			int subjectIndex = resultCursor
					.getColumnIndex(Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN);
			int articlesIndex = resultCursor
					.getColumnIndex(Constants.CONTENT_PROVIDER_KEY_CORRECT_ARTICLES_COLUMN);

			String subject = resultCursor.getString(subjectIndex);
			Article[] correctArticles = Utils.convertToArticle(resultCursor.getInt(articlesIndex));

			retval = new SubjectElement(subject, correctArticles);
		}
		else
		{
			retval = null;
		}
		return retval;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException(
				"SQLRandomContentResolver: The remove action is not supported.");
	}

}
