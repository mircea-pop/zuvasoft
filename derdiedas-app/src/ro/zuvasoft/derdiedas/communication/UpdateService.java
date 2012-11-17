/* 
 * Date: UpdateService.java created on Sep 7, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.communication;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.zuvasoft.derdiedas.R;
import ro.zuvasoft.derdiedas.core.Constants;
import ro.zuvasoft.derdiedas.core.IArticleSubjectDBHandler;
import ro.zuvasoft.derdiedas.core.ISubjectContainer;
import ro.zuvasoft.derdiedas.core.ISubjectQueueListener;
import ro.zuvasoft.derdiedas.core.LimitedQueue;
import ro.zuvasoft.derdiedas.core.SubjectElement;
import ro.zuvasoft.derdiedas.core.Utils;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Vlad Popovici vpo86@yahoo.com
 * 
 */
public class UpdateService extends Service implements ISubjectContainer
{

	private static final String TAG = "ro.zuvasoft.derdiedas.communication.UpdateService";
	private final IBinder updateBinder = new UpdateBinder();
	private LimitedQueue<SubjectElement> limitedQueue;
	private Handler scheduledUpdateHandler;
	private UpdateQueueRunnable updateQueueRunnable;
	private Runnable loadFileDataRunnable;
	private boolean isBindToActivity;
	private long dbElementCounted = 0;
	private SQLRandomContentResolver sqlRandomContentResolver;
	private List<ISubjectQueueListener> queueListeners = new ArrayList<ISubjectQueueListener>();
	private boolean isQueueFilled;

	@Override
	public IBinder onBind(Intent arg0)
	{
		// when the binding between the MainActivity and the update service is
		// done (at the creation of the activity), the update scheduler will be
		// activated.
		isBindToActivity = true;
		fillQueueInBackground();
		return updateBinder;
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		isBindToActivity = false;
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		isQueueFilled = false;
		limitedQueue = new LimitedQueue<SubjectElement>(Constants.DEFAULT_SUBJECT_QUEUE_SIZE);
		scheduledUpdateHandler = new Handler();
		updateQueueRunnable = new UpdateQueueRunnable();
		loadFileDataRunnable = new LoadFileDataRunnable();
		sqlRandomContentResolver = new SQLRandomContentResolver(getContentResolver());
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		limitedQueue = null;
		scheduledUpdateHandler.removeCallbacks(updateQueueRunnable);
		scheduledUpdateHandler = null;
		updateQueueRunnable = null;
		loadFileDataRunnable = null;
		sqlRandomContentResolver = null;
		isQueueFilled = false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "onStartCommand executing");

