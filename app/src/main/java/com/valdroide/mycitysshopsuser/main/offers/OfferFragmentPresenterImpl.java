package com.valdroide.mycitysshopsuser.main.offers;


import android.content.Context;

import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.offers.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class OfferFragmentPresenterImpl implements OfferFragmentPresenter {
    OfferFragmentView view;
    EventBus eventBus;
    OfferFragmentInteractor interactor;

    public OfferFragmentPresenterImpl(OfferFragmentView view, EventBus eventBus, OfferFragmentInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getOffers(Context context) {
        interactor.getOffers(context);
    }

    @Override
    public OfferFragmentView getView() {
        return this.view;
    }

    @Override
    public void refreshLayout(Context context) {
        interactor.refreshLayout(context);
    }

    @Override
    public void getOfferSearch(Context context, String letter) {
        interactor.getOfferSearch(context, letter);
    }

    @Subscribe
    @Override
    public void onEventMainThread(OfferFragmentEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case OfferFragmentEvent.OFFERS:
                    view.setOffers(event.getOfferList());
                    view.hideProgressDialog();
                    break;
                case OfferFragmentEvent.ERROR:
                    view.hideProgressDialog();
                    view.setError(event.getError());
                    break;
                case OfferFragmentEvent.WITHOUTCHANGE:
                    view.hideProgressDialog();
                    view.withoutChange();
                    break;
                case OfferFragmentEvent.GETOFFERSREFRESH:
                    view.hideProgressDialog();
                    view.setOffersRefresh();
                    break;
            }
        }
    }
}
