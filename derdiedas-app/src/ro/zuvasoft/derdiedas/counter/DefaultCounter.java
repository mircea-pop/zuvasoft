package ro.zuvasoft.derdiedas.counter;

import java.util.ArrayList;
import java.util.List;

public class DefaultCounter implements ICounter
{

	private static final long serialVersionUID = -1103956923462004912L;
	private int countValue;
	private int maximumCounted;
	private transient List<ICounterListener> counterListeners = new ArrayList<ICounterListener>();
	private int offset;

	public DefaultCounter(int offset, int maximumCounted)
	{
		countValue = offset;
		this.offset = offset;
		this.maximumCounted = maximumCounted;
		syncronizeCountMaximumValues();
	}

	private void syncronizeCountMaximumValues()
	{
		if (countValue > getMaximumCounted())
		{
			setMaximumCounted(countValue);
		}
	}

	public DefaultCounter()
	{
		this(0, 0);
	}

	DefaultCounter(int offset)
	{
		this(offset, 0);
	}

	@Override
	public void increment()
	{
		countValue++;
		notifyCurrentValueChanged(countValue);
		syncronizeCountMaximumValues();
	}

	@Override
	public void decrement()
	{
		countValue--;
		notifyCurrentValueChanged(countValue);
	}

	@Override
	public void resetCurrent()
	{
		countValue = offset;
		notifyCurrentValueChanged(countValue);
	}

	@Override
	public int getCounted()
	{
		return countValue;
	}

	@Override
	public int getMaximumCounted()
	{
		return maximumCounted;
	}

	@Override
	public void setMaximumCounted(int newMaximumCounted)
	{
		this.maximumCounted = newMaximumCounted;
		notifyMaxValueChanged(maximumCounted);
	}

	@Override
	public void addCounterListener(ICounterListener counterListener)
	{
		if (!counterListeners.contains(counterListener)
				&& counterListener != null)
		{
			counterListeners.add(counterListener);
		}
	}

	private void notifyMaxValueChanged(int maxValue)
	{
		for (ICounterListener listener : counterListeners)
		{
			listener.onMaximumValueChanged(maxValue);
		}
	}

	private void notifyCurrentValueChanged(int currentValue)
	{
		for (ICounterListener listener : counterListeners)
		{
			listener.onCurrentValueChanged(currentValue);
		}
	}

}
