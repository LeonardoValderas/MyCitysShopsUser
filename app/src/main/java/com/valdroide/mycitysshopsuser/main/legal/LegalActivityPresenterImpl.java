package com.valdroide.mycitysshopsuser.main.legal;

import android.content.Context;

import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.legal.events.LegalActivityEvent;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivityView;

import org.greenrobot.eventbus.Subscribe;

public class LegalActivityPresenterImpl implements LegalActivityPresenter {
    EventBus eventBus;
    LegalActivityView view;
    LegalActivityInteractor interactor;

    public LegalActivityPresenterImpl(EventBus eventBus, LegalActivityView view, LegalActivityInteractor interactor) {
        this.eventBus = eventBus;
        this.view = view;
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
    public void getDataLegal(Context context) {
        interactor.getDataLegal(context);
    }

    @Subscribe
    @Override
    public void onEventMainThread(LegalActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case LegalActivityEvent.DATASUCCESS:
                    view.setLegal(event.getAbout(), event.getLegal());
                    break;
                case LegalActivityEvent.ERRRO:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
