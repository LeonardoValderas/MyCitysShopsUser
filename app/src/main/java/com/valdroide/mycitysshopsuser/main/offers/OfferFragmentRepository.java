package com.valdroide.mycitysshopsuser.main.offers;


import android.content.Context;

public interface OfferFragmentRepository {
    void getOffers(Context context);
    void refreshLayout(Context context);
    void getOfferSearch(Context context, String letter);
}
