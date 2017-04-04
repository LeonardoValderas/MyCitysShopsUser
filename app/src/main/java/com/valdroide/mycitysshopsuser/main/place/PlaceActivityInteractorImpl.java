package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;

public class PlaceActivityInteractorImpl implements PlaceActivityInteractor {

    private PlaceActivityRepository repository;

    public PlaceActivityInteractorImpl(PlaceActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCountries(Context context) {
        repository.getCountries(context);
    }

    @Override
    public void getStateForCountry(Context context, int id_country) {
        repository.getStateForCountry(context, id_country);
    }

    @Override
    public void getCitiesForState(Context context, int id_state) {
        repository.getCitiesForState(context, id_state);
    }

    @Override
    public void savePlace(Context context, MyPlace place) {
        repository.savePlace(context, place);
    }
}
