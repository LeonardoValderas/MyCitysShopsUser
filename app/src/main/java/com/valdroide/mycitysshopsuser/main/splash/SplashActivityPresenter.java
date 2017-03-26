package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;

import com.valdroide.mycitysshopsuser.main.splash.events.SplashActivityEvent;

public interface SplashActivityPresenter {
    void onCreate();
    void onDestroy();
    void validateDatePlace(Context context);
    void validateDateShop(Context context);
    void onEventMainThread(SplashActivityEvent event);
}
