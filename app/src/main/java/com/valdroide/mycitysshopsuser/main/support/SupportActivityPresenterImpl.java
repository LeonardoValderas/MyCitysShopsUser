package com.valdroide.mycitysshopsuser.main.support;

import android.content.Context;


import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsuser.main.support.ui.SupportActivityView;

import org.greenrobot.eventbus.Subscribe;

public class SupportActivityPresenterImpl implements SupportActivityPresenter {

    private SupportActivityView view;
    private EventBus eventBus;
    private SupportActivityInteractor interactor;

    public SupportActivityPresenterImpl(SupportActivityView view, EventBus eventBus, SupportActivityInteractor interactor) {
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
    public void sendEmail(Context context, String comment) {
        interactor.sendEmail(context, comment);
    }

    @Override
    @Subscribe
    public void onEventMainThread(SupportActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case SupportActivityEvent.SENDEMAILSUCCESS:
                    view.sendEmailSuccess();
                    break;
                case SupportActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
