package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.DatePlace;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultPlace;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivityRepositoryImpl implements SplashActivityRepository {
    private EventBus eventBus;
    private APIService service;
    private ResponseWS responseWS;
    private DatePlace datePlace;
    private DatePlace datePlacesWS;
    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private MyPlace place;
    private List<Offer> offers;
    private List<Shop> shops;
    private List<Category> categories;
    private List<SubCategory> subCategories;
    private List<CatSubCity> catSubCities;
    private DateUserCity dateUserCity;

    /*
       private DateUser dateUser;
       private DateUser dateUserWS;
       private Account account;
       private List<Offer> offers;
       private User user;
   */
    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context) {
        try {
            place = SQLite.select().from(MyPlace.class).querySingle();
            if (place == null) { // es la primera vez que entra o quiere cambiar de lugar
                datePlace = SQLite.select().from(DatePlace.class).querySingle();
                if (datePlace != null)
                    validateDatePlace(context, datePlace.getTABLE_DATE(), datePlace.getCOUNTRY_DATE(),
                            datePlace.getSTATE_DATE(), datePlace.getCITY_DATE());
                else  // traemos los datos sin validar fechas
                    getPlace(context);
            } else
                validateDateShop(context);
        } catch (Exception e) {
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    public void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> validateDatePlace = service.validateDatePlace(cou, sta, ci, date);
                validateDatePlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        datePlacesWS.save();
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Delete.table(Country.class);
                                        for (Country country : countries) {
                                            country.save();
                                        }
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Delete.table(State.class);
                                        for (State state : states) {
                                            state.save();
                                        }
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Delete.table(City.class);
                                        for (City city : cities) {
                                            city.save();
                                        }
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void getPlace(final Context context) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultPlace> getPlace = service.getPlace();
                getPlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Delete.table(DatePlace.class);
                                        //      for (DatePlace datePlace : datePlacesWS) {
                                        datePlacesWS.save();
                                        //    }
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Delete.table(Country.class);
                                        for (Country country : countries) {
                                            country.save();
                                        }
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Delete.table(State.class);
                                        for (State state : states) {
                                            state.save();
                                        }
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Delete.table(City.class);
                                        for (City city : cities) {
                                            city.save();
                                        }
                                    }
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void validateDateShop(Context context) {
        try {
            dateUserCity = SQLite.select().from(DateUserCity.class).querySingle();
            if (dateUserCity != null) {
                validateDateShop(context, dateUserCity);
            } else
                getShop(context, Utils.getIdCity(context));
        } catch (Exception e) {
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    public void validateDateShop(final Context context, DateUserCity dateUserCityWS) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultShop> validateDateUser = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getCAT_SUB_CITY_DATE(),
                        dateUserCityWS.getSHOP_DATE(), dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDATE_USER_CITY());
                validateDateUser.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Delete.table(DateUserCity.class);
                                        dateUserCity.save();
                                    }

                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Delete.table(Category.class);
                                        for (Category category : categories) {
                                            category.save();
                                        }
                                    } else
                                        Delete.table(Category.class);

                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Delete.table(SubCategory.class);
                                        for (SubCategory subCategory : subCategories) {
                                            subCategory.save();
                                        }
                                    } else
                                        Delete.table(SubCategory.class);

                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Delete.table(CatSubCity.class);
                                        for (CatSubCity catSubCity : catSubCities) {
                                            catSubCity.save();
                                        }
                                    } else
                                        Delete.table(CatSubCity.class);

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Delete.table(Shop.class);
                                        for (Shop shop : shops) {
                                            shop.save();
                                        }
                                    } else
                                        Delete.table(Shop.class);

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Delete.table(Offer.class);
                                        for (Offer offer : offers) {
                                            offer.save();
                                        }
                                    } else
                                        Delete.table(Offer.class);

                                    post(SplashActivityEvent.GOTONAV);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void getShop(final Context context, int id_city) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResultShop> getShop = service.getShop(id_city);
                getShop.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                if (responseWS.getSuccess().equals("0")) {
                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Delete.table(DateUserCity.class);
                                        dateUserCity.save();
                                    }
                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Delete.table(Category.class);
                                        for (Category category : categories) {
                                            category.save();
                                        }
                                    }
                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Delete.table(SubCategory.class);
                                        for (SubCategory subCategory : subCategories) {
                                            subCategory.save();
                                        }
                                    }
                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Delete.table(CatSubCity.class);
                                        for (CatSubCity catSubCity : catSubCities) {
                                            catSubCity.save();
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Delete.table(Shop.class);
                                        for (Shop shop : shops) {
                                            shop.save();
                                        }
                                    }
                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Delete.table(Offer.class);
                                        for (Offer offer : offers) {
                                            offer.save();
                                        }
                                    }
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            post(SplashActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            post(SplashActivityEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public void post(int type) {
        post(type, null);
    }

    public void post(int type, String error) {
        SplashActivityEvent event = new SplashActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
