package ro.zuvasoft.derdiedas.core;

public class LimitExceededException extends RuntimeException
{

	private static final long serialVersionUID = 593918638558202147L;

	public LimitExceededException(String message)
	{
	}

	public LimitExceededException(int sizeLimit, int requesteSize)
	{
	}
}
