

package sg.com.bigspoon.www.BGDashboard.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.otto.Subscribe;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import butterknife.Views;
import sg.com.bigspoon.www.BGDashboard.BootstrapServiceProvider;
import sg.com.bigspoon.www.BGDashboard.R;
import sg.com.bigspoon.www.BGDashboard.events.NavItemSelectedEvent;
import sg.com.bigspoon.www.BGDashboard.util.BGUtils;
import sg.com.bigspoon.www.BGDashboard.util.Ln;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link sg.com.bigspoon.www.BGDashboard.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject protected BootstrapServiceProvider serviceProvider;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private XWalkView myXWalkWebView;
    private int outletID;
    private ScheduledFuture scheduledFuture;
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        context = getApplicationContext();
        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        Views.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
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
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        myXWalkWebView = (XWalkView)findViewById(R.id.xwalkWebView);
        myXWalkWebView.clearCache(true);
        myXWalkWebView.load("http://bigspoon.biz/staff/main", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        getSupportActionBar().hide();

        //checkAuth();
        gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            registerInBackground();
        } else {
            System.out.println("///////////////////////////////////////////////////////////////");
            System.out.println(regid);
            System.out.println("///////////////////////////////////////////////////////////////");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        final ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduledFuture = scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.this.myXWalkWebView.evaluateJavascript("OUTLET_IDS[0];", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        System.out.println(value);
                                        Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
                                        if (value != null && BGUtils.isNumeric(value)) {
                                            MainActivity.this.outletID = Integer.valueOf(value);
                                        }
                                        MainActivity.this.scheduledFuture .cancel(true);
                                    }
                                });
                            }
                        });
                    }
                }, 0, 5, TimeUnit.SECONDS);


    }

    private boolean isTablet() {
        return false; //UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() {
        if (userHasAuthenticated) {

            Ln.d("Foo");
//            final FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, new XxxFragment())
//                    x
        }

    }

//    private void checkAuth() {
//        new SafeAsyncTask<Boolean>() {
//
//            @Override
//            public Boolean call() throws Exception {
//                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
//                return svc != null;
//            }
//
//            @Override
//            protected void onException(final Exception e) throws RuntimeException {
//                super.onException(e);
//                if (e instanceof OperationCanceledException) {
//                    // User cancelled the authentication process (back button, etc).
//                    // Since auth could not take place, lets finish this activity.
//                    finish();
//                }
//            }
//
//            @Override
//            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
//                super.onSuccess(hasAuthenticated);
//                userHasAuthenticated = true;
//                initScreen();
//            }
//        }.execute();
//    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
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
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        return registrationId;
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

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                System.out.println("///////////////////////////////////////////////////////////////");
                System.out.println(getRegistrationId(MainActivity.this));
                System.out.println("///////////////////////////////////////////////////////////////");
            }
        }.execute(null, null, null);

    }

    private void sendRegistrationIdToBackend() {
        // TODO
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.commit();
    }
}

