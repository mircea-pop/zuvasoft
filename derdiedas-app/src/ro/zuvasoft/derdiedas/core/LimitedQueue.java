package ro.zuvasoft.derdiedas.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LimitedQueue<T> implements Queue<T>
{

	private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();
	private int sizeLimit;

	public LimitedQueue(int sizeLimit)
	{
		this.sizeLimit = sizeLimit;
	}

	public int getSizeLimit()
	{
		return sizeLimit;
	}

	@Override
	public boolean addAll(Collection<? extends T> elements)
	{
		boolean retval = false;
		int totalSize = elements.size() + queue.size();
		int sizeLimit = getSizeLimit();
		if (totalSize < sizeLimit)
		{
			retval = queue.addAll(elements);
		}
		return retval;
	}

	@Override
	public void clear()
	{
		queue.clear();
	}

	@Override
	public boolean contains(Object object)
	{
		return queue.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> elements)
	{
		return queue.containsAll(elements);
	}

	@Override
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return queue.iterator();
	}

	@Override
	public boolean remove(Object object)
	{
		return queue.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> elements)
	{
		return queue.removeAll(elements);
	}

	@Override
	public boolean retainAll(Collection<?> elements)
	{
		return queue.retainAll(elements);
	}

	@Override
	public int size()
	{
		return queue.size();
	}

	@Override
	public Object[] toArray()
	{
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array)
	{
		return queue.toArray(array);
	}

	@Override
	public boolean add(T e)
	{
		boolean retval = false;
		if (queue.size() + 1 < getSizeLimit())
		{
			retval = queue.add(e);
		}
		return retval;
	}

	@Override
	public T element()
	{
		return queue.element();
	}

	@Override
	public boolean offer(T e)
	{
		boolean retval = false;
		if (queue.size() + 1 < getSizeLimit())
		{
			retval = queue.offer(e);
		}
		return retval;
	}

	@Override
	public T peek()
	{
		return queue.peek();
	}

	@Override
	public T poll()
	{
		return queue.poll();
	}

	@Override
	public T remove()
	{
		return queue.remove();
	}
}
