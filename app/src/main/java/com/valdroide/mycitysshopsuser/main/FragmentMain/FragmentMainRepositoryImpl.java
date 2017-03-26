package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity_Table;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Shop_Table;
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
    //    private List<ResponseWS> responseWses;
//    private List<Category> categories;
//    private List<SubCategory> subCategories;
    private ShopFollow shopFollow;
    private List<Offer> offers;

    public FragmentMainRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getListShops(SubCategory subCategory) {

        int id = selectIdCatSubCity(subCategory);
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Shop.ID_CAT_SUB_FOREIGN")).is(id));
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_CAT_SUB_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()))
                    .where(conditionGroup).orderBy(new NameAlias("SHOP"), true).queryList();
            if (shopsList != null)
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            else
                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getListOffer(int id_shop) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Offer.ID_SHOP_FOREIGN")).is(id_shop));
        try {
            offers = SQLite.select()
                    .from(Offer.class)
                    .where(conditionGroup).queryList();
            if (offers != null)
                post(FragmentMainEvent.GETLISTOFFER, offers, true);
            else
                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    public int selectIdCatSubCity(SubCategory subCategory) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("CatSubCity.ID_CATEGORY_FOREIGN")).is(subCategory.getID_CATEGORY()));
        conditionGroup.and(Condition.column(new NameAlias("CatSubCity.ID_SUBCATEGORY_FOREIGN")).is(subCategory.getID_SUBCATEGORY_KEY()));

        return SQLite.select(CatSubCity_Table.ID_CAT_SUB_KEY).from(CatSubCity.class).where(conditionGroup).querySingle().getID_CAT_SUB_KEY();
    }

    @Override
    public void getDateTable() {
       /*
        try {
            List<DateTable> dateTables = SQLite.select().from(DateTable.class).queryList();
            if (dateTables != null)
                post(FragmentMainEvent.GETDATETABLE, dateTables, true);
            else
                post(FragmentMainEvent.ERROR, "Error en la base de datos.");
        } catch (Exception e) {
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
        */
    }

    @Override
    public void refreshLayout(Context context, String date, String category, String subcategory, String clothesStg, String contact) {
      /*
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<Result> splashService = service.sendDateTable(date, category, subcategory, clothesStg, contact);
                splashService.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {

                            responseWses = response.body().getResponseData();
                            if (responseWses.get(0).getSuccess().equals("0")) {

                                categories = response.body().getCategory();
                                if (categories != null) {
                                    Delete.table(Category.class);

                                    for (Category category : categories) {
                                        category.save();
                                    }
                                }

                                subCategories = response.body().getSubcategory();
                                if (subCategories != null) {
                                    Delete.table(SubCategory.class);

                                    for (SubCategory subCategory : subCategories) {
                                        subCategory.save();
                                    }
                                }
                                clothes = response.body().getClothes();
                                if (clothes != null) {
                                    Delete.table(Clothes.class);

                                    for (Clothes clothe : clothes) {
                                        clothe.save();
                                    }
                                }

                                dateTables = response.body().getDate_table();
                                if (dateTables != null) {
                                    Delete.table(DateTable.class);

                                    for (DateTable dateTable : dateTables) {
                                        dateTable.save();
                                    }
                                }
                                contacts = response.body().getContacts();
                                if (contacts != null) {
                                    Delete.table(Contact.class);

                                    for (Contact contact : contacts) {
                                        contact.save();
                                    }
                                }
                                post(FragmentMainEvent.CALLCLHOTHES);
                            } else if (responseWses.get(0).getSuccess().equals("4")) {
                                post(FragmentMainEvent.WITHOUTCHANGE);
                            } else {
                                post(FragmentMainEvent.ERROR, responseWses.get(0).getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        post(FragmentMainEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            post(FragmentMainEvent.ERROR, "Verificar su conexión de Internet.");
        }
        */
    }

    @Override
    public void onClickFollow(Context context, final Shop shop) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResponseWS> followService = service.follow(shop.getID_SHOP_KEY(), Utils.getIdCity(context),
                        Utils.getFechaOficial(), 0);
                followService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() != null) {
                                if (response.body().getSuccess().equals("0")) {
                                    int follow = response.body().getId();
                                    if (follow != 0) {
                                        shop.setFOLLOW(follow);
                                        shop.setIS_FOLLOW(1);
                                        shopFollow = new ShopFollow();
                                        shopFollow.setID_SHOP_FOREIGN(shop.getID_SHOP_KEY());
                                        shopFollow.setIS_SHOP_FOLLOW(1);
                                        shopFollow.save();

                                        post(FragmentMainEvent.FOLLOW, shop);
                                    } else
                                        post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                                } else {
                                    post(FragmentMainEvent.ERROR, response.body().getMessage());
                                }
                            } else {
                                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        post(FragmentMainEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            post(FragmentMainEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    @Override
    public void onClickUnFollow(Context context, final Shop shop) {
        if (Utils.isNetworkAvailable(context)) {
            try {
                Call<ResponseWS> followService = service.follow(shop.getID_SHOP_KEY(), Utils.getIdCity(context),
                        Utils.getFechaOficial(), 1);
                followService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() != null) {
                                if (response.body().getSuccess().equals("0")) {
                                    int follow = response.body().getId();
                                    if (follow != 0) {
                                        shop.setFOLLOW(follow);
                                        shop.setIS_FOLLOW(0);

                                        shopFollow = getShopFollow(shop.getID_SHOP_KEY());
                                        shopFollow.delete();

                                        post(FragmentMainEvent.FOLLOW, shop);
                                    } else
                                        post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                                } else {
                                    post(FragmentMainEvent.ERROR, response.body().getMessage());
                                }
                            } else {
                                post(FragmentMainEvent.ERROR, Utils.ERROR_DATA_BASE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        post(FragmentMainEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            post(FragmentMainEvent.ERROR, Utils.ERROR_INTERNET);
        }
    }

    public ShopFollow getShopFollow(int id_shop) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("ShopFollow.ID_SHOP_FOREIGN")).is(id_shop));

        return SQLite.select().from(ShopFollow.class).where(conditionGroup).querySingle();
    }

    public void post(int type) {
        post(type, null, null, null, null);
    }

    public void post(int type, List<Shop> shopsList) {
        post(type, shopsList, null, null, null);
    }

    public void post(int type, List<Offer> offers, boolean isOffer) {
        post(type, null, offers, null, null);
    }

    public void post(int type, Shop shop) {
        post(type, null, null, shop, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, error);
    }

    public void post(int type, List<Shop> shopsList, List<Offer> offers, Shop shop, String error) {
        FragmentMainEvent event = new FragmentMainEvent();
        event.setType(type);
        event.setShopsList(shopsList);
        event.setShop(shop);
        event.setOffers(offers);
        //   event.setDateTables(dateTables);
        event.setError(error);
        eventBus.post(event);
    }
}
