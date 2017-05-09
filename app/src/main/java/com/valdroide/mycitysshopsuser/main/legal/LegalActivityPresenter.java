package com.valdroide.mycitysshopsuser.main.legal;


import android.content.Context;

import com.valdroide.mycitysshopsuser.main.legal.events.LegalActivityEvent;

public interface LegalActivityPresenter {
    void onCreate();
    void onDestroy();
    void getDataLegal(Context context);
    void onEventMainThread(LegalActivityEvent event);
}
