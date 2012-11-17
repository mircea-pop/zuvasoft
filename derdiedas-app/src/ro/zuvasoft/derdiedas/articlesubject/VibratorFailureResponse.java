package ro.zuvasoft.derdiedas.articlesubject;

import ro.zuvasoft.derdiedas.core.IArticleFailureResponse;
import android.os.Vibrator;

public class VibratorFailureResponse implements IArticleFailureResponse
{

	private Vibrator vibrator;
	private int duration;

	public VibratorFailureResponse(Vibrator vibrator, int duration)
	{
		this.vibrator = vibrator;
		this.duration = duration;
	}

	public int getDuration()
	{
		return duration;
	}

	public Vibrator getVibrator()
	{
		return vibrator;
	}

	@Override
	public void executeFailureResponse()
	{
		vibrator.vibrate(getDuration());
	}
}
