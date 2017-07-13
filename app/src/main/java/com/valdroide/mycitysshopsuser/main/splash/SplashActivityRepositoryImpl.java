package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.db.ShopsDatabase;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.Category_Table;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory_Table;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.DatePlace;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultPlace;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.entities.shop.DrawWinner;
import com.valdroide.mycitysshopsuser.entities.shop.DrawWinner_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Offer_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Support;
import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.entities.shop.Token_Table;
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

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

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
    private DateUserCity dateUserCity;
    private Support support;
    private List<Draw> draws;
    private List<Integer> idsShops;
    private List<Integer> idsOffers;
    private DatabaseDefinition database = FlowManager.getDatabase(ShopsDatabase.class);
    private FastStoreModelTransaction transaction;

    public SplashActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void validateDatePlace(Context context, Intent intent) {
        Utils.writelogFile(context, "validateDatePlace(Splash, Repository)");
        try {
            place = select().from(MyPlace.class).querySingle();
            if (place == null) { // es la primera vez que entra o quiere cambiar de lugar
                Utils.writelogFile(context, "place==null(Splash, Repository)");
                datePlace = select().from(DatePlace.class).querySingle();
                if (datePlace != null) {
                    Utils.writelogFile(context, "datePlace != null y validateDatePlace(Splash, Repository)");
                    validateDatePlace(context, datePlace.getTABLE_DATE(), datePlace.getCOUNTRY_DATE(),
                            datePlace.getSTATE_DATE(), datePlace.getCITY_DATE());
                } else { // traemos los datos sin validar fechas
                    Utils.writelogFile(context, "datePlace == null y getPlace(Splash, Repository)");
                    getPlace(context);
                    Utils.setIsFirst(context, true);
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

    private void validateDatePlace(final Context context, String date, String cou, String sta, String ci) {
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
                                        Utils.writelogFile(context, "delete Country ok y transaction(Splash, Repository)");
                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(Country.class))
                                                .addAll(countries)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y transaction(Splash, Repository)");
                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(State.class))
                                                .addAll(states)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y transaction(Splash, Repository)");
                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(City.class))
                                                .addAll(cities)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    Utils.writelogFile(context, "post GOTOPLACE (Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4)(Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void getPlace(final Context context) {
        Utils.writelogFile(context, "Metodo getPlace y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call getPlace(Splash, Repository)");
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
                                        Utils.writelogFile(context, "delete Country ok y transaction(Splash, Repository)");
                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(Country.class))
                                                .addAll(countries)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    states = response.body().getStates();
                                    if (states != null) {
                                        Utils.writelogFile(context, "states != null y delete State(Splash, Repository)");
                                        Delete.table(State.class);
                                        Utils.writelogFile(context, "delete State ok y transaction(Splash, Repository)");
                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(State.class))
                                                .addAll(states)
                                                .build();
                                        database.executeTransaction(transaction);

                                    }
                                    cities = response.body().getCities();
                                    if (cities != null) {
                                        Utils.writelogFile(context, "cities != null y delete City(Splash, Repository)");
                                        Delete.table(City.class);
                                        Utils.writelogFile(context, "delete City ok y transaction(Splash, Repository)");

                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(City.class))
                                                .addAll(cities)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    Utils.writelogFile(context, "post GOTOPLACE (Splash, Repository)");
                                    post(SplashActivityEvent.GOTOPLACE);
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultPlace> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void validateDateShop(Context context, Intent intent) {
        Utils.writelogFile(context, "Metodo validateDateShop(Splash, Repository)");
        try {
            dateUserCity = select().from(DateUserCity.class).querySingle();
            if (dateUserCity != null) {
                Utils.writelogFile(context, "dateUserCity != null y validateDateShop(Splash, Repository)");
                validateDateShop(context, dateUserCity, intent);
            } else {
                Utils.writelogFile(context, "dateUserCity == null y getShop(Splash, Repository)");
                getShop(context, Utils.getIdCity(context));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
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
                            if (responseWS != null && responseWS.getSuccess() != null) {
                                Utils.writelogFile(context, "responseWS != null && responseWS.getSuccess() != null(Splash, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(0)(Splash, Repository)");
                                    if (isInsert) {
                                        int id = responseWS.getId();
                                        if (id != 0) {
                                            Utils.writelogFile(context, "id != 0 y save(Splash, Repository)");
                                            token.setID_TOKEN_KEY(id);
                                            token.save();
                                        } else {
                                            Utils.writelogFile(context, "id == 0(Splash, Repository)");
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
                                Utils.writelogFile(context, "responseWS == null(Splash, Repository)");
                                post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, "!response.isSuccessful()(Splash, Repository)");
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
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private Token getTokenDB(Context context) {
        Utils.writelogFile(context, "getTokenDB(Splash, Repository)");
        return SQLite.select().from(Token.class).where(Token_Table.ID_CITY_FOREIGN.eq(Utils.getIdCity(context))).querySingle();
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
            Utils.writelogFile(context, "sendEmail internet(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private void validateDateShop(final Context context, DateUserCity dateUserCityWS, final Intent intent) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(Splash, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateUser(Splash, Repository)");
                Call<ResultShop> validateDateUser = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getSHOP_DATE(),
                        dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDRAW_DATE(), dateUserCityWS.getSUPPORT_DATE(), dateUserCityWS.getDATE_USER_CITY());
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
                                            Utils.writelogFile(context, "Delete Category ok y transaction(Splash, Repository)");
                                            transaction = FastStoreModelTransaction
                                                    .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                    .addAll(categories)
                                                    .build();
                                            database.executeTransaction(transaction);
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
                                            Utils.writelogFile(context, "Delete SubCategory ok y transaction(Splash, Repository)");
                                            transaction = FastStoreModelTransaction
                                                    .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                    .addAll(subCategories)
                                                    .build();
                                            database.executeTransaction(transaction);
                                        } else {
                                            Utils.writelogFile(context, "subCategories.size() = 0  y Delete.SubCategory(Splash, Repository)");
                                            Delete.table(SubCategory.class);
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y shops.size()(Splash, Repository)");
                                        if (shops.size() > 0) {
                                            Utils.writelogFile(context, "shops.size() > 0 for(Splash, Repository)");
                                            for (Shop shop : shops) {
                                                int count_offer_now = shop.getQUANTITY_OFFER();
                                                List<Offer> offerList = getListOfferForShopId(shop.getID_SHOP_KEY());
                                                if (offerList != null)
                                                    if (offerList.size() > count_offer_now) {
                                                        int diff = offerList.size() - count_offer_now;

                                                        for (int i = 0; diff > i; i++) {
                                                            deleteOffer(offerList.get(i).getID_OFFER_KEY());
                                                        }
                                                    }
                                                shop.save();
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
                                            Utils.writelogFile(context, "offers.size() > 0 y for(Splash, Repository)");
                                            for (Offer offer : offers) {
                                                offer.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "offers.size()= 0 y DeleteOffer(Splash, Repository)");
                                            Delete.table(Offer.class);
                                        }
                                    }

                                    idsShops = response.body().getIdsShops();
                                    if (idsShops != null) {
                                        Utils.writelogFile(context, "idsShops != null y idsShops.size()(Splash, Repository)");
                                        if (idsShops.size() > 0) {
                                            Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(Splash, Repository)");
                                            for (Integer i : idsShops) {
                                                setUpdateCatSub(context, i);
                                            }
                                        }
                                    }

                                    idsOffers = response.body().getIdsOffers();
                                    if (idsOffers != null) {
                                        Utils.writelogFile(context, "idsOffers != null y idsOffers.size()(Splash, Repository)");
                                        if (idsOffers.size() > 0) {
                                            Utils.writelogFile(context, "idsOffers.size() > 0 y for idsOffers(Splash, Repository)");
                                            for (Integer i : idsOffers) {
                                                int id_sub = updateShopAndIdSub(context, i);
                                                setUpdateCatSub(context, id_sub);
                                            }
                                        }
                                    }

                                    draws = response.body().getDraws();
                                    if (draws != null) {
                                        Utils.writelogFile(context, "draws != null y draws.size()(Splash, Repository)");
                                        if (draws.size() > 0) {
                                            Utils.writelogFile(context, "draws.size() > 0 y for draws(Splash, Repository)");
                                            for (Draw draw : draws) {
                                                if (draw.getIS_ACTIVE() == 0) {
                                                    Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (Splash, Repository)");
                                                    if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                        Utils.writelogFile(context, "isDrawWinner = true (Splash, Repository)");
                                                        if (draw.getIS_TAKE() == 0) {
                                                            Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (Splash, Repository)");
                                                            if (draw.getIS_LIMITE() == 0) {
                                                                Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(Splash, Repository)");
                                                                draw.setIS_WINNER(1);
                                                                draw.update();
                                                            } else {
                                                                Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(Splash, Repository)");
                                                                deleteDrawAndWinner(draw);
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (Splash, Repository)");
                                                            deleteDrawWinner(draw.getID_DRAW_KEY());
                                                            draw.delete();
                                                        }
                                                    } else {
                                                        Utils.writelogFile(context, "no winner delete (Splash, Repository)");
                                                        draw.delete();
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(Splash, Repository)");
                                                    draw.save();
                                                }
                                            }
                                        } else {
                                            Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(Splash, Repository)");
                                            Delete.table(Draw.class);
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
                                    Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

//    private int getQuantityForShopId(int id) {
//        try {
//            return SQLite.select(Shop_Table.QUANTITY_OFFER).from(Shop.class).where(Shop_Table.ID_SHOP_KEY.eq(id)).querySingle().getQUANTITY_OFFER();
//        } catch (Exception e) {
//            return 0;
//        }
//    }

    private List<Offer> getListOfferForShopId(int id) {
        try {
            return SQLite.select()
                    .from(Offer.class)
                    .where(OperatorGroup.clause().and(Offer_Table.ID_SHOP_FOREIGN.is(id)))
                    .orderBy(Offer_Table.ID_OFFER_KEY, true).queryList();
        } catch (Exception e) {
            return null;
        }
    }

    private void deleteOffer(int id_offer) {
        SQLite.delete(Offer.class)
                .where(Offer_Table.ID_OFFER_KEY.is(id_offer))
                .async()
                .execute();
    }

    private boolean isDrawWinner(int id_draw) {
        return SQLite.select().from(DrawWinner.class).where(DrawWinner_Table.ID_DRAW_FOREIGN.is(id_draw)).querySingle() != null;
    }

    private void deleteDrawWinner(int id_draw) {
        SQLite.delete(DrawWinner.class)
                .where(DrawWinner_Table.ID_DRAW_FOREIGN.is(id_draw))
                .async()
                .execute();
    }

    private void deleteDrawAndWinner(Draw draw) {
        deleteDrawWinner(draw.getID_DRAW_KEY());
        draw.delete();
    }

    private void getShop(final Context context, int id_city) {
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

                                        transaction = FastStoreModelTransaction
                                                .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                .addAll(categories)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Utils.writelogFile(context, "subCategories != null y delete SubCategory(Splash, Repository)");
                                        Delete.table(SubCategory.class);
                                        Utils.writelogFile(context, "delete SubCategory ok y for subCategories(Splash, Repository)");

                                        transaction = FastStoreModelTransaction
                                                .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                .addAll(subCategories)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y delete Shop(Splash, Repository)");
                                        Delete.table(Shop.class);
                                        Utils.writelogFile(context, "delete Shop ok y for shops(Splash, Repository)");

                                        transaction = FastStoreModelTransaction
                                                .insertBuilder(FlowManager.getModelAdapter(Shop.class))
                                                .addAll(shops)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }
                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y delete Offer(Splash, Repository)");
                                        Delete.table(Offer.class);
                                        Utils.writelogFile(context, "delete Offer ok y for offers(Splash, Repository)");

                                        transaction = FastStoreModelTransaction
                                                .insertBuilder(FlowManager.getModelAdapter(Offer.class))
                                                .addAll(offers)
                                                .build();
                                        database.executeTransaction(transaction);
                                    }

                                    idsShops = response.body().getIdsShops();
                                    if (idsShops != null) {
                                        Utils.writelogFile(context, "idsShops != null y idsShops.size()(Splash, Repository)");
                                        if (idsShops.size() > 0) {
                                            Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(Splash, Repository)");
                                            for (Integer i : idsShops) {
                                                setUpdateCatSub(context, i);
                                            }
                                        }
                                    }

                                    draws = response.body().getDraws();
                                    if (draws != null) {
                                        Utils.writelogFile(context, "draws != null y draws.size()(Splash, Repository)");
                                        if (draws.size() > 0) {
                                            Utils.writelogFile(context, "draws.size() > 0 y for draws(Splash, Repository)");
                                            transaction = FastStoreModelTransaction
                                                    .insertBuilder(FlowManager.getModelAdapter(Draw.class))
                                                    .addAll(draws)
                                                    .build();
                                            database.executeTransaction(transaction);
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
                                    Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Splash, Repository)");
                                    post(SplashActivityEvent.ERROR, responseWS.getMessage());
                                }
                            }
                        } else {
                            Utils.writelogFile(context, "Base de datos error(Splash, Repository)");
                            post(SplashActivityEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, "Call error " + t.getMessage() + "(Splash, Repository)");
                        post(SplashActivityEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Splash, Repository)");
                post(SplashActivityEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Splash, Repository)");
            post(SplashActivityEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    private boolean setUpdateCatSub(Context context, int id_sub) {
        Utils.writelogFile(context, "metodo setUpdateCatSub(Splash, Repository)");

        int id_cat = getIdCategory(id_sub);
        if (id_cat > 0) {
            SQLite.update(Category.class)
                    .set(Category_Table.IS_UPDATE.eq(1))
                    .where(Category_Table.ID_CATEGORY_KEY.is(id_cat))
                    .async()
                    .execute();

            SQLite.update(SubCategory.class)
                    .set(SubCategory_Table.IS_UPDATE.eq(1))
                    .where(SubCategory_Table.ID_SUBCATEGORY_KEY.is(id_sub))
                    .async()
                    .execute();
            return true;
        } else {
            return false;
        }
    }

    private int getIdCategory(int id_sub) {
        return SQLite.select(SubCategory_Table.ID_CATEGORY_FOREIGN).from(SubCategory.class)
                .where(SubCategory_Table.ID_SUBCATEGORY_KEY.is(id_sub)).querySingle().getID_CATEGORY_FOREIGN();
    }

    private int updateShopAndIdSub(Context context, int id_shop) {
        Utils.writelogFile(context, "updateShopAndIdSub(OfferFragment, Repository)");
        SQLite.update(Shop.class)
                .set(Shop_Table.IS_OFFER_UPDATE.eq(1))
                .where(Shop_Table.ID_SHOP_KEY.is(id_shop))
                .async()
                .execute();
        return SQLite.select(Shop_Table.ID_SUBCATEGORY_FOREIGN).from(Shop.class).querySingle().getID_SUBCATEGORY_FOREIGN();
    }

//    private FastStoreModelTransaction setFastStoreModelTransaction(Class entityClass, List<Object> objects) {
//        return FastStoreModelTransaction
//                .insertBuilder(FlowManager.getModelAdapter(entityClass))
//                .addAll(objects)
//                .build();
//    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String error) {
        SplashActivityEvent event = new SplashActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
