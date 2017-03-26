package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;

public class PlaceActivityInteractorImpl implements PlaceActivityInteractor {

    private PlaceActivityRepository repository;

    public PlaceActivityInteractorImpl(PlaceActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCountries() {
        repository.getCountries();
    }

    @Override
    public void getStateForCountry(int id_country) {
        repository.getStateForCountry(id_country);
    }

    @Override
    public void getCitiesForState(int id_state) {
        repository.getCitiesForState(id_state);
    }

    @Override
    public void savePlace(Context context, MyPlace place) {
        repository.savePlace(context, place);
    }
}
