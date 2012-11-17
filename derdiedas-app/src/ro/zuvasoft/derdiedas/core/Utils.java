package ro.zuvasoft.derdiedas.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ro.zuvasoft.derdiedas.core.Constants.Article;
import android.util.Log;

public class Utils
{

	private static final String TAG = "ro.zuvasoft.derdiedas.core.Utils";

	public static Article[] convertToArticle(Integer articlesAsInteger)
	{
		String articlesAsString = articlesAsInteger.toString();
		int length = articlesAsString.length();
		Article[] retval = new Article[length];

		for (int i = length; i >= 1; i--)
		{
			int pow = (int)Math.pow(10, i - 1);
			int articleAsInt = articlesAsInteger / pow;
			retval[i - length] = Article.intValueOf(articleAsInt);
			articlesAsInteger = articlesAsInteger - articleAsInt * pow;
		}
		return retval;
	}

	public static void parseArticleSubjectFile(InputStream is, IArticleSubjectDBHandler asHandler)
	{
		parseArticleSubjectFile(is, asHandler, -1);
	}

	public static void parseArticleSubjectFile(
			InputStream is,
			IArticleSubjectDBHandler asHandler,
			long lineOffset)
	{
		BufferedReader bufferedReader = null;
		long currentLineIndex = 0;
		try
		{
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
			{
				if (currentLineIndex < lineOffset)
				{
					continue;
				}
				int articles = convertToInt(line.charAt(0));
				String subject = line.substring(1, line.length());
				asHandler.handleArticleSubject(subject, articles);
			}
		}
		catch (FileNotFoundException e)
		{
			Log.w(TAG, e.getMessage());
		}
		catch (IOException e)
		{
			Log.w(TAG, e.getMessage());
		}
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
					is.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}

	private static int convertToInt(char gender)
	{
		int retval = 1;
		switch (gender)
		{
			case 'm' :
				retval = 1;
				break;
			case 'f' :
				retval = 2;
				break;
			case 'n' :
				retval = 3;
				break;
		}
		return retval;
	}

	public static String readLine(InputStream is, long lineNumber)
	{
		String retval = null;
		String line = "";
		long currentLineNumber = 0;
		BufferedReader bufferedReader = null;
		try
		{
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			while ((line = bufferedReader.readLine()) != null)
			{
				if (currentLineNumber < lineNumber)
				{
					currentLineNumber++;
				}
				else
				{
					retval = line;
					break;
				}
			}
		}
		catch (FileNotFoundException e)
		{
			Log.w(TAG, e.getMessage());
		}
		catch (IOException e)
		{
			Log.w(TAG, e.getMessage());
		}
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
					is.close();
				}
				catch (IOException e)
				{
				}
			}
		}
		return retval;
	}
}
