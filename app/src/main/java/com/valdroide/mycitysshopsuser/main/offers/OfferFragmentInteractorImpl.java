package com.valdroide.mycitysshopsuser.main.offers;

import android.content.Context;

public class OfferFragmentInteractorImpl implements OfferFragmentInteractor {
    OfferFragmentRepository repository;

    public OfferFragmentInteractorImpl(OfferFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getOffers(Context context) {
        repository.getOffers(context);
    }

    @Override
    public void refreshLayout(Context context) {
        repository.refreshLayout(context);
    }

    @Override
    public void getOfferSearch(Context context, String letter) {
        repository.getOfferSearch(context, letter);
    }
}
