package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivityView;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivityPresenterImpl implements SplashActivityPresenter {


    private SplashActivityView view;
    private EventBus eventBus;
    private SplashActivityInteractor interactor;

    public SplashActivityPresenterImpl(SplashActivityView view, EventBus eventBus, SplashActivityInteractor interactor) {
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
    public void validateDatePlace(Context context, Intent intent) {
        interactor.validateDatePlace(context, intent);
    }

    @Override
    public void validateDateShop(Context context, Intent intent) {
        interactor.validateDateShop(context, intent);
    }

    @Override
    public void sendEmail(Context context, String comment) {
        interactor.sendEmail(context, comment);
    }

    @Override
    public void getToken(Context context) {
        interactor.getToken(context);
    }

    @Override
    @Subscribe
    public void onEventMainThread(SplashActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case SplashActivityEvent.GOTONAV:
                    view.goToNav();
                    break;
                case SplashActivityEvent.GOTOPLACE:
                    view.goToPlace();
                    break;
                case SplashActivityEvent.TOKENSUCCESS:
                    view.tokenSuccess();
                    break;
                case SplashActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
