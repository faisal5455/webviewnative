package com.app.webapp;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.webapp.util.LogUtil;
import com.google.android.gms.ads.MobileAds;
//import com.onesignal.Continue;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import org.json.JSONObject;
import com.onesignal.debug.LogLevel;
import com.onesignal.notifications.INotificationClickEvent;
import com.onesignal.notifications.INotificationClickListener;

import java.lang.reflect.Method;

public class MyApp extends Application {

    private static final String ONESIGNAL_APP_ID = "fcb81c51-2b0f-4510-9e14-06cd38d60a61";

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);

//        OneSignal.initWithContext(this);

//        OneSignal.setAppId(ONESIGNAL_APP_ID);
//        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
            if (r.isSuccess()) {
                if (r.getData()) {
                    Log.e("onesignal", "permission given");
                    OneSignal.getUser().getPushSubscription().optIn();
                    // `requestPermission` completed successfully and the user has accepted permission
                }
                else {
                    // `requestPermission` completed successfully but the user has rejected permission
                    Log.e("onesignal", "rejected123");
                }
            }
            else {
                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                Log.e("onesignal", "failure123");
            }
        }));

        OneSignal.getNotifications().addClickListener(new INotificationClickListener() {
            @Override
            public void onClick(@NonNull INotificationClickEvent iNotificationClickEvent) {
                JSONObject data = iNotificationClickEvent.getNotification().getAdditionalData();
                String customKey;
                if (data != null) {
                    customKey = data.optString("customkey", null);
                    if (customKey != null)
                        LogUtil.loge("customkey set with value: " + customKey);
                    else
                        LogUtil.loge("customkey null");
                }
                sendBroadcast(new Intent("close"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyApp.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(iNotificationClickEvent.getNotification().getLaunchURL() != null)
                            intent.putExtra("launchURL", iNotificationClickEvent.getNotification().getLaunchURL());
                        startActivity(intent);
                    }
                }, 500);
            }
        });


//        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);
//        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
//            @Override
//            public void notificationOpened(OSNotificationOpenedResult result) {
//                LogUtil.loge("notificationOpened");
//                LogUtil.loge("title: "+result.getNotification().getTitle());
//                LogUtil.loge("body: "+result.getNotification().getBody());
//                LogUtil.loge("launchUrl: "+result.getNotification().getLaunchURL());
//                JSONObject data = result.getNotification().getAdditionalData();
//                String customKey;
//                if (data != null) {
//                    customKey = data.optString("customkey", null);
//                    if (customKey != null)
//                        LogUtil.loge("customkey set with value: " + customKey);
//                    else
//                        LogUtil.loge("customkey null");
//                }
//                sendBroadcast(new Intent("close"));
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(MyApp.this, Home.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        if(result.getNotification().getLaunchURL() != null)
//                            intent.putExtra("launchURL", result.getNotification().getLaunchURL());
//                        startActivity(intent);
//                    }
//                }, 500);
//            }
//        });

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
