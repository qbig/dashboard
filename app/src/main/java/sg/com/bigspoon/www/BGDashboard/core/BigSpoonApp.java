package sg.com.bigspoon.www.BGDashboard.core;

/**
 * Created by qiaoliang89 on 20/4/15.
 */

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.PowerManager;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import sg.com.bigspoon.www.BGDashboard.BootstrapApplication;

import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.MIXPANEL_TOKEN;
import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.OUTLET_ID;
import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.PREFS_NAME;
/**
 * Created by qiaoliang89 on 20/4/15.
 */


public class BigSpoonApp extends BootstrapApplication implements Foreground.Listener {
    final Handler mHandler = new Handler();
    private MixpanelAPI mMixpanel;
    private PowerManager.WakeLock fullWakeLock;
    private PowerManager.WakeLock partialWakeLock;
    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
    }

    public void wakeDevice() {
        fullWakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }


    public void releaseWakeLock() {
        if(fullWakeLock.isHeld()){
            fullWakeLock.release();
        }
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }
    }

    private BroadcastReceiver mLocationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    private BroadcastReceiver mLocationStartServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createWakeLocks();
//        Fabric.with(this, new Crashlytics());
//        try {
//            LocationLibrary.initialiseLibrary(getBaseContext(), "sg.com.bigspoon.www");
//        } catch (UnsupportedOperationException ex) {
//            Crashlytics.logException(ex);
//        }

        Foreground.get(this).addListener(this);

        mMixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
        mMixpanel.identify(mMixpanel.getDistinctId());
        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());

        final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        if (pref.contains(OUTLET_ID)) {
            final String outletId = pref.getString(OUTLET_ID, null);
            if (outletId != null) {
                JSONObject props = new JSONObject();
                try {
                    props.put("user", outletId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Foreground.get(this).removeListener(this);
        //mMixpanel.flush();
    }

    // Foreground Callback
    @Override
    public void onBecameForeground() {
    }

    // Foreground Callback
    @Override
    public void onBecameBackground() {
    }
}
