package com.app.webapp.util;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.app.webapp.Home;
import com.app.webapp.R;
import com.app.webapp.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdUtil {

    public static void loadBannerAd(Home activity, ViewGroup adParent) {
        if(Settings.BANNER_AD){
            adParent.removeAllViews();
            AdRequest adRequest = new AdRequest.Builder().build();
            AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(activity.getResources().getString(R.string.admobBannerId));
            adView.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
            adView.loadAd(adRequest);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
            adParent.addView(adView, params);
        }
    }

    public static void loadInterstitialAd(Home activity){
        LogUtil.loge("loadInterstitialAd");
        Home.linkClicks = 0;
        if(Settings.INTERSTITIAL_AD){
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, activity.getResources().getString(R.string.admobInterstitialId), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    interstitialAd.show(activity);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.d("ad failed", loadAdError.toString());
                }});
        }
    }
}
