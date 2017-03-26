package com.valdroide.mycitysshopsuser.entities.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.DatePlace;
import com.valdroide.mycitysshopsuser.entities.place.State;

import java.util.List;

public class ResultPlace {
    @SerializedName("responseWS")
    @Expose
    ResponseWS responseWS;
    @SerializedName("date_place")
    @Expose
    DatePlace datePlace;
    @SerializedName("country")
    @Expose
    List<Country> countries;
    @SerializedName("state")
    @Expose
    List<State> states;
    @SerializedName("city")
    @Expose
    List<City> cities;

    public ResponseWS getResponseWS() {
        return responseWS;
    }

    public void setResponseWS(ResponseWS responseWS) {
        this.responseWS = responseWS;
    }

    public DatePlace getDatePlace() {
        return datePlace;
    }

    public void setDatePlace(DatePlace datePlace) {
        this.datePlace = datePlace;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
