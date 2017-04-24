package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
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
import com.valdroide.mycitysshopsuser.entities.shop.Support;
import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.splash.events.SplashActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

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
    private Support support;

    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context, Intent intent) {
        Utils.writelogFile(context, "validateDatePlace(Splash, Repository)");
        try {
            place = SQLite.select().from(MyPlace.class).querySingle();
            if (place == null) { // es la primera vez que entra o quiere cambiar de lugar
                Utils.writelogFile(context, "place==null(Splash, Repository)");
                datePlace = SQLite.select().from(DatePlace.class).querySingle();
                if (datePlace != null) {
                    Utils.writelogFile(context, "datePlace != null y validateDatePlace(Splash, Repository)");
                    validateDatePlace(context, datePlace.getTABLE_DATE(), datePlace.getCOUNTRY_DATE(),
                            datePlace.getSTATE_DATE(), datePlace.getCITY_DATE());
                } else { // traemos los datos sin validar fechas
                    Utils.writelogFile(context, "datePlace == null y getPlace(Splash, Repository)");
                    getPlace(context);
                }
            } else {
                Utils.writelogFile(context, "place != null y validateDateShop(Splash, Repository)");
                validateDateShop(context, intent);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    public void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
        Utils.writelogFile(context, "Metodo validateDatePlace y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDatePlace(Splash, Repository)");
                Call<ResultPlace> validateDatePlace = service.validateDatePlace(cou, sta, ci, date);
                validateDatePlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDatePlace(Splash, Repository)");
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Utils.writelogFile(context, "getDatePlace != null y delete DatePlace(Splash, Repository)");
                                        Delete.table(DatePlace.class);
                                        Utils.writelogFile(context, "delete DatePlace ok y save datePlacesWS(Splash, Repository)");
                                        datePlacesWS.save();
                                        Utils.writelogFile(context, "save datePlacesWS y getCountries(Splash, Repository)");
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Utils.writelogFile(context, "countries != null y delete Country(Splash, Repository)");
                                        Delete.table(Country.class);
                                        Utils.writelogFile(context, "delete Country ok y For countries(Splash, Repository)");
                                        for (Country country : countries) {
                                            Utils.writelogFile(context, "save country: " + country.getID_COUNTRY_KEY() + " (Splash, Repository)");
                                            country.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR countries y getStates (Splash, Repository)");
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y For states(Splash, Repository)");
                                        for (State state : states) {
                                            Utils.writelogFile(context, "save state: " + state.getID_STATE_KEY() + " (Splash, Repository)");
                                            state.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR state y getCities(Splash, Repository)");
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y For cities(Splash, Repository)");
                                        for (City city : cities) {
                                            Utils.writelogFile(context, "save city: " + city.getID_CITY_KEY() + " (Splash, Repository)");
                                            city.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR cities(Splash, Repository)");
                                    }
                                    Utils.writelogFile(context, "post GOTOPLACE (Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4)(Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    public void getPlace(final Context context) {
        Utils.writelogFile(context, "Metodo validateDatePlace y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDatePlace(Splash, Repository)");
                Call<ResultPlace> getPlace = service.getPlace();
                getPlace.enqueue(new Callback<ResultPlace>() {
                    @Override
                    public void onResponse(Call<ResultPlace> call, Response<ResultPlace> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDatePlace(Splash, Repository)");
                                    datePlacesWS = response.body().getDatePlace();
                                    if (datePlacesWS != null) {
                                        Utils.writelogFile(context, "getDatePlace != null y delete DatePlace(Splash, Repository)");
                                        Delete.table(DatePlace.class);
                                        Utils.writelogFile(context, "delete DatePlace ok y save datePlacesWS(Splash, Repository)");
                                        datePlacesWS.save();
                                        Utils.writelogFile(context, "save datePlacesWS y getCountries(Splash, Repository)");
                                    }
                                    countries = response.body().getCountries();
                                    if (countries != null) {
                                        Utils.writelogFile(context, "countries != null y delete Country(Splash, Repository)");
                                        Delete.table(Country.class);
                                        Utils.writelogFile(context, "delete Country ok y save countries(Splash, Repository)");
                                        for (Country country : countries) {
                                            Utils.writelogFile(context, "save country: " + country.getID_COUNTRY_KEY() + " (Splash, Repository)");
                                            country.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR country y getStates (Splash, Repository)");
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y save states(Splash, Repository)");
                                        for (State state : states) {
                                            Utils.writelogFile(context, "save state: " + state.getID_STATE_KEY() + " (Splash, Repository)");
                                            state.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR state y getCities (Splash, Repository)");
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y save city(Splash, Repository)");

                                        for (City city : cities) {
                                            Utils.writelogFile(context, "save city: " + city.getID_CITY_KEY() + " (Splash, Repository)");
                                            city.save();
                                        }
                                        Utils.writelogFile(context, "fin FOR city(Splash, Repository)");
                                    }
                                    Utils.writelogFile(context, "post GOTOPLACE (Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void validateDateShop(Context context, Intent intent) {
        Utils.writelogFile(context, "Metodo validateDateShop(Splash, Repository)");
        try {
            dateUserCity = SQLite.select().from(DateUserCity.class).querySingle();
            if (dateUserCity != null) {
                Utils.writelogFile(context, "dateUserCity != null y validateDateShop(Splash, Repository)");
                validateDateShop(context, dateUserCity, intent);
            } else {
                Utils.writelogFile(context, "dateUserCity == null y getShop(Splash, Repository)");
                getShop(context, Utils.getIdCity(context));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void sendEmail(final Context context, final String comment) {
        Utils.writelogFile(context, "sendEmail y getSupport(Splash, Repository)");
        support = getSupport();
        if (support == null) {
            Utils.writelogFile(context, "support==null(Splash, Repository)");
            support = new Support();
            support.setFROM("valdroide.soporte@gmail.com");
            support.setTO("aplicacionesandroid15@gmail.com");
            support.setPASS("Vsupport2017");
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Utils.writelogFile(context, "sendEmail(Splash, Repository)");
                sendEmail(context, support, "Usuario", comment);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void getToken(Context context) {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Utils.writelogFile(context, "FirebaseInstanceId.getInstance().getToken()(Splash, Repository)");
        if (recent_token != null) {
            Utils.writelogFile(context, "recent_token != null(Splash, Repository)");
            if (!recent_token.isEmpty()) {
                Utils.writelogFile(context, "!recent_token.isEmpty()(Splash, Repository)");
                Token token = getTokenDB(context);
                if (token != null) {
                    Utils.writelogFile(context, "token != null(Splash, Repository)");
                    String current_token = token.getTOKEN();
                    if (current_token.compareTo(recent_token) != 0) {
                        Utils.writelogFile(context, "update token(Splash, Repository)");
                        token.setTOKEN(recent_token);
                        //UPDATE
                        validateToken(context, token, false);
                    } else {
                        Utils.writelogFile(context, "insert token(Splash, Repository)");
                        if (Utils.getIdCity(context) != 0) {
                            token = setTokenDB(recent_token, context);
                            //INSERT
                            validateToken(context, token, true);
                        } else {
                            Utils.writelogFile(context, "Error id_city == 0(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }
                } else {
                    Utils.writelogFile(context, "insert token(Splash, Repository)");
                    if (Utils.getIdCity(context) != 0) {
                        token = setTokenDB(recent_token, context);
                        //INSERT
                        validateToken(context, token, true);
                    } else {
                        Utils.writelogFile(context, "Error id_city == 0(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                    }
                }
            } else {
                Utils.writelogFile(context, "recent_token.isEmpty()(Splash, Repository)");
                post(SplashActivityEvent.TOKENSUCCESS);
            }
        } else {
            Utils.writelogFile(context, "Error recent_token == null y validateToken(Splash, Repository)");
            post(SplashActivityEvent.TOKENSUCCESS);
        }
    }

    private Token setTokenDB(String recent_token, Context context) {
        Token token = new Token();
        token.setID_TOKEN_KEY(0);
        token.setTOKEN(recent_token);
        token.setID_CITY_FOREIGN(Utils.getIdCity(context));
        return token;
    }

//    private void validateToken(final Context context) {
//        Utils.writelogFile(context, "validateToken y getTokenDB(Splash, Repository)");
//        Token token = getTokenDB(context);
//        if (token != null) {
//            Utils.writelogFile(context, "token != null y token.getTOKEN()(Splash, Repository)");
//            String current_token = token.getTOKEN();
//            if (current_token != null) {
//                Utils.writelogFile(context, "current_token != null y current_token.isEmpty()(Splash, Repository)");
//                if (current_token.isEmpty()) {
//                    Utils.writelogFile(context, "current_token.isEmpty() y post error(Splash, Repository)");
//                    post(SplashActivityEvent.ERROR, context.getString(R.string.error_id_device));
//                } else {
//                    Utils.writelogFile(context, "!current_token.isEmpty() y post success(Splash, Repository)");
//                    post(SplashActivityEvent.TOKENSUCCESS);
//                }
//            } else {
//                Utils.writelogFile(context, "current_token == null y post error(Splash, Repository)");
//                post(SplashActivityEvent.ERROR, context.getString(R.string.error_id_device));
//            }
//        } else {
//            Utils.writelogFile(context, "token == null y post error(Splash, Repository)");
//            post(SplashActivityEvent.ERROR, context.getString(R.string.error_id_device));
//        }
//    }

    private void validateToken(final Context context, final Token token, final boolean isInsert) {
        Utils.writelogFile(context, "Metodo validateToken y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            Utils.writelogFile(context, "processToken FirebaseInstanceId.getInstance().getToken()(Splash, Repository)");
            try {
                Call<ResponseWS> tokenService = service.setToken(token.getID_TOKEN_KEY(), token.getTOKEN(),
                        token.getID_CITY_FOREIGN(), isInsert);
                tokenService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "isSuccessful(Splash, Repository)");
                            responseWS = response.body();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "responseWS != null(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(0)(Splash, Repository)");
                                    if (isInsert) {
                                        int id = responseWS.getId();
                                        if (id != 0) {
                                            Utils.writelogFile(context, "id != 0 y save(Splash, Repository)");
                                            token.setID_TOKEN_KEY(id);
                                            token.save();
                                        } else {
                                            Utils.writelogFile(context, "id == 0 " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                                        }
                                    } else {
                                        Utils.writelogFile(context, "update (FmcInstanceIdService)");
                                        token.update();
                                    }
                                    Utils.writelogFile(context, "Token success(Splash, Repository)");
                                    post(SplashActivityEvent.TOKENSUCCESS);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, "responseWS == null: " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                                post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, "!response.isSuccessful() " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    public Token getTokenDB(Context context) {
        Utils.writelogFile(context, "getToken(Splash, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("Token.ID_CITY_FOREIGN")).is(Utils.getIdCity(context)));
        return SQLite.select().from(Token.class).where().querySingle();
    }

    public Support getSupport() {
        return SQLite.select().from(Support.class).querySingle();
    }

    public void sendEmail(Context context, final Support support, String Shop_name, String comment) {
        Utils.writelogFile(context, "Metodo sendEmail y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            String to = support.getTO();
            final String from = support.getFROM();
            final String password = support.getPASS();
            try {
                Utils.createMailcapCommandMap();
                Session session = Utils.createPropertiesAndSession(from, password);
                MimeMessage message = Utils.createMimeMessage(session, from, to, Shop_name, context);
                BodyPart messageBodyPart1 = Utils.createMimeBodyPart(comment);
                MimeBodyPart messageBodyPart2 = Utils.createMimeBodyPart2(context.getFilesDir() + "/" + context.getResources().getString(R.string.log_file_name));
                Multipart multipart = Utils.createMultipart(messageBodyPart1, messageBodyPart2);
                message.setContent(multipart);
                Transport.send(message);
            } catch (MessagingException ex) {
                Utils.writelogFile(context, "sendEmail catch: " + ex.getMessage() + " (Splash, Repository)");
                post(SplashActivityEvent.ERROR, ex.getMessage());
            }
        } else {
            Utils.writelogFile(context, "sendEmail internet: " + context.getString(R.string.error_internet) + " (Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    public void validateDateShop(final Context context, DateUserCity dateUserCityWS, final Intent intent) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateUser(Splash, Repository)");
                Call<ResultShop> validateDateUser = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getCAT_SUB_CITY_DATE(),
                        dateUserCityWS.getSHOP_DATE(), dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDATE_USER_CITY());
                validateDateUser.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");

                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDateUserCity(Splash, Repository)");

                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(Splash, Repository)");
                                        Delete.table(DateUserCity.class);
                                        Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(Splash, Repository)");
                                        dateUserCity.save();
                                        Utils.writelogFile(context, "save dateUserCity y getCategories(Splash, Repository)");
                                    }
                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Utils.writelogFile(context, "categories != null y categories.size()(Splash, Repository)");
                                        if (categories.size() > 0) {
                                            Utils.writelogFile(context, "categories.size() > 0 y Delete.Category(Splash, Repository)");
                                            Delete.table(Category.class);
                                            Utils.writelogFile(context, "Delete Category ok y for categories(Splash, Repository)");
                                            for (Category category : categories) {
                                                Utils.writelogFile(context, "save category: " + category.getID_CATEGORY_KEY() + "(Splash, Repository)");
                                                category.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "categories.size() = 0 y Delete.Category(Splash, Repository)");
                                            Delete.table(Category.class);
                                        }
                                    }

                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Utils.writelogFile(context, "subCategories != null y subCategories.size()(Splash, Repository)");
                                        if (subCategories.size() > 0) {
                                            Utils.writelogFile(context, "subCategories.size() > 0 y Delete SubCategory(Splash, Repository)");
                                            Delete.table(SubCategory.class);
                                            Utils.writelogFile(context, "Delete SubCategory ok y for subCategories(Splash, Repository)");
                                            for (SubCategory subCategory : subCategories) {
                                                Utils.writelogFile(context, "save subCategory: " + subCategory.getID_SUBCATEGORY_KEY() + " (Splash, Repository)");
                                                subCategory.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "subCategories.size() = 0  y Delete.SubCategory(Splash, Repository)");
                                            Delete.table(SubCategory.class);
                                        }
                                    }

                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Utils.writelogFile(context, "catSubCities != null y catSubCities.size()(Splash, Repository)");
                                        if (catSubCities.size() > 0) {
                                            Utils.writelogFile(context, "catSubCities.size() > 0 y Delete.CatSubCity(Splash, Repository)");
                                            Delete.table(CatSubCity.class);
                                            Utils.writelogFile(context, "Delete.CatSubCity ok  y for catSubCities(Splash, Repository)");
                                            for (CatSubCity catSubCity : catSubCities) {
                                                Utils.writelogFile(context, "save catSubCity: " + catSubCity.getID_CAT_SUB_KEY() + " (Splash, Repository)");
                                                catSubCity.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "catSubCities.size() = 0 y delete CatSubCity(Splash, Repository)");
                                            Delete.table(CatSubCity.class);
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y shops.size()(Splash, Repository)");
                                        if (shops.size() > 0) {
                                            Utils.writelogFile(context, "shops.size() > 0 for shops (Splash, Repository)");
                                            for (Shop shop : shops) {
                                                Utils.writelogFile(context, "fin FOR city y post GOTOPLACE (Splash, Repository)");
                                                shop.setIS_SHOP_UPDATE(1);
                                                shop.save();
                                                setUpdateCatSub(context, shop.getID_CAT_SUB_FOREIGN());
                                            }
                                        } else {
                                            Utils.writelogFile(context, "shops.size() = 0 y deleteShop(Splash, Repository)");
                                            Delete.table(Shop.class);
                                        }
                                    }

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y offers.size()(Splash, Repository)");
                                        if (offers.size() > 0) {
                                            Utils.writelogFile(context, "offers.size() > 0 y for offers(Splash, Repository)");
                                            for (Offer offer : offers) {
                                                Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + "(Splash, Repository)");
                                                offer.save();
                                                Utils.writelogFile(context, " getShop (Splash, Repository)");
                                                Shop shop = getShop(offer.getID_SHOP_FOREIGN());
                                                if (shop != null) {
                                                    Utils.writelogFile(context, " shop != null y save (Splash, Repository)");
                                                    shop.setIS_OFFER_UPDATE(1);
                                                    shop.update();
                                                    setUpdateCatSub(context, shop.getID_CAT_SUB_FOREIGN());
                                                }
                                            }
                                        } else {
                                            Utils.writelogFile(context, "offers.size()= 0 y DeleteOffer(Splash, Repository)");
                                            Delete.table(Offer.class);
                                        }
                                    }
                                    support = response.body().getSupport();
                                    if (support != null) {
                                        Utils.writelogFile(context, "support != null y delete Support(Splash, Repository)");
                                        Delete.table(Support.class);
                                        Utils.writelogFile(context, "delete Support ok y save support(Splash, Repository)");
                                        support.save();
                                        Utils.writelogFile(context, "save Support ok y GOTONAV(Splash, Repository)");
                                    }
                                    Utils.writelogFile(context, "post GOTONAV(Splash, Repository)");
                                    if (intent != null) {
                                        Utils.writelogFile(context, "intent notification != null(Splash, Repository)");
                                        context.startActivity(intent);
                                    } else {
                                        Utils.writelogFile(context, "intent notification == null(Splash, Repository)");
                                        post(SplashActivityEvent.GOTONAV);
                                    }
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4) y post GOTONAV(Splash, Repository)");
                                    if (intent != null) {
                                        Utils.writelogFile(context, "intent notification != null(Splash, Repository)");
                                        context.startActivity(intent);
                                    } else {
                                        Utils.writelogFile(context, "intent notification == null(Splash, Repository)");
                                        post(SplashActivityEvent.GOTONAV);
                                    }
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    public void getShop(final Context context, int id_city) {
        Utils.writelogFile(context, "Metodo getShop y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call getShop(Splash, Repository)");
                Call<ResultShop> getShop = service.getShop(id_city);
                getShop.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Splash, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDateUserCity(Splash, Repository)");
                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(Splash, Repository)");
                                        Delete.table(DateUserCity.class);
                                        Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(Splash, Repository)");
                                        dateUserCity.save();
                                        Utils.writelogFile(context, "save dateUserCity y getCategories(Splash, Repository)");
                                    }
                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Utils.writelogFile(context, "categories != null y delete Category(Splash, Repository)");
                                        Delete.table(Category.class);
                                        Utils.writelogFile(context, "delete Category ok y for categories(Splash, Repository)");
                                        for (Category category : categories) {
                                            Utils.writelogFile(context, "save category: " + category.getID_CATEGORY_KEY() + " (Splash, Repository)");
                                            category.save();
                                        }
                                    }
                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Utils.writelogFile(context, "subCategories != null y delete SubCategory(Splash, Repository)");
                                        Delete.table(SubCategory.class);
                                        Utils.writelogFile(context, "delete SubCategory ok y for subCategories(Splash, Repository)");
                                        for (SubCategory subCategory : subCategories) {
                                            Utils.writelogFile(context, "save subCategory: " + subCategory.getID_SUBCATEGORY_KEY() + " (Splash, Repository)");
                                            subCategory.save();
                                        }
                                    }
                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Utils.writelogFile(context, "catSubCities != null y delete CatSubCity(Splash, Repository)");
                                        Delete.table(CatSubCity.class);
                                        Utils.writelogFile(context, "delete CatSubCity ok y for catSubCities(Splash, Repository)");
                                        for (CatSubCity catSubCity : catSubCities) {
                                            Utils.writelogFile(context, "catSubCity city: " + catSubCity.getID_CAT_SUB_KEY() + " (Splash, Repository)");
                                            catSubCity.save();
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y delete Shop(Splash, Repository)");
                                        Delete.table(Shop.class);
                                        Utils.writelogFile(context, "delete Shop ok y for shops(Splash, Repository)");
                                        for (Shop shop : shops) {
                                            Utils.writelogFile(context, "save shop: " + shop.getID_SHOP_KEY() + " (Splash, Repository)");
                                            shop.setIS_SHOP_UPDATE(1);
                                            shop.save();
                                            setUpdateCatSub(context, shop.getID_CAT_SUB_FOREIGN());
                                        }
                                    }
                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y delete Offer(Splash, Repository)");
                                        Delete.table(Offer.class);
                                        Utils.writelogFile(context, "delete Offer ok y for offers(Splash, Repository)");
                                        for (Offer offer : offers) {
                                            Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + " (Splash, Repository)");
                                            offer.save();
                                            Utils.writelogFile(context, " getShop (Splash, Repository)");
                                            Shop shop = getShop(offer.getID_SHOP_FOREIGN());
                                            if (shop != null) {
                                                Utils.writelogFile(context, " shop != null y save (Splash, Repository)");
                                                shop.setIS_OFFER_UPDATE(1);
                                                shop.update();
                                                setUpdateCatSub(context, shop.getID_CAT_SUB_FOREIGN());
                                            }
                                        }
                                    }
                                    support = response.body().getSupport();
                                    if (support != null) {
                                        Utils.writelogFile(context, "support != null y delete Support(Splash, Repository)");
                                        Delete.table(Support.class);
                                        Utils.writelogFile(context, "delete Support ok y save support(Splash, Repository)");
                                        support.save();
                                        Utils.writelogFile(context, "save support y GOTONAV(Splash, Repository)");
                                    }
                                    Utils.writelogFile(context, "post GOTONAV(Splash, Repository)");
                                    post(SplashActivityEvent.GOTONAV);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private CatSubCity getCatSubEntity(int id) {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("CatSubCity.ID_CAT_SUB_KEY")).is(id));
        try {
            return SQLite.select().from(CatSubCity.class).where(conditions).querySingle();
        } catch (Exception e) {
            return null;
        }
    }

    private Category getCategoryEntity(int id) {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("Category.ID_CATEGORY_KEY")).is(id));
        try {
            return SQLite.select().from(Category.class).where(conditions).querySingle();
        } catch (Exception e) {
            return null;
        }
    }

    private SubCategory getSubCategoryEntity(int id) {
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("SubCategory.ID_SUBCATEGORY_KEY")).is(id));
        try {
            return SQLite.select().from(SubCategory.class).where(conditions).querySingle();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean setUpdateCatSub(Context context, int id) {
        Utils.writelogFile(context, "metodo setUpdateCatSub y getCatSubEntity(Splash, Repository)");
        CatSubCity catSubCity = getCatSubEntity(id);
        if (catSubCity != null) {
            Utils.writelogFile(context, "catSubCity != null y getCategoryEntity y getSubCategoryEntity(Splash, Repository)");
            Category category = getCategoryEntity(catSubCity.getID_CATEGORY_FOREIGN());
            SubCategory subCategory = getSubCategoryEntity(catSubCity.getID_SUBCATEGORY_FOREIGN());
            if (category != null && subCategory != null) {
                Utils.writelogFile(context, "category != null && subCategory != null y update(Splash, Repository)");
                category.setIS_UPDATE(1);
                category.update();

                subCategory.setIS_UPDATE(1);
                subCategory.update();
            } else {
                Utils.writelogFile(context, "category == null && subCategory == null(Splash, Repository)");
                return false;
            }
            return true;
        } else {
            Utils.writelogFile(context, "catSubCity == null(Splash, Repository)");
            return false;
        }
    }

    public Shop getShop(int id_shop) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Shop.ID_SHOP_KEY")).is(id_shop));
        return SQLite.select().from(Shop.class).where(conditionGroup).querySingle();
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
