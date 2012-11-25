package ro.zuvasoft.derdiedas.counter;

import java.io.Serializable;

import android.content.ContextWrapper;

public interface ICounter extends Serializable
{
	static final String SAVED_MAX_SCORE = "savedMaxScore";
    static final String SAVED_CURRENT_SCORE = "savedCurrentScore";

    void increment();

	void decrement();

	int getCounted();

	int getMaximumCounted();

	void setMaximumCounted(int newMaximumCounted);

	void addCounterListener(ICounterListener counterListener);

	void resetCurrent();
	
	void resetAll(ContextWrapper context);
	
	void save(ContextWrapper context);
}
