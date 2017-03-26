package com.valdroide.mycitysshopsuser.main.place.ui;

import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;

import java.util.List;

public interface PlaceActivityView {
    void setCountry(List<Country> countries);
    void setState(List<State> states);
    void setCity(List<City> cities);
    void setError(String mgs);
    void savePlace();
    void saveSuccess(MyPlace place);
}
