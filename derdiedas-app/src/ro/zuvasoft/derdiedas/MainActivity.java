package ro.zuvasoft.derdiedas;

import java.beans.PropertyChangeSupport;

import ro.zuvasoft.derdiedas.activities.MenuActivity;
import ro.zuvasoft.derdiedas.articlesubject.ArticleButtonListener;
import ro.zuvasoft.derdiedas.articlesubject.ArticleSubjectController;
import ro.zuvasoft.derdiedas.articlesubject.DefaultArticleSubjectModel;
import ro.zuvasoft.derdiedas.articlesubject.IArticleSubjectModel;
import ro.zuvasoft.derdiedas.articlesubject.NewSessionStarter;
import ro.zuvasoft.derdiedas.articlesubject.StatefullArticleSubjectModel;
import ro.zuvasoft.derdiedas.communication.UpdateService;
import ro.zuvasoft.derdiedas.core.Constants;
import ro.zuvasoft.derdiedas.core.ISubjectContainer;
import ro.zuvasoft.derdiedas.core.ISubjectQueueListener;
import ro.zuvasoft.derdiedas.counter.CounterController;
import ro.zuvasoft.derdiedas.counter.CounterTester;
import ro.zuvasoft.derdiedas.counter.DefaultCounter;
import ro.zuvasoft.derdiedas.counter.ICounter;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends MenuActivity implements ServiceConnection, ISubjectQueueListener {
    private static final String TAG = "DerDieDasMainActivity";

    private ICounter counter;
    PropertyChangeSupport mainPropChangeSupport = new PropertyChangeSupport(this);

    private ArticleSubjectController articleSubjectController;

    private IArticleSubjectModel articleSubjModel;

    private UpdateService updateService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the order of this line is important, here the db is filled first time
        checkOrStartUpdateService();
        getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.activity_main);

        initCounter(savedInstanceState);
        updateCounterView();

        CounterController counterController = new CounterController(getCounterView(), counter);
        counter.addCounterListener(counterController);
        counterController.updateCounterView();

        articleSubjModel = new StatefullArticleSubjectModel(new DefaultArticleSubjectModel());

        articleSubjModel.addArticleSubjectListener(counterController);

        TextView subjectView = getSubjectView();
        articleSubjectController = new ArticleSubjectController(this, subjectView, articleSubjModel);

        articleSubjectController.addArticleFailureResponse(counterController);

        addClickListeners(new ArticleButtonListener(articleSubjModel), R.id.derButton, R.id.dieButton, R.id.dasButton);

        bindToUpdateService();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            // finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    public void bindToUpdateService() {
        Intent bindIntent = new Intent(this, UpdateService.class);
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }

    private void checkOrStartUpdateService() {
        if (!isUpdateServiceRunning()) {
            startService(new Intent(this, UpdateService.class));
        }
    }

    private boolean isUpdateServiceRunning() {
        boolean retval = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("ro.zuvasoft.derdiedas.communication.UpdateService".equals(service.service.getClassName())) {
                retval = true;
                break;
            }
        }

        return retval;
    }

    private TextView getCounterView() {
        TextView subjectView = (TextView) findViewById(R.id.counterView);
        return subjectView;
    }

    private TextView getSubjectView() {
        TextView subjectView = (TextView) findViewById(R.id.subjectView);
        return subjectView;
    }

    private void addClickListeners(OnClickListener handler, int... ids) {
        for (int id : ids) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(handler);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToUpdateService();
        // this is needed if the user doesn't press the dialog's button
        // counter.resetCurrent();
        // doTestOnBackground(new CounterTester(counter));
    }

    private void doTestOnBackground(CounterTester executable) {
        CounterTester.testMe(counter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateService.removeSubjectQueueListener(this);
        unbindService(this);

        counter.save(this);
    }

    private void updateCounterView() {
        Resources res = getResources();
        String countText = res.getString(R.string.textCounter, counter.getMaximumCounted(), counter.getCounted());
        TextView countView = (TextView) findViewById(R.id.counterView);
        countView.setText(countText);
    }

    private void initCounter(Bundle savedInstanceState) {
        // restore the saved pref data
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.COUNTER_OBJECT)) {
            counter = (ICounter) savedInstanceState.getSerializable(Constants.COUNTER_OBJECT);
        } else {
            counter = new DefaultCounter(preferences.getInt(ICounter.SAVED_CURRENT_SCORE, 0), preferences.getInt(ICounter.SAVED_MAX_SCORE,
                    0));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.COUNTER_OBJECT, counter);
        outState.putString("test", "working");
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        updateService = ((UpdateService.UpdateBinder) service).getService();
        NewSessionStarter newSessionStarter = new NewSessionStarter(updateService, articleSubjModel);
        articleSubjectController.addAnimatorListener(newSessionStarter);
        articleSubjectController.addArticleFailureResponse(newSessionStarter);
        updateService.addSubjectQueueListener(this);
        
        if (!updateService.isQueueEmpty()) {
            newSessionStarter.startNewSession();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Override
    public void onQueueFilled(final ISubjectContainer subjectContainer) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                NewSessionStarter newSessionStarter = new NewSessionStarter(subjectContainer, articleSubjModel);
                newSessionStarter.startNewSession();
            }
        });
        Thread.yield();
    }

    @Override
    public void resetCounter() {
        if (null != counter) {
            counter.resetAll(this);
        }
    }
}
