package ro.zuvasoft.derdiedas.counter;

public interface ICounterListener
{

	void onMaximumValueChanged(int newMaximumValue);

	void onCurrentValueChanged(int newCurrentValue);
}
