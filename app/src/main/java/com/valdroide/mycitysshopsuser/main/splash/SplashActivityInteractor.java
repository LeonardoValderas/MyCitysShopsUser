package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;

public interface SplashActivityInteractor {
    void validateDatePlace(Context context, Intent intent);
    void validateDateShop(Context context, Intent intent);
    void sendEmail(Context context, String comment);
    void getToken(Context context);
}
