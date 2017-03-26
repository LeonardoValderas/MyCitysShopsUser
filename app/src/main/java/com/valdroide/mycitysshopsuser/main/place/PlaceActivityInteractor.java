package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;

public interface PlaceActivityInteractor {
    void getCountries();
    void getStateForCountry(int id_country);
    void getCitiesForState(int id_state);
    void savePlace(Context context, MyPlace place);
}
