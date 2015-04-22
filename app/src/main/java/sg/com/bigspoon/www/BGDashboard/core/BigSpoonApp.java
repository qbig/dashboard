package sg.com.bigspoon.www.BGDashboard.core;

/**
 * Created by qiaoliang89 on 20/4/15.
 */

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by qiaoliang89 on 20/4/15.
 */


public class BigSpoonApp extends Application implements Foreground.Listener {
    final Handler mHandler = new Handler();

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
//
//        mMixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
//        mMixpanel.identify(mMixpanel.getDistinctId());
//        mMixpanel.getPeople().identify(mMixpanel.getDistinctId());
//
//        User.getInstance(this).mMixpanel = this.mMixpanel;
//        final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
//        if (pref.contains(LOGIN_INFO_EMAIL)) {
//            final String email = pref.getString(LOGIN_INFO_EMAIL, null);
//            if (email != null) {
//                JSONObject props = new JSONObject();
//                try {
//                    props.put("user", email);
//                    //mMixpanel.track("Usage starts", props);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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
