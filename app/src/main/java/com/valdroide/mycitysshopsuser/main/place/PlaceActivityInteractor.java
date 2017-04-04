package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;

public interface PlaceActivityInteractor {
    void getCountries(Context context);
    void getStateForCountry(Context context, int id_country);
    void getCitiesForState(Context context, int id_state);
    void savePlace(Context context, MyPlace place);
}
