package ro.zuvasoft.derdiedas.counter;

import android.os.AsyncTask;

public class CounterTester extends AsyncTask<Integer, Integer, Integer>
{

	private volatile ICounter counter;

	public CounterTester(ICounter counter)
	{
		this.counter = counter;
	}

	public static void testMe(ICounter counter)
	{
		int[] operations = new int[] { 1, 1, 1, 1, 2, 1, 3, 3, 1 };
		for (int operation : operations)
		{
			CounterTester counterTester = new CounterTester(counter);
			counterTester.execute(Integer.valueOf(2000),
					Integer.valueOf(operation));
		}

	}

	private void sleep(int sleepTime)
	{
		try
		{
			Thread.sleep(sleepTime);
		} catch (InterruptedException e)
		{
		}
	}

	@Override
	protected Integer doInBackground(Integer... params)
	{
		sleep(params[0]);

		return params[1];
	}

	@Override
	protected void onPostExecute(Integer result)
	{
		super.onPostExecute(result);

		switch (result)
		{
		case 1:
			counter.increment();
			break;
		case 2:
			counter.decrement();
			break;
		case 3:
			if (counter.getMaximumCounted() > 0)
			{
				counter.setMaximumCounted(counter.getMaximumCounted() * 2);
			}
			else
			{
				counter.setMaximumCounted(3);
			}
		}
	}
}
