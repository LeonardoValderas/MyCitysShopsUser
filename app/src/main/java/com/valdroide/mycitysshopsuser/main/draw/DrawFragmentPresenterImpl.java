package com.valdroide.mycitysshopsuser.main.draw;


import android.content.Context;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragmentView;

import org.greenrobot.eventbus.Subscribe;

public class DrawFragmentPresenterImpl implements DrawFragmentPresenter {
    DrawFragmentView view;
    EventBus eventBus;
    DrawFragmentInteractor interactor;

    public DrawFragmentPresenterImpl(DrawFragmentView view, EventBus eventBus, DrawFragmentInteractor interactor) {
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
    public void getDraws(Context context) {
        interactor.getDraws(context);
    }

    @Override
    public DrawFragmentView getView() {
        return this.view;
    }

    @Override
    public void participate(Context context, Draw draw, String dni, String name) {
        interactor.participate(context, draw, dni, name);
    }

    @Override
    public void refreshLayout(Context context) {
        interactor.refreshLayout(context);
    }

    @Override
    public void getDrawSearch(Context context, String letter) {
        interactor.getDrawSearch(context, letter);
    }

    @Subscribe
    @Override
    public void onEventMainThread(DrawFragmentEvent event) {
        if (view != null) {
            switch (event.getType()) {
                case DrawFragmentEvent.DRAWS:
                    view.setDraws(event.getDrawList());
                    view.hideProgressDialog();
                    break;
                case DrawFragmentEvent.PARTICIPATESUCCESS:
                    view.hideProgressDialog();
                    view.participationSuccess();
                    break;
                case DrawFragmentEvent.ERROR:
                    view.hideProgressDialog();
                    view.setError(event.getError());
                    break;
                case DrawFragmentEvent.WITHOUTCHANGE:
                    view.hideProgressDialog();
                    view.withoutChange();
                    break;
                case DrawFragmentEvent.GETDRAWSREFRESH:
                    view.hideProgressDialog();
                    view.setDrawsRefresh();
                    break;
            }
        }
    }
}
