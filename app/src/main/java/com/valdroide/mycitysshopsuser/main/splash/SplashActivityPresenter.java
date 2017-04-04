package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;

import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.main.splash.events.SplashActivityEvent;

public interface SplashActivityPresenter {
    void onCreate();
    void onDestroy();
    void validateDatePlace(Context context, Intent intent);
    void validateDateShop(Context context, Intent intent);
    void sendEmail(Context context, String comment);
    void getToken(Context context);
    void onEventMainThread(SplashActivityEvent event);
}
