package ro.zuvasoft.derdiedas.database;

import ro.zuvasoft.derdiedas.core.Constants;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WordProvider extends ContentProvider
{

	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;
	private static UriMatcher uriMatcher = null;
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Constants.CONTENT_URI_AUTHORITY,
				Constants.CONTENT_URI_PATH, ALL_ROWS);
		uriMatcher.addURI(Constants.CONTENT_URI_AUTHORITY,
				Constants.CONTENT_URI_PATH + Constants.SLASH + "#", SINGLE_ROW);
	}

	private static ArticleSubjectSQLiteOpenHelper sqliteOpenHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();

		switch (uriMatcher.match(uri))
		{
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = Constants.CONTENT_PROVIDER_KEY_ID
					+ "="
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ")" : "");
		default:
			break;
		}

		if (selection == null)
		{
			selection = "1";
		}

		int deleteCount = db.delete(Constants.DATABASE_TABLE, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return deleteCount;
	}

	@Override
	public String getType(Uri uri)
	{
		switch (uriMatcher.match(uri))
		{
		case ALL_ROWS:
			return "vnd.android.cursor.dir/vnd.derdiedas.elemental";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.derdiedas.elemental";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		Uri retval = null;

		SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();

		String nullColumnHack = null;
		long id = db.insert(Constants.DATABASE_TABLE, nullColumnHack, values);

		if (id > -1)
		{
			retval = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(retval, null);
		}

		return retval;
	}

	@Override
	public boolean onCreate()
	{
		sqliteOpenHelper = new ArticleSubjectSQLiteOpenHelper(getContext(),
				Constants.DATABASE_TABLE, null, Constants.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		SQLiteDatabase db = null;
		String groupBy = null;
		String having = null;
		try
		{
			db = sqliteOpenHelper.getWritableDatabase();
		} catch (Exception e)
		{
			db = sqliteOpenHelper.getReadableDatabase();
		}

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		switch (uriMatcher.match(uri))
		{
		case SINGLE_ROW:
			String rowIndex = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(Constants.CONTENT_PROVIDER_KEY_ID + "="
					+ rowIndex);
			break;
		}

		queryBuilder.setTables(Constants.DATABASE_TABLE);

		Cursor query = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);
		return query;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs)
	{
		SQLiteDatabase db = sqliteOpenHelper.getWritableDatabase();

		int updateCount = db.update(Constants.DATABASE_TABLE, values,
				selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

}
