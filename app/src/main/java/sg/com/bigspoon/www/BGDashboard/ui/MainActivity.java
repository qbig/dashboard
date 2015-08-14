

package sg.com.bigspoon.www.BGDashboard.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.ProgressBar;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.onesignal.OneSignal;
import com.onesignal.OneSignal.NotificationOpenedHandler;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.Views;
import sg.com.bigspoon.www.BGDashboard.BootstrapServiceProvider;
import sg.com.bigspoon.www.BGDashboard.R;
import sg.com.bigspoon.www.BGDashboard.core.BigSpoonApp;
import sg.com.bigspoon.www.BGDashboard.core.Constants;
import sg.com.bigspoon.www.BGDashboard.events.NavItemSelectedEvent;
import sg.com.bigspoon.www.BGDashboard.util.BGUtils;
import sg.com.bigspoon.www.BGDashboard.util.Ln;

import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.OUTLET_ID;
import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.PREFS_NAME;

/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link sg.com.bigspoon.www.BGDashboard.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {
    // NotificationOpenedHandler is implemented in its own class instead of adding implements to MainActivity so we don't hold on to a reference of our first activity if it gets recreated.
    private class ExampleNotificationOpenedHandler implements NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
         * a new one comes in while the app is running.
         * This method is located in this activity as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
         *
         * @param message        The message string the user seen/should see in the Android status bar.
         * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
         * @param isActive       Was the app in the foreground when the notification was received.
         */
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String messageTitle = "New Message from Customer!", messageBody = message;

            try {
                if (additionalData != null) {
                    if (additionalData.has("title"))
                        messageTitle = additionalData.getString("title");
                    if (additionalData.has("actionSelected"))
                        messageBody += "\nPressed ButtonID: " + additionalData.getString("actionSelected");
                    if (additionalData.has("full"))
                        messageBody = message + "\n\nFull additionalData:\n" + additionalData.toString();
                }
            } catch (JSONException e) {
            }
            final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (! (isInFronground() && pm.isScreenOn())) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(messageTitle)
                        .setMessage(messageBody)
                        .setCancelable(true)
                        .setPositiveButton("OK", null)
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        })
                        .create().show();
            }

        }
    }


    class MyResourceClient extends XWalkResourceClient {
        MyResourceClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onLoadFinished(XWalkView view, String url) {
            super.onLoadFinished(view, url);
            MainActivity.this.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    spinner.setVisibility(View.GONE);
                    MainActivity.this.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(new Intent(Constants.Notification.CANCEL_ALARM_NOTIF));
                        }
                    }, 10 * 1000);
                }
            }, 3000);
        }
    }

    class MyUIClient extends XWalkUIClient {
        MyUIClient(XWalkView view) {
            super(view);
        }

        @Override
        public void onFullscreenToggled(XWalkView view, boolean enterFullscreen){
            super.onFullscreenToggled(view, enterFullscreen);
        }
    }


    @Inject protected BootstrapServiceProvider serviceProvider;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private XWalkView myXWalkWebView;
    private int outletID;
    private ScheduledFuture scheduledFutureGetId;
    private ScheduledFuture scheduledFutureReload;
    static final String TAG = "MainActvivity";

    String SENDER_ID = "264050289553";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String regid;
    private ProgressBar spinner;
    private Handler handler;


    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        ((BigSpoonApp)getApplicationContext()).wakeDevice();
    }

    private void sleepScreen() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        ((BigSpoonApp)getApplicationContext()).releaseWakeLock();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // register for push
        OneSignal.init(this, "760620824085", "c412366e-e725-11e4-9685-ef6b78def1f2", new ExampleNotificationOpenedHandler());
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // View injection with Butterknife
        Views.inject(this);
        setupDrawer();
        handler = new Handler();

        myXWalkWebView = (XWalkView)findViewById(R.id.xwalkWebView);
        myXWalkWebView.clearCache(true);
        myXWalkWebView.setResourceClient(new MyResourceClient(myXWalkWebView));
        myXWalkWebView.setUIClient(new MyUIClient(myXWalkWebView));
        myXWalkWebView.load("http://bigspoon.biz/staff/main", null); //http://54.251.209.132/staff/main
        spinner.setVisibility(View.VISIBLE);
        // turn on debugging
        // XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    private void setupDrawer() {
        // Set up navigation drawer
        title = drawerTitle = getTitle();


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this,                    /* Host activity */
                drawerLayout,           /* DrawerLayout object */
                R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,    /* "open drawer" description */
                R.string.navigation_drawer_close) { /* "close drawer" description */

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        OneSignal.onPaused();
        scheduledFutureGetId.cancel(true);
        scheduledFutureReload.cancel(true);
        sleepScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.myXWalkWebView.reload(XWalkView.RELOAD_NORMAL);
        final ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        if (scheduledFutureReload != null) {
            scheduledFutureReload.cancel(true);
        }

        scheduledFutureGetId= scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.this.myXWalkWebView.evaluateJavascript("OUTLET_IDS[0];", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        System.out.println("OUTLET_ID obtained !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        System.out.println(value);
                                        System.out.println("OUTLET_ID obtained !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                        if (value != null && BGUtils.isNumeric(value)) {
                                            MainActivity.this.outletID = Integer.valueOf(value);
                                            final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
                                            final SharedPreferences.Editor edit = pref.edit();
                                            edit.putString(OUTLET_ID, value);
                                            OneSignal.sendTag(getString(R.string.OUTLET_ID), value);
                                            OneSignal.getTags(new OneSignal.GetTagsHandler() {
                                                @Override
                                                public void tagsAvailable(JSONObject jsonObject) {
                                                    final String tags = jsonObject.toString();
                                                    if (tags != null && tags.contains(getString(R.string.OUTLET_ID))) {
                                                        MainActivity.this.scheduledFutureGetId.cancel(true);
                                                    }
                                                }
                                            });
                                        }

                                    }
                                });
                            }
                        });
                    }
                }, 5, 5, TimeUnit.SECONDS);

        scheduledFutureReload  = scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.this.myXWalkWebView.reload(XWalkView.RELOAD_NORMAL);
                            }
                        });
                    }
                }, 5, 5, TimeUnit.MINUTES);

        OneSignal.onResumed();
        unlockScreen();
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            case R.id.timer:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Timer
                break;
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private boolean isInFronground() {
        final Context appContext = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(appContext.getPackageName().toString())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }

}

