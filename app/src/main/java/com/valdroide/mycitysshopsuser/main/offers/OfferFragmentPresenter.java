package com.valdroide.mycitysshopsuser.main.offers;

import android.content.Context;

import com.valdroide.mycitysshopsuser.main.offers.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragmentView;

public interface OfferFragmentPresenter {
    void onCreate();
    void onDestroy();
    void getOffers(Context context);
    OfferFragmentView getView();
    void refreshLayout(Context context);
    void getOfferSearch(Context context, String letter);
    void onEventMainThread(OfferFragmentEvent event);
}
