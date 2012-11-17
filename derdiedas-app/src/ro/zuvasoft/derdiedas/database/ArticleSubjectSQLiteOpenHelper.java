package ro.zuvasoft.derdiedas.database;

import ro.zuvasoft.derdiedas.core.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArticleSubjectSQLiteOpenHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_CREATE = "create table "
			+ Constants.DATABASE_TABLE + " ("
			+ Constants.CONTENT_PROVIDER_KEY_ID
			+ " integer primary key autoincrement, "
			+ Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN
			+ " text not null, "
			+ Constants.CONTENT_PROVIDER_KEY_CORRECT_ARTICLES_COLUMN
			+ " integer)";
	private static final String TAG = "ro.zuvasoft.derdiedas.database.ArticleSubjecSQLLiteOpenHelper";

	public ArticleSubjectSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TAG, "Upgrading from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		db.execSQL("DROP TABLE IF IT EXISTS " + Constants.DATABASE_TABLE);
		onCreate(db);
	}

}
