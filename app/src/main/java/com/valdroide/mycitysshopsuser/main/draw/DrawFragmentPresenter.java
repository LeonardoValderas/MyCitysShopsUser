package com.valdroide.mycitysshopsuser.main.draw;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.main.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragmentView;

public interface DrawFragmentPresenter {
    void onCreate();
    void onDestroy();
    void getDraws(Context context);
    DrawFragmentView getView();
    void participate(Context context, Draw draw, String dni, String name);
    void refreshLayout(Context context);
    void getDrawSearch(Context context, String letter);
    void onEventMainThread(DrawFragmentEvent event);
}
