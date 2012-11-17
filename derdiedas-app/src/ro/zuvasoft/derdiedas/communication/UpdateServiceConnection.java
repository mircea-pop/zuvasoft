package ro.zuvasoft.derdiedas.communication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class UpdateServiceConnection implements ServiceConnection
{

	private static final String TAG = "ro.zuvasoft.derdiedas.communication.UpdateServiceConnection";

	private UpdateService updateService;

	@Override
	public void onServiceConnected(ComponentName name, IBinder service)
	{
		updateService = ((UpdateService.UpdateBinder) service).getService();
		Log.i(TAG, "service = " + updateService);
		Log.i(TAG, "IBinder = " + service);

	}

	@Override
	public void onServiceDisconnected(ComponentName name)
	{
		// TODO Auto-generated method stub

	}

	public UpdateService getUpdateService()
	{
		return updateService;
	}
}
