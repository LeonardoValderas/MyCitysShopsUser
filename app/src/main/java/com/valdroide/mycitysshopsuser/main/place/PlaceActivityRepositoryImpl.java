package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.City_Table;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.Country_Table;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.entities.place.State_Table;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.place.events.PlaceActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

public class PlaceActivityRepositoryImpl implements PlaceActivityRepository {
    private EventBus eventBus;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;


    public PlaceActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getCountries(Context context) {
        Utils.writelogFile(context, "getCountries(Place, Repository)");
        try {
            countries = SQLite.select().from(Country.class).where().orderBy(Country_Table.COUNTRY, true).queryList();
            if (countries != null) {
                Utils.writelogFile(context, "countries != null(Place, Repository)");
                post(PlaceActivityEvent.GETCOUNTRIES, countries);
            } else {
                Utils.writelogFile(context, "Base de datos error(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Place, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getStateForCountry(Context context, int id_country) {
        Utils.writelogFile(context, "getStateForCountry(Place, Repository)");
        try {
            states = SQLite.select().from(State.class).where(State_Table.ID_COUNTRY_FOREIGN.is(id_country))
                    .orderBy(State_Table.STATE, true).queryList();

            if (states != null) {
                Utils.writelogFile(context, "states != null(Place, Repository)");
                post(PlaceActivityEvent.GETSTATES, states, true);
            } else {
                Utils.writelogFile(context, "Base de datos error(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Place, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getCitiesForState(Context context, int id_state) {
        Utils.writelogFile(context, "getCitiesForState(Place, Repository)");
        try {
            cities = SQLite.select().from(City.class).where(City_Table.ID_STATE_FOREIGN.is(id_state))
                    .orderBy(City_Table.CITY, true).queryList();
            if (cities != null) {
                Utils.writelogFile(context, "cities != null(Place, Repository)");
                post(PlaceActivityEvent.GETCITIES, 0, cities);
            } else {
                Utils.writelogFile(context, "Base de datos error(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Place, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void savePlace(Context context, final MyPlace place) {
        Utils.writelogFile(context, "savePlace(Place, Repository)");
        try {
            if (place != null) {
                Utils.writelogFile(context, "place != null y save place(Place, Repository)");
                place.save();
                Utils.setIdCity(context, place.getID_CITY_FOREIGN());
                post(PlaceActivityEvent.SAVE, place);
            } else {
                Utils.writelogFile(context, "Base de datos error(Place, Repository)");
                post(PlaceActivityEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Place, Repository)");
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type, MyPlace place) {
        post(type, null, null, null, null, place);
    }

    private void post(int type, List<Country> countries) {
        post(type, countries, null, null, null, null);
    }

    private void post(int type, List<State> states, boolean isSub) {
        post(type, null, states, null, null, null);
    }

    private void post(int type, int nothing, List<City> cities) {
        post(type, null, null, cities, null, null);
    }

    private void post(int type, String error) {
        post(type, null, null, null, error, null);
    }

    private void post(int type, List<Country> countries, List<State> states, List<City> cities, String error, MyPlace place) {
        PlaceActivityEvent event = new PlaceActivityEvent();
        event.setType(type);
        event.setCountries(countries);
        event.setStates(states);
        event.setCities(cities);
        event.setError(error);
        event.setPlace(place);
        eventBus.post(event);
    }
}
