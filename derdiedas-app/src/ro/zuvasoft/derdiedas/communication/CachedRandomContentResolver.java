/* 
 * Date: TemporaryRandomContentResolver.java created on Sep 7, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.communication;

import java.util.Iterator;

import ro.zuvasoft.derdiedas.core.SubjectElement;

/**
 * 
 * @author Vlad Popovici vpo86@yahoo.com
 * 
 */
public class CachedRandomContentResolver
		implements
			IRandomContentResolver,
			Iterator<SubjectElement>
{

	private SubjectElement[] sourceOfsubjectElements;
	private int numberOfQueries;
	private int iteratorSize;

	public CachedRandomContentResolver(SubjectElement[] sourceOfsubjectElements)
	{
		this.sourceOfsubjectElements = sourceOfsubjectElements;
	}

	@Override
	public Iterator<SubjectElement> queryIterator(int iteratorSize)
	{
		this.iteratorSize = iteratorSize;
		return this;
	}

	@Override
	public void close()
	{
		sourceOfsubjectElements = null;
		numberOfQueries = 0;
	}

	@Override
	public boolean hasNext()
	{
		return sourceOfsubjectElements != null && numberOfQueries < iteratorSize;
	}

	@Override
	public SubjectElement next()
	{
		numberOfQueries++;
		return sourceOfsubjectElements != null
				? sourceOfsubjectElements[(int)(Math.random() * sourceOfsubjectElements.length)] : null;
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException(
				"CachedRandomContentResolver: The remove action is not supported.");
	}
}
