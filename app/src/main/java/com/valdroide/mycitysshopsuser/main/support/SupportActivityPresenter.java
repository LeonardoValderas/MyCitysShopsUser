package com.valdroide.mycitysshopsuser.main.support;

import android.content.Context;

import com.valdroide.mycitysshopsuser.main.support.events.SupportActivityEvent;

public interface SupportActivityPresenter {
    void onCreate();
    void onDestroy();
    void sendEmail(Context context, String comment);
    void onEventMainThread(SupportActivityEvent event);
}
