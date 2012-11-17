package ro.zuvasoft.derdiedas.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent updateIntend = new Intent(context, UpdateService.class);
		context.startService(updateIntend);
	}

}
