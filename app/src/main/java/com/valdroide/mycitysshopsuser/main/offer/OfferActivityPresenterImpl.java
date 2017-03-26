package com.valdroide.mycitysshopsuser.main.offer;

import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;
import com.valdroide.mycitysshopsuser.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsuser.main.offer.ui.OfferActivityView;

import org.greenrobot.eventbus.Subscribe;

public class OfferActivityPresenterImpl implements OfferActivityPresenter {

    private OfferActivityView view;
    private EventBus eventBus;
    private OfferActivityInteractor interactor;

    public OfferActivityPresenterImpl(OfferActivityView view, EventBus eventBus, OfferActivityInteractor interactor) {
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
    public void getListOffer(int id_shop) {
        interactor.getListOffer(id_shop);
    }

    @Override
    @Subscribe
    public void onEventMainThread(OfferActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case OfferActivityEvent.GETLISTOFFER:
                    view.setListOffer(event.getOfferList());
                    break;
                case FragmentMainEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
