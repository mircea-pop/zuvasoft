package ro.zuvasoft.derdiedas.counter;

import java.io.Serializable;

public interface ICounter extends Serializable
{

	void increment();

	void decrement();

	int getCounted();

	int getMaximumCounted();

	void setMaximumCounted(int newMaximumCounted);

	void addCounterListener(ICounterListener counterListener);

	void resetCurrent();
}
