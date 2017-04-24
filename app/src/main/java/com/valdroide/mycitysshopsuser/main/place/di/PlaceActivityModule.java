package com.valdroide.mycitysshopsuser.main.place.di;

import android.app.Activity;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityInteractor;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityInteractorImpl;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityPresenter;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityPresenterImpl;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityRepository;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivityView;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerCity;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerCountry;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class PlaceActivityModule {
    PlaceActivityView view;
    Activity activity;

    public PlaceActivityModule(PlaceActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    PlaceActivityView providePlaceActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    PlaceActivityPresenter providePlaceActivityPresenter(PlaceActivityView view, EventBus eventBus, PlaceActivityInteractor interactor) {
        return new PlaceActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    PlaceActivityInteractor providePlaceActivityInteractor(PlaceActivityRepository repository) {
        return new PlaceActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    PlaceActivityRepository providePlaceActivityRepository(EventBus eventBus) {
        return new PlaceActivityRepositoryImpl(eventBus);
    }

    @Provides
    @Singleton
    List<Country> provideCountryList() {
        return new ArrayList<Country>();
    }

    @Provides
    @Singleton
    List<State> provideStateList() {
        return new ArrayList<State>();
    }

    @Provides
    @Singleton
    List<City> provideCityList() {
        return new ArrayList<City>();
    }

    @Provides
    @Singleton
    int providesResource() {
        return R.layout.support_simple_spinner_dropdown_item;
    }

    @Provides
    @Singleton
    AdapterSpinnerCountry provideAdapterSpinnerCountry(Activity context, int resource, List<Country> countries) {
        return new AdapterSpinnerCountry(context, resource, countries);
    }


    @Provides
    @Singleton
    AdapterSpinnerState provideAdapterSpinnerState(Activity context, int resource, List<State> states) {
        return new AdapterSpinnerState(context, resource, states);
    }


    @Provides
    @Singleton
    AdapterSpinnerCity provideAdapterSpinnerCity(Activity context, int resource, List<City> cities) {
        return new AdapterSpinnerCity(context, resource, cities);
    }
}
