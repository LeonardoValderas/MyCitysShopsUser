package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.place.events.PlaceActivityEvent;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivityView;

import org.greenrobot.eventbus.Subscribe;

public class PlaceActivityPresenterImpl implements PlaceActivityPresenter {


    private PlaceActivityView view;
    private EventBus eventBus;
    private PlaceActivityInteractor interactor;

    public PlaceActivityPresenterImpl(PlaceActivityView view, EventBus eventBus, PlaceActivityInteractor interactor) {
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
    public void getCountries(Context context) {
        interactor.getCountries(context);
    }

    @Override
    public void getStateForCountry(Context context, int id_country) {
        interactor.getStateForCountry(context, id_country);
    }

    @Override
    public void getCitiesForState(Context context, int id_state) {
        interactor.getCitiesForState(context, id_state);
    }

    @Override
    public void savePlace(Context context, MyPlace place) {
        interactor.savePlace(context, place);
    }

    @Override
    @Subscribe
    public void onEventMainThread(PlaceActivityEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case PlaceActivityEvent.GETCOUNTRIES:
                    view.setCountry(event.getCountries());
                    break;
                case PlaceActivityEvent.GETSTATES:
                    view.setState(event.getStates());
                    break;
                case PlaceActivityEvent.GETCITIES:
                    view.setCity(event.getCities());
                    break;
                case PlaceActivityEvent.SAVE:
                    view.saveSuccess(event.getPlace());
                    break;
                case PlaceActivityEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
