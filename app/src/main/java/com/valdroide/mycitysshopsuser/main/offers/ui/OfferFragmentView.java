package com.valdroide.mycitysshopsuser.main.offers.ui;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;

import java.util.List;

public interface OfferFragmentView {
    void setOffers(List<Offer> offers);
    void setError(String error);
    void withoutChange();
    void setOffersRefresh();
    void showProgressDialog();
    void hideProgressDialog();
}

