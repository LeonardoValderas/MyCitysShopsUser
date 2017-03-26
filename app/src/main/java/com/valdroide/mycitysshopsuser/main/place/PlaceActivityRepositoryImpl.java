package com.valdroide.mycitysshopsuser.main.place;

import android.content.Context;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.place.events.PlaceActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

public class PlaceActivityRepositoryImpl implements PlaceActivityRepository {
    private EventBus eventBus;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private APIService service;

    public PlaceActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getCountries() {
        try {
            countries = SQLite.select().from(Country.class).where().orderBy(new NameAlias("COUNTRY"), true).queryList();
            if (countries != null)
                post(PlaceActivityEvent.GETCOUNTRIES, countries);
            else
                post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getStateForCountry(int id_country) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_COUNTRY_FOREIGN")).is(id_country));
        try {
            states = SQLite.select().from(State.class).where(conditionGroup).orderBy(new NameAlias("STATE"), true).queryList();

            if (states != null)
                post(PlaceActivityEvent.GETSTATES, states, true);
            else
                post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getCitiesForState(int id_state) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ID_STATE_FOREIGN")).is(id_state));
        try {
            cities = SQLite.select().from(City.class).where(conditionGroup).orderBy(new NameAlias("CITY"), true).queryList();
            if (cities != null)
                post(PlaceActivityEvent.GETCITIES, 0, cities);
            else
                post(PlaceActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void savePlace(Context context, final MyPlace place) {
        try {
            place.save();
            Utils.setIdCity(context, place.getID_CITY_FOREIGN());
            post(PlaceActivityEvent.SAVE, place);
        } catch (Exception e) {
            post(PlaceActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type, MyPlace place) {
        post(type, null, null, null, null, place);
    }

    public void post(int type, List<Country> countries) {
        post(type, countries, null, null, null, null);
    }

    public void post(int type, List<State> states, boolean isSub) {
        post(type, null, states, null, null, null);
    }

    public void post(int type, int nothing, List<City> cities) {
        post(type, null, null, cities, null, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, error, null);
    }

    public void post(int type, List<Country> countries, List<State> states, List<City> cities, String error, MyPlace place) {
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
