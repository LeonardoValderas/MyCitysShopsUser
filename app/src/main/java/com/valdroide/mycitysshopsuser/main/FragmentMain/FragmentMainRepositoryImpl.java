package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Operator;
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
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.entities.shop.DrawWinner;
import com.valdroide.mycitysshopsuser.entities.shop.DrawWinner_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Offer_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow;
import com.valdroide.mycitysshopsuser.entities.shop.ShopFollow_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsuser.entities.shop.Support;
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
    private DateUserCity dateUserCity;
    private Support support;
    private List<Draw> draws;
    private List<Integer> idsShops;
    private List<Integer> idsOffers;
    private DatabaseDefinition database = FlowManager.getDatabase(ShopsDatabase.class);
    private FastStoreModelTransaction transaction;

    public FragmentMainRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getListShops(Context context, SubCategory subCategory) {
        Utils.writelogFile(context, "getListShops(Fragmentmain, Repository)");
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_SUBCATEGORY_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()), Shop_Table.ID_CITY_FOREIGN.eq(ShopFollow_Table.ID_CITY.withTable()))
                    .where(OperatorGroup.clause()
                            .and(Shop_Table.ID_SUBCATEGORY_FOREIGN.is(subCategory.getID_SUBCATEGORY_KEY())).and(Shop_Table.ISACTIVE.is(1)))
                    .orderBy(Shop_Table.FOLLOW, false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS(Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getListOffer(Context context, int id_shop) {
        Utils.writelogFile(context, "getListOffer(Fragmentmain, Repository)");
        try {

            offers = SQLite.select()
                    .from(Offer.class)
                    .where(OperatorGroup.clause().and(Offer_Table.ID_SHOP_FOREIGN.is(id_shop))
                            .and(Offer_Table.IS_ACTIVE.is(1))).orderBy(Offer_Table.ID_OFFER_KEY, false).queryList();

            if (offers != null) {
                Utils.writelogFile(context, "offers != null y post GETLISTOFFER(Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTOFFER, offers, true);
            } else {
                Utils.writelogFile(context, "offers == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    private DateUserCity getDateUserCity(Context context) {
        Utils.writelogFile(context, "getDateUserCity(Fragmentmain, Repository)");
        try {
            return SQLite.select().from(DateUserCity.class).querySingle();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
            return null;
        }
    }

    @Override
    public void getMyFavoriteShops(Context context) {
        Utils.writelogFile(context, "getMyFavoriteShops(Fragmentmain, Repository)");
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_SUBCATEGORY_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .innerJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()), Shop_Table.ID_CITY_FOREIGN.eq(ShopFollow_Table.ID_CITY.withTable()))
                    .orderBy(Shop_Table.FOLLOW, false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS (Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR (Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void setUpdateOffer(Context context, int position, Shop shop) {
        Utils.writelogFile(context, "setUpdateOffer(Fragmentmain, Repository)");
        try {
            if (shop.getIS_OFFER_UPDATE() != 0) {
                shop.setIS_OFFER_UPDATE(0);
                if (shop.getIS_SHOP_UPDATE() != 0)
                    shop.setIS_SHOP_UPDATE(0);
                shop.update();
            }
            post(FragmentMainEvent.ISUPDATE);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getShopsSearch(Context context, SubCategory subCategory, String letter) {
        Utils.writelogFile(context, "getShopsSearch(Fragmentmain, Repository)");
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_SUBCATEGORY_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()))
                    .where(OperatorGroup.clause().and(Shop_Table.ID_SUBCATEGORY_FOREIGN.is(subCategory.getID_SUBCATEGORY_KEY())).and(Shop_Table.ISACTIVE.is(1)).and(Shop_Table.SHOP.like("%" + letter + "%")))
                    .orderBy(Shop_Table.FOLLOW, false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS(Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getShopsSearchFavorites(Context context, String letter) {
        Utils.writelogFile(context, "getShopsSearchFavorites(Fragmentmain, Repository)");
        try {
            shopsList = SQLite.select(Shop_Table.ID_SHOP_KEY, Shop_Table.SHOP, Shop_Table.ID_ACCOUNT_FOREIGN,
                    Shop_Table.USER, Shop_Table.PASS, Shop_Table.ID_CITY_FOREIGN, Shop_Table.ID_SUBCATEGORY_FOREIGN, Shop_Table.ISACTIVE,
                    Shop_Table.URL_LOGO, Shop_Table.NAME_LOGO, Shop_Table.DESCRIPTION, Shop_Table.PHONE, Shop_Table.EMAIL,
                    Shop_Table.LATITUD, Shop_Table.LONGITUD, Shop_Table.ADDRESS, Shop_Table.FOLLOW, Shop_Table.WEB,
                    Shop_Table.WHATSAAP, Shop_Table.INSTAGRAM, Shop_Table.TWITTER, Shop_Table.SNAPCHAT,
                    Shop_Table.WORKING_HOURS, Shop_Table.FACEBOOK, Shop_Table.IS_SHOP_UPDATE, Shop_Table.IS_OFFER_UPDATE, ShopFollow_Table.IS_SHOP_FOLLOW.as("IS_FOLLOW"))
                    .from(Shop.class)
                    .innerJoin(ShopFollow.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(ShopFollow_Table.ID_SHOP_FOREIGN.withTable()), Shop_Table.ID_CITY_FOREIGN.eq(ShopFollow_Table.ID_CITY.withTable()))
                    .where(Shop_Table.SHOP.like("%" + letter + "%"))
                    .orderBy(Shop_Table.FOLLOW, false).queryList();
            if (shopsList != null) {
                Utils.writelogFile(context, "shopsList != null y post GETLISTSHOPS (Fragmentmain, Repository)");
                post(FragmentMainEvent.GETLISTSHOPS, shopsList);
            } else {
                Utils.writelogFile(context, "shopsList == null y post ERROR (Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void refreshLayout(final Context context, final boolean isMyShop, final boolean isFollow) {
        Utils.writelogFile(context, "Metodo refreshLayout y Se valida conexion a internet(Fragmentmain, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                dateUserCity = getDateUserCity(context);
                if (dateUserCity != null) {
                    Utils.writelogFile(context, "dateUserCity != null y Call validateDateUser metodo refreshLayout(Fragmentmain, Repository)");
                    Call<ResultShop> splashService = service.validateDateUser(Utils.getIdCity(context), dateUserCity.getCATEGORY_DATE(),
                            dateUserCity.getSUBCATEGORY_DATE(), dateUserCity.getSHOP_DATE(),
                            dateUserCity.getOFFER_DATE(), dateUserCity.getDRAW_DATE(), dateUserCity.getSUPPORT_DATE(),
                            dateUserCity.getDATE_USER_CITY());
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
                                                Utils.writelogFile(context, "Delete Category ok y transaction(Fragmentmain, Repository)");
                                                transaction = FastStoreModelTransaction
                                                        .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                        .addAll(categories)
                                                        .build();
                                                database.executeTransaction(transaction);
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
                                                Utils.writelogFile(context, "Delete SubCategory ok y transaction(Fragmentmain, Repository)");
                                                transaction = FastStoreModelTransaction
                                                        .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                        .addAll(subCategories)
                                                        .build();
                                                database.executeTransaction(transaction);
                                            } else {
                                                Utils.writelogFile(context, "subCategories == 0 y Delete SubCategory(Fragmentmain, Repository)");
                                                Delete.table(SubCategory.class);
                                            }
                                        }

                                        shops = response.body().getShops();
                                        if (shops != null) {
                                            Utils.writelogFile(context, "shops != null y shops.size()(Fragmentmain, Repository)");
                                            if (shops.size() > 0) {
                                                Utils.writelogFile(context, "shops.size() > 0 for(Fragmentmain, Repository)");
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
                                                Utils.writelogFile(context, "shops == 0 y Delete Shop(Fragmentmain, Repository)");
                                                Delete.table(Shop.class);
                                            }
                                        }

                                        offers = response.body().getOffers();
                                        if (offers != null) {
                                            Utils.writelogFile(context, "offers != null y offers.size()(Fragmentmain, Repository)");
                                            if (offers.size() > 0) {
                                                Utils.writelogFile(context, "offers.size() > 0 y for(Fragmentmain, Repository)");
                                                for (Offer offer : offers) {
                                                    offer.save();
                                                }
                                            } else {
                                                Utils.writelogFile(context, "offers == 0 y Delete Offer(Fragmentmain, Repository)");
                                                Delete.table(Offer.class);
                                            }
                                        }

                                        idsShops = response.body().getIdsShops();
                                        if (idsShops != null) {
                                            Utils.writelogFile(context, "idsShops != null y idsShops.size()(Fragmentmain, Repository)");
                                            if (idsShops.size() > 0) {
                                                Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(Fragmentmain, Repository)");
                                                for (Integer i : idsShops) {
                                                    setUpdateCatSub(context, i);
                                                }
                                            }
                                        }

                                        idsOffers = response.body().getIdsOffers();
                                        if (idsOffers != null) {
                                            Utils.writelogFile(context, "idsOffers != null y idsOffers.size()(Fragmentmain, Repository)");
                                            if (idsOffers.size() > 0) {
                                                Utils.writelogFile(context, "idsOffers.size() > 0 y for idsOffers(Fragmentmain, Repository)");
                                                for (Integer i : idsOffers) {
                                                    int id_sub = updateShopAndIdSub(context, i);
                                                    setUpdateCatSub(context, id_sub);
                                                }
                                            }
                                        }

                                        draws = response.body().getDraws();
                                        if (draws != null) {
                                            Utils.writelogFile(context, "draws != null y draws.size()(Fragmentmain, Repository)");
                                            if (draws.size() > 0) {
                                                Utils.writelogFile(context, "draws.size() > 0 y for draws(Fragmentmain, Repository)");
                                                for (Draw draw : draws) {
                                                    if (draw.getIS_ACTIVE() == 0) {
                                                        Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (Fragmentmain, Repository)");
                                                        if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                            Utils.writelogFile(context, "isDrawWinner = true (Fragmentmain, Repository)");
                                                            if (draw.getIS_TAKE() == 0) {
                                                                Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (Fragmentmain, Repository)");
                                                                if (draw.getIS_LIMITE() == 0) {
                                                                    Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(Fragmentmain, Repository)");
                                                                    draw.setIS_WINNER(1);
                                                                    draw.update();
                                                                } else {
                                                                    Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(Fragmentmain, Repository)");
                                                                    deleteDrawAndWinner(draw);
                                                                }
                                                            } else {
                                                                Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (Fragmentmain, Repository)");
                                                                deleteDrawWinner(draw.getID_DRAW_KEY());
                                                                draw.delete();
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "no winner delete (Fragmentmain, Repository)");
                                                            draw.delete();
                                                        }
                                                    } else {
                                                        Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(Fragmentmain, Repository)");
                                                        draw.save();
                                                    }
                                                }
                                            } else {
                                                Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(Fragmentmain, Repository)");
                                                Delete.table(Draw.class);
                                            }
                                        }

                                        support = response.body().getSupport();
                                        if (support != null) {
                                            Utils.writelogFile(context, "support != null y delete Support(Fragmentmain, Repository)");
                                            Delete.table(Support.class);
                                            Utils.writelogFile(context, "delete Support ok y save support(Fragmentmain, Repository)");
                                            support.save();
                                            Utils.writelogFile(context, "save Support ok y GOTONAV(Fragmentmain, Repository)");
                                        }
                                        Utils.writelogFile(context, "post CALLSHOPS(Fragmentmain, Repository)");
                                        if (!isFollow) {
                                            if (!isMyShop)
                                                post(FragmentMainEvent.CALLSHOPS);
                                            else
                                                post(FragmentMainEvent.MYSHOPS);
                                        }
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "responseWS.getSuccess().equals(4) y post WITHOUTCHANGE(Fragmentmain, Repository)");
                                        if (!isFollow)
                                            post(FragmentMainEvent.WITHOUTCHANGE);
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Fragmentmain, Repository)");
                                        post(FragmentMainEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS == null(Fragmentmain, Repository)");
                                    post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultShop> call, Throwable t) {
                            Utils.writelogFile(context, " Call error " + t.getMessage() + "(Fragmentmain, Repository)");
                            post(FragmentMainEvent.ERROR, t.getMessage());
                        }
                    });
                } else {
                    Utils.writelogFile(context, "DateUserCity == null(Fragmentmain, Repository)");
                    post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
                }
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

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

    @Override
    public void onClickFollowOrUnFollow(final Context context, final Shop shop, final boolean isMyShop,  final boolean isFollow) {
        Utils.writelogFile(context, "Metodo onClickFollowOrUnFollow y Se valida conexion a internet(Fragmentmain, Repository)");
        refreshLayout(context, isMyShop, true);
        if (getIdToken(context) != 0) {
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
                                        if (follow != -1) {
                                            Utils.writelogFile(context, "getId != 0 y fill shop object save y post FOLLOW(Fragmentmain, Repository)");
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
                                                if (shopFollow != null) {
                                                    Utils.writelogFile(context, "shopFollow != null(Fragmentmain, Repository)");
                                                    shopFollow.delete();
                                                } else {
                                                    Utils.writelogFile(context, "shopFollow == null(Fragmentmain, Repository)");
                                                    post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
                                                }
                                            }
                                            post(FragmentMainEvent.FOLLOWORUNFOLLOW, shop);
                                        } else {
                                            Utils.writelogFile(context, "Base de datos error(Fragmentmain, Repository)");
                                            post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
                                        }
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(Fragmentmain, Repository)");
                                        post(FragmentMainEvent.ERROR, response.body().getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "Base de datos error(Fragmentmain, Repository)");
                                    post(FragmentMainEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseWS> call, Throwable t) {
                            Utils.writelogFile(context, "Call error " + t.getMessage() + "(Fragmentmain, Repository)");
                            post(FragmentMainEvent.ERROR, t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Utils.writelogFile(context, "catch error " + e.getMessage() + "(Fragmentmain, Repository)");
                    post(FragmentMainEvent.ERROR, e.getMessage());
                }
            } else {
                Utils.writelogFile(context, "Internet error(Fragmentmain, Repository)");
                post(FragmentMainEvent.ERROR, context.getString(R.string.error_internet));
            }
        } else {
            Utils.writelogFile(context, "Error token(Fragmentmain, Repository)");
            post(FragmentMainEvent.ERROR, context.getString(R.string.error_id_device));
        }
    }

    private boolean setDateUserCity(Context context, String date) {
        Utils.writelogFile(context, "setDateUserCity(Fragmentmain, Repository)");
        try {

            SQLite.update(DateUserCity.class)
                    .set(DateUserCity_Table.SHOP_DATE.eq(date), DateUserCity_Table.DATE_USER_CITY.eq(date))
                    .async()
                    .execute();

            return true;
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Fragmentmain, Repository)");
            return false;
        }
    }

    private ShopFollow getShopFollow(int id_shop) {
        try {
            return SQLite.select().from(ShopFollow.class).where(ShopFollow_Table.ID_SHOP_FOREIGN.is(id_shop)).querySingle();
        } catch (Exception e) {
            return null;
        }
    }

    private int getIdToken(Context context) {
        Utils.writelogFile(context, "getIdToken(Fragmentmain, Repository)");
        try {
            return SQLite.select(Token_Table.ID_TOKEN_KEY).from(Token.class).where(Token_Table.ID_CITY_FOREIGN.is(Utils.getIdCity(context)))
                    .querySingle().getID_TOKEN_KEY();
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean setUpdateCatSub(Context context, int id_sub) {
        Utils.writelogFile(context, "metodo setUpdateCatSub(Fragmentmain, Repository)");

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

    private void post(int type) {
        post(type, null, null, null, null, null);
    }

    private void post(int type, List<Shop> shopsList) {
        post(type, shopsList, null, null, null, null);
    }

    private void post(int type, List<Offer> offers, boolean isOffer) {
        post(type, null, offers, null, null, null);
    }

    private void post(int type, Shop shop) {
        post(type, null, null, shop, null, null);
    }

    private void post(int type, String error) {
        post(type, null, null, null, null, error);
    }

    private void post(int type, List<Shop> shopsList, List<Offer> offers, Shop shop, DateUserCity dateUserCity, String error) {
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
