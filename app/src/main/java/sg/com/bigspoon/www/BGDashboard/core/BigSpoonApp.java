package sg.com.bigspoon.www.BGDashboard.core;

/**
 * Created by qiaoliang89 on 20/4/15.
 */

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.MIXPANEL_TOKEN;
import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.PREFS_NAME;
import static sg.com.bigspoon.www.BGDashboard.core.Constants.Auth.OUTLET_ID;
/**
 * Created by qiaoliang89 on 20/4/15.
 */


public class BigSpoonApp extends Application implements Foreground.Listener {
    final Handler mHandler = new Handler();
    private MixpanelAPI mMixpanel;

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
