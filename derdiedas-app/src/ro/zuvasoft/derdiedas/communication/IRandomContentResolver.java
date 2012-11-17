/* 
 * Date: IRandomContentResolver.java created on Sep 6, 2012 
 * Copyright zuvasoft.ro. All rights reserved.
 */

package ro.zuvasoft.derdiedas.communication;

import java.util.Iterator;

import ro.zuvasoft.derdiedas.core.SubjectElement;

/**
 * @author vpo
 */
public interface IRandomContentResolver
{

	Iterator<SubjectElement> queryIterator(int iteratorSize);

	void close();
}
