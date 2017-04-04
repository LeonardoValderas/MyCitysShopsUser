package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity_Table;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.entities.shop.Token_Table;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMainRepositoryImpl implements FragmentMainRepository {
    private EventBus eventBus;
    private List<Shop> shopsList;
    private APIService service;
    private ShopFollow shopFollow;
    private ResponseWS responseWS;
    private List<Offer> offers;
    private List<Shop> shops;
    private List<Category> categories;
    private List<SubCategory> subCategories;
    private List<CatSubCity> catSubCities;
    private DateUserCity dateUserCity;

    public FragmentMainRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getListShops(Context context, SubCategory subCategory) {
        Utils.writelogFile(context, "getListShops(Fragmentmain, Repository)");
        int id = selectIdCatSubCity(context, subCategory);
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Shop.ID_CAT_SUB_FOREIGN")).is(id));
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_CAT_SUB_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()))
                    .where(conditionGroup).orderBy(new NameAlias("FOLLOW"), false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS(Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getListOffer(Context context, int id_shop) {
        Utils.writelogFile(context, "getListOffer(Fragmentmain, Repository)");
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Offer.ID_SHOP_FOREIGN")).is(id_shop));
        conditionGroup.and(Condition.column(new NameAlias("Offer.IS_ACTIVE")).is(1));
        try {
            offers = SQLite.select()
                    .from(Offer.class)
                    .where(conditionGroup).queryList();
            if (offers != null) {
                Utils.writelogFile(context, "offers != null y post GETLISTOFFER(Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTOFFER, offers, true);
            } else {
                Utils.writelogFile(context, "offers == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    public int selectIdCatSubCity(Context context, SubCategory subCategory) {
        Utils.writelogFile(context, "selectIdCatSubCity(Fragmentmain, Repository)");
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("CatSubCity.ID_CATEGORY_FOREIGN")).is(subCategory.getID_CATEGORY()));
        conditionGroup.and(Condition.column(new NameAlias("CatSubCity.ID_SUBCATEGORY_FOREIGN")).is(subCategory.getID_SUBCATEGORY_KEY()));
        try {
            return SQLite.select(CatSubCity_Table.ID_CAT_SUB_KEY).from(CatSubCity.class).where(conditionGroup).querySingle().getID_CAT_SUB_KEY();
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            return 0;
        }
    }

    @Override
    public void getDateUserCity(Context context) {
        Utils.writelogFile(context, "getDateUserCity(Fragmentmain, Repository)");
        try {
            DateUserCity dateUserCity = SQLite.select().from(DateUserCity.class).querySingle();
            if (dateUserCity != null) {
                Utils.writelogFile(context, "dateUserCity != null y post GETDATEUSERCITY (Fragmentmain, Repository)");
                post(FragmentMainEvent.GETDATEUSERCITY, dateUserCity);
            } else {
                Utils.writelogFile(context, "dateUserCity == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, "Error en la base de datos.");
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getMyFavoriteShops(Context context) {
        Utils.writelogFile(context, "getMyFavoriteShops(Fragmentmain, Repository)");
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_CAT_SUB_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .innerJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()), Shop_Table.ID_CITY_FOREIGN.eq(ShopFollow_Table.ID_CITY.withTable()))
                    .orderBy(new NameAlias("FOLLOW"), false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS (Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR (Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void setUpdateOffer(Context context, int position, Shop shop) {
        Utils.writelogFile(context, "setUpdateOffer(Fragmentmain, Repository)");
        try {
            shop.setIS_OFFER_UPDATE(0);
            shop.update();
            post(FragmentMainEvent.ISUPDATE);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void refreshLayout(final Context context, DateUserCity dateUserCityWS) {
        Utils.writelogFile(context, "Metodo validateDatePlace y Se valida conexion a internet(Fragmentmain, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateUser(Fragmentmain, Repository)");
                Call<ResultShop> splashService = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getCAT_SUB_CITY_DATE(),
                        dateUserCityWS.getSHOP_DATE(), dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDATE_USER_CITY());
                splashService.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Fragmentmain, Repository)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Fragmentmain, Repository)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y getDateUserCity(Fragmentmain, Repository)");
                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(Fragmentmain, Repository)");
                                        Delete.table(DateUserCity.class);
                                        Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(Fragmentmain, Repository)");
                                        dateUserCity.save();
                                        Utils.writelogFile(context, "save dateUserCity y getCategories(Fragmentmain, Repository)");
                                    }

                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Utils.writelogFile(context, "categories != null y categories.size()(Fragmentmain, Repository)");
                                        if (categories.size() > 0) {
                                            Utils.writelogFile(context, "categories.size() > 0 y Delete Category(Fragmentmain, Repository)");
                                            Delete.table(Category.class);
                                            Utils.writelogFile(context, "Delete Category y FOR categories(Fragmentmain, Repository)");
                                            for (Category category : categories) {
                                                Utils.writelogFile(context, "save category: " + category.getID_CATEGORY_KEY() + "(Fragmentmain, Repository)");
                                                category.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "categories == 0 y Delete Category(Fragmentmain, Repository)");
                                            Delete.table(Category.class);
                                        }
                                    }

                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Utils.writelogFile(context, "subCategories != null y subCategories.size()(Fragmentmain, Repository)");
                                        if (subCategories.size() > 0) {
                                            Utils.writelogFile(context, "subCategories.size() > 0 y Delete SubCategory(Fragmentmain, Repository)");
                                            Delete.table(SubCategory.class);
                                            Utils.writelogFile(context, "Delete SubCategory y FOR subCategories(Fragmentmain, Repository)");
                                            for (SubCategory subCategory : subCategories) {
                                                Utils.writelogFile(context, "save category: " + subCategory.getID_SUBCATEGORY_KEY() + "(Fragmentmain, Repository)");
                                                subCategory.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "subCategories == 0 y Delete SubCategory(Fragmentmain, Repository)");
                                            Delete.table(SubCategory.class);
                                        }
                                    }

                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Utils.writelogFile(context, "catSubCities != null y catSubCities.size()(Fragmentmain, Repository)");
                                        if (catSubCities.size() > 0) {
                                            Utils.writelogFile(context, "catSubCities.size() > 0 y Delete CatSubCity(Fragmentmain, Repository)");
                                            Delete.table(CatSubCity.class);
                                            Utils.writelogFile(context, "Delete CatSubCity y FOR catSubCities(Fragmentmain, Repository)");
                                            for (CatSubCity catSubCity : catSubCities) {
                                                Utils.writelogFile(context, "save catSubCity: " + catSubCity.getID_CAT_SUB_KEY() + "(Fragmentmain, Repository)");
                                                catSubCity.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "catSubCities == 0 y Delete CatSubCity(Fragmentmain, Repository)");
                                            Delete.table(CatSubCity.class);
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y shops.size()(Fragmentmain, Repository)");
                                        if (shops.size() > 0) {
                                            Utils.writelogFile(context, "shops.size() > 0 y FOR shops(Fragmentmain, Repository)");
                                            for (Shop shop : shops) {
                                                Utils.writelogFile(context, "save shop: " + shop.getID_SHOP_KEY() + "(Fragmentmain, Repository)");
                                                shop.setIS_OFFER_UPDATE(1);
                                                shop.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "shops == 0 y Delete Shop(Fragmentmain, Repository)");
                                            Delete.table(Shop.class);
                                        }
                                    }

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y offers.size()(Fragmentmain, Repository)");
                                        if (offers.size() > 0) {
                                            Utils.writelogFile(context, "offers.size() > 0 y FOR offers(Fragmentmain, Repository)");
                                            for (Offer offer : offers) {
                                                Utils.writelogFile(context, "save offer: " + offer.getID_OFFER_KEY() + "(Fragmentmain, Repository)");
                                                offer.save();
                                                Shop shop = new Shop();
                                                shop.setID_SHOP_KEY(offer.getID_SHOP_FOREIGN());
                                                shop.setIS_OFFER_UPDATE(1);
                                                shop.update();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "offers == 0 y Delete Offer(Fragmentmain, Repository)");
                                            Delete.table(Offer.class);
                                        }
                                    }
                                    Utils.writelogFile(context, "post CALLSHOPS(Fragmentmain, Repository)");
                                    post(FragmentMainEvent.CALLSHOPS);
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4) y post WITHOUTCHANGE(Fragmentmain, Repository)");
                                    post(FragmentMainEvent.WITHOUTCHANGE);
                                }
                            } else {
                                Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Fragmentmain, Repository)");
                                post(FragmentMainEvent.ERROR, responseWS.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Fragmentmain, Repository)");
                        post(FragmentMainEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + Utils.ERROR_INTERNET + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void onClickFollowOrUnFollow(final Context context, final Shop shop, final boolean isFollow) {
        Utils.writelogFile(context, "Metodo onClickFollowOrUnFollow y Se valida conexion a internet(Fragmentmain, Repository)");
        final String date = Utils.getFechaOficialSeparate();
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call followOrUnFollow(Fragmentmain, Repository)");
                Call<ResponseWS> followService = service.followOrUnFollow(shop.getID_SHOP_KEY(), Utils.getIdCity(context),
                        date, isFollow, getIdToken(context));
                followService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(Fragmentmain, Repository)");
                            if (response.body().getSuccess() != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(Fragmentmain, Repository)");
                                if (response.body().getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "getSuccess = 0 y getId(Fragmentmain, Repository)");
                                    int follow = response.body().getId();
                                    if (follow != 0) {
                                        Utils.writelogFile(context, "getId != 0 y fill shop object save y post FOLLOW(Fragmentmain, Repository)");
                                        //shopInit = shop;
                                        shop.setFOLLOW(follow);
                                        if (isFollow) {
                                            Utils.writelogFile(context, "is follow(Fragmentmain, Repository)");
                                            shop.setIS_FOLLOW(1);
                                        } else {
                                            Utils.writelogFile(context, "is unfollow(Fragmentmain, Repository)");
                                            shop.setIS_FOLLOW(0);
                                        }
                                        shop.setDATE_UNIQUE(date);
                                        shop.update();
                                        setDateUserCity(context, date);
                                        shopFollow = new ShopFollow();
                                        if (isFollow) {
                                            Utils.writelogFile(context, "is follow-shopFollow(Fragmentmain, Repository)");
                                            shopFollow.setID_SHOP_FOREIGN(shop.getID_SHOP_KEY());
                                            shopFollow.setIS_SHOP_FOLLOW(1);
                                            shopFollow.setID_CITY(Utils.getIdCity(context));
                                            shopFollow.save();
                                        } else {
                                            Utils.writelogFile(context, "is unfollow-shopFollow(Fragmentmain, Repository)");
                                            shopFollow = getShopFollow(shop.getID_SHOP_KEY());
                                            shopFollow.delete();
                                        }
                                        post(FragmentMainEvent.FOLLOWORUNFOLLOW, shop);
                                    } else {
                                        Utils.writelogFile(context, " Base de datos error " + Utils.ERROR_DATA_BASE + "(Fragmentmain, Repository)");
                                        post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                                    }
                                } else {
                                    Utils.writelogFile(context, " getSuccess = error " + responseWS.getMessage() + "(Fragmentmain, Repository)");
                                    post(FragmentMainEvent.ERROR, response.body().getMessage());
                                }
                            } else {
                                Utils.writelogFile(context, " Base de datos error " + Utils.ERROR_DATA_BASE + "(Fragmentmain, Repository)");
                                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(Fragmentmain, Repository)");
                        post(FragmentMainEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, " Internet error " + Utils.ERROR_INTERNET + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public boolean setDateUserCity(Context context, String date) {
        Utils.writelogFile(context, "setDateUserCity(Fragmentmain, Repository)");
        try {
            DateUserCity dateUserCity = SQLite.select().from(DateUserCity.class).querySingle();
            dateUserCity.setSHOP_DATE(date);
            dateUserCity.setDATE_USER_CITY(date);
            dateUserCity.update();
            return true;
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            return false;
        }
    }

    public ShopFollow getShopFollow(int id_shop) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ShopFollow.ID_SHOP_FOREIGN")).is(id_shop));

        return SQLite.select().from(ShopFollow.class).where(conditionGroup).querySingle();
    }

    public int getIdToken(Context context) {
        Utils.writelogFile(context, "getIdToken(Fragmentmain, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("Token.ID_CITY_FOREIGN")).is(Utils.getIdCity(context)));

        return SQLite.select(Token_Table.ID_TOKEN_KEY).from(Token.class).where(conditions).querySingle().getID_TOKEN_KEY();
    }

    public void post(int type) {
        post(type, null, null, null, null, null);
    }

    public void post(int type, List<Shop> shopsList) {
        post(type, shopsList, null, null, null, null);
    }

    public void post(int type, List<Offer> offers, boolean isOffer) {
        post(type, null, offers, null, null, null);
    }

    public void post(int type, Shop shop) {
        post(type, null, null, shop, null, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, null, error);
    }

    public void post(int type, DateUserCity dateUserCity) {
        post(type, null, null, null, dateUserCity, null);
    }

    public void post(int type, List<Shop> shopsList, List<Offer> offers, Shop shop, DateUserCity dateUserCity, String error) {
        FragmentMainEvent event = new FragmentMainEvent();
        event.setType(type);
        event.setShopsList(shopsList);
        event.setShop(shop);
        event.setOffers(offers);
        event.setDateUserCity(dateUserCity);
        event.setError(error);
        eventBus.post(event);
    }
}
