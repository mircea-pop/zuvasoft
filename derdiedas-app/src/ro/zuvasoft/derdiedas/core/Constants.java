package ro.zuvasoft.derdiedas.core;

import android.net.Uri;

public interface Constants
{

	public enum Article {
		DER("der"), DIE("die"), DAS("das");

		private String displayValue;

		private Article(String displayValue)
		{
			this.displayValue = displayValue;
		}

		public String getDisplayValue()
		{
			return displayValue;
		}

		public static Article intValueOf(int i)
		{
			Article retval = null;
			switch (i)
			{
				case 1 :
					retval = DER;
					break;

				case 2 :
					retval = DIE;
					break;

				case 3 :
					retval = DAS;
					break;

				default :
					retval = DIE;
					break;
			}
			return retval;
		}
	}

	public static final String COUNTER_OBJECT = "counterObject";
	public static final String DER_DIE_DAS_PREFERENCES = "DerDieDasPreferences";

	// update service constants
	public static final int DEFAULT_SUBJECT_QUEUE_SIZE = 32;
	public static final long DEFAULT_SUBJECT_QUEUE_UPDATE_DELAY = 40000;
	public static final int QUEUE_FILLED_MESSAGE_IDENTIFIER = 0x000001;

	/**
	 * Defines the flag when the the subject queue needs to be filled first time
	 * when the file is parsed to fill the db. This value should be larger then
	 * the subject queue size
	 */
	public static final int SUBJECT_QUEUE_FILL_FLAG_INDEX = 200;

	// database stuff
	public static final String DATABASE_NAME = "articlesubject.db";
	public static final String DATABASE_TABLE = "ArticleAndSubject";
	public static final int DATABASE_VERSION = 1;

	// ContentProvider stuff
	public static final Uri CONTENT_PROVIDER_FILTER_URI = null;
	public static final String SLASH = "/";
	public static final String CONTENT_URI_PROTOCOL = "content://";
	public static final String CONTENT_URI_AUTHORITY = "ro.zuvasoft.derdiedas.articlesubjectdatabase";
	public static final String CONTENT_URI_PATH = "elements";

	public static final Uri CONTENT_PROVIDER_URI = Uri.parse(CONTENT_URI_PROTOCOL
			+ CONTENT_URI_AUTHORITY + SLASH + CONTENT_URI_PATH);
	public static final String CONTENT_PROVIDER_KEY_ID = "_id";
	public static final String CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN = "SUBJECT_VALUE_COLUMN";
	public static final String CONTENT_PROVIDER_KEY_CORRECT_ARTICLES_COLUMN = "CORRECT_ARTICLES_COLUMN";
}
