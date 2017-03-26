package com.valdroide.mycitysshopsuser.main.offer;

import com.valdroide.mycitysshopsuser.main.offer.events.OfferActivityEvent;

public interface OfferActivityPresenter {
    void onCreate();
    void onDestroy();
    void getListOffer(int id_shop);
    void onEventMainThread(OfferActivityEvent event);
}
