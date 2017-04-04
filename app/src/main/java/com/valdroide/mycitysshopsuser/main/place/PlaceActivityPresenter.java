package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.main.place.events.PlaceActivityEvent;

public interface PlaceActivityPresenter {
    void onCreate();
    void onDestroy();
    void getCountries(Context context);
    void getStateForCountry(Context context, int id_country);
    void getCitiesForState(Context context, int id_state);
    void savePlace(Context context, MyPlace place);
    void onEventMainThread(PlaceActivityEvent event);
}