		startBackgroundTasks(intent, startId);
		return Service.START_STICKY;
	}

	public boolean isBindToActivity()
	{
		return isBindToActivity;
	}

	private void startBackgroundTasks(Intent intent, int startId)
	{
		Log.i(TAG, "Starting background tasks.");
		loadFileDataInBackground();
		fillQueueInBackground();
	}

	public boolean isFileDataAlreadyLoaded()
	{
		String[] projection = new String[]
		{
			Constants.CONTENT_PROVIDER_KEY_ID
		};

		InputStream is = getResources().openRawResource(R.raw.words);

		Cursor query = getContentResolver().query(
				Constants.CONTENT_PROVIDER_URI,
				projection,
				Constants.CONTENT_PROVIDER_KEY_ID + "=" + (Long.parseLong(Utils.readLine(is, 0)) - 1),
				null,
				null);
		Log.i(TAG, "query.getCount()>0 = " + (query != null && query.getCount() > 0));
		return query != null && query.moveToNext();
	}

	private void loadFileDataInBackground()
	{
		if (!isFileDataAlreadyLoaded())
		{
			Log.i(TAG, "File data is beeing loaded");
			Thread xmlLoaderThread = new Thread(loadFileDataRunnable);
			xmlLoaderThread.start();
		}
	}

	private void fillQueueInBackground()
	{
		Thread thread = new Thread(updateQueueRunnable);
		thread.start();
	}

	private void fillQueue(IRandomContentResolver randomContentResolver)
	{
		int actualSize = limitedQueue.size();
		Log.i(TAG, "actualSize= " + actualSize);
		if (actualSize < Constants.DEFAULT_SUBJECT_QUEUE_SIZE)
		{

			int numerOfRequests = Constants.DEFAULT_SUBJECT_QUEUE_SIZE - actualSize;

			Iterator<SubjectElement> subjectElements = randomContentResolver
					.queryIterator(numerOfRequests);

			while (subjectElements.hasNext())
			{
				SubjectElement next = subjectElements.next();
				if (next != null)
				{
					limitedQueue.offer(next);
				}
			}

		}
		randomContentResolver.close();
	}

	@Override
	public SubjectElement pollSubject()
	{
		return limitedQueue.poll();
	}

	public void addSubjectQueueListener(ISubjectQueueListener queueListener)
	{
		if (!queueListeners.contains(queueListener))
		{
			queueListeners.add(queueListener);
		}
	}

	public void removeSubjectQueueListener(ISubjectQueueListener queueListener)
	{
		if (queueListeners.contains(queueListener))
		{
			queueListeners.remove(queueListener);
		}
	}

	private void fireQueueFilled()
	{
		for (ISubjectQueueListener queueListener : queueListeners)
		{
			queueListener.onQueueFilled(this);
		}
	}

	public boolean isQueueEmpty()
	{
		return !isQueueFilled;
	}

	public class UpdateBinder extends Binder
	{

		public UpdateService getService()
		{
			return UpdateService.this;
		}
	}

	class UpdateQueueRunnable implements Runnable
	{

		@Override
		public void run()
		{
			fillQueue(sqlRandomContentResolver);
			if (isBindToActivity())
			{
				scheduledUpdateHandler.postDelayed(this, Constants.DEFAULT_SUBJECT_QUEUE_UPDATE_DELAY);
			}
			if (isFileDataAlreadyLoaded() && !isQueueFilled)
			{
				fireQueueFilled();
				isQueueFilled = true;
			}
		}
	}

	class LoadFileDataRunnable implements Runnable
	{

		@Override
		public void run()
		{
			InputStream is = getResources().openRawResource(R.raw.words);
			Utils.parseArticleSubjectFile(is, new DbHandler(), 0);
		}
	}

	class DbHandler implements IArticleSubjectDBHandler
	{

		private ContentValues values;
		private ContentResolver cr;
		private int indexCounter = 0;
		private SubjectElement[] cachedSubjectContainer = new SubjectElement[Constants.SUBJECT_QUEUE_FILL_FLAG_INDEX];

		public DbHandler()
		{
			cr = getContentResolver();
			values = new ContentValues();
		}

		@Override
		public void handleArticleSubject(String subject, int articles)
		{
			values.put(Constants.CONTENT_PROVIDER_KEY_SUBJECT_VALUE_COLUMN, subject);
			values.put(Constants.CONTENT_PROVIDER_KEY_CORRECT_ARTICLES_COLUMN, articles);

			cr.insert(Constants.CONTENT_PROVIDER_URI, values);
			values.clear();
			fillQueueWithinFileLoad(subject, articles);
			indexCounter++;
		}

		private void fillQueueWithinFileLoad(String subject, int articles)
		{
			if (!isQueueFilled)
			{
				if (cachedSubjectContainer != null && !isCachedSubjectContainerFull())
				{
					cachedSubjectContainer[indexCounter] = new SubjectElement(
							subject,
							Utils.convertToArticle(articles));
				}
				else
				{
					limitedQueue.clear();
					fillQueue(new CachedRandomContentResolver(cachedSubjectContainer));
					cachedSubjectContainer = null;
					isQueueFilled = true;
					Log.i(TAG, "fillQueueWithinFileLoad: fire queue filled");
					fireQueueFilled();
				}
			}
		}

		private boolean isCachedSubjectContainerFull()
		{
			return indexCounter >= cachedSubjectContainer.length;
		}
	}

	class QueryLastRowRunnable implements Runnable
	{

		@Override
		public void run()
		{
			String[] projection = new String[]
			{
				"last_insert_rowid();"
			};
			Cursor query = getContentResolver().query(
					Constants.CONTENT_PROVIDER_URI,
					projection,
					null,
					null,
					null);
			dbElementCounted = query.getLong(0);
			Log.i(TAG, "total number of db entries = " + dbElementCounted);
		}
	}
}
