package com.valdroide.mycitysshopsuser.main.offers;


import android.content.Context;

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
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
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
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.offers.events.OfferFragmentEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferFragmentRepositoryImpl implements OfferFragmentRepository {
    private APIService service;
    private EventBus eventBus;
    private ResponseWS responseWS;
    private List<Draw> draws;
    private List<Offer> offers;
    private List<Shop> shops;
    private List<Category> categories;
    private List<SubCategory> subCategories;
    private DateUserCity dateUserCity;
    private Support support;
    private List<Integer> idsShops;
    private List<Integer> idsOffers;
    private DatabaseDefinition database = FlowManager.getDatabase(ShopsDatabase.class);
    private FastStoreModelTransaction transaction;


    public OfferFragmentRepositoryImpl(APIService service, EventBus eventBus) {
        this.service = service;
        this.eventBus = eventBus;
    }

    @Override
    public void getOffers(Context context) {
        try {
            Utils.writelogFile(context, "getOffers(OfferFragment, Repository)");
            offers = SQLite.select(Offer_Table.ID_OFFER_KEY, Offer_Table.ID_CITY_FOREIGN.withTable(),
                    Offer_Table.ID_SHOP_FOREIGN, Offer_Table.IS_ACTIVE,
                    Offer_Table.DATE_UNIQUE.withTable(), Offer_Table.OFFER, Offer_Table.TITLE, Offer_Table.URL_IMAGE,
                    Shop_Table.SHOP.as("SHOP").withTable()).from(Offer.class)
                    .innerJoin(Shop.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(Offer_Table.ID_SHOP_FOREIGN.withTable()))
                    .where(Offer_Table.IS_ACTIVE.is(1))
                    .orderBy(Shop_Table.FOLLOW, false).orderBy(Offer_Table.ID_OFFER_KEY, false).queryList();

            post(OfferFragmentEvent.OFFERS, offers);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch: " + e.getMessage() + "(OfferFragment, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private void deleteOfferForId(Context context, List<Integer> ids) {
        Utils.writelogFile(context, "deleteOfferForId(OfferList, Repository)");
        for (int i = 0; i < ids.size(); i++) {
            for (int j = 0; j < offers.size(); j++) {
                if (offers.get(j).getID_OFFER_KEY() == ids.get(i)) {
                    offers.get(j).delete();
                    offers.remove(j);
                    break;
                }
            }
        }
    }

    private int getQuantityOffer(Context context) {
        Utils.writelogFile(context, "getQuantityOffer(OfferFragment, Repository)");
        return SQLite.select(Shop_Table.QUANTITY_OFFER).from(Shop.class).querySingle().getQUANTITY_OFFER();
    }

    @Override
    public void refreshLayout(final Context context) {
        Utils.writelogFile(context, "Metodo refreshLayout y Se valida conexion a internet(OfferFragment, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                dateUserCity = getDateUserCity(context);
                if (dateUserCity != null) {
                    Utils.writelogFile(context, "dateUserCity != null y Call validateDateUser metodo refreshLayout(OfferFragment, Repository)");
                    Call<ResultShop> splashService = service.validateDateUser(Utils.getIdCity(context), dateUserCity.getCATEGORY_DATE(),
                            dateUserCity.getSUBCATEGORY_DATE(), dateUserCity.getSHOP_DATE(),
                            dateUserCity.getOFFER_DATE(), dateUserCity.getDRAW_DATE(), dateUserCity.getSUPPORT_DATE(), dateUserCity.getDATE_USER_CITY());
                    splashService.enqueue(new Callback<ResultShop>() {
                        @Override
                        public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                            if (response != null) {
                                if (response.isSuccessful()) {
                                    Utils.writelogFile(context, "Response Successful y get ResponseWS(OfferFragment, Repository)");
                                    responseWS = response.body().getResponseWS();
                                    if (responseWS != null) {
                                        Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(OfferFragment, Repository)");
                                        if (responseWS.getSuccess().equals("0")) {
                                            Utils.writelogFile(context, " getSuccess = 0 y getDateUserCity(OfferFragment, Repository)");
                                            dateUserCity = response.body().getDateUserCity();
                                            if (dateUserCity != null) {
                                                Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(OfferFragment, Repository)");
                                                Delete.table(DateUserCity.class);
                                                Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(OfferFragment, Repository)");
                                                dateUserCity.save();
                                                Utils.writelogFile(context, "save dateUserCity y getCategories(OfferFragment, Repository)");
                                            }

                                            categories = response.body().getCategories();
                                            if (categories != null) {
                                                Utils.writelogFile(context, "categories != null y categories.size()(OfferFragment, Repository)");
                                                if (categories.size() > 0) {
                                                    Utils.writelogFile(context, "categories.size() > 0 y Delete Category(OfferFragment, Repository)");
                                                    Delete.table(Category.class);
                                                    Utils.writelogFile(context, "Delete Category ok y transaction(OfferFragment, Repository)");
                                                    transaction = FastStoreModelTransaction
                                                            .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                            .addAll(categories)
                                                            .build();
                                                    database.executeTransaction(transaction);
                                                } else {
                                                    Utils.writelogFile(context, "categories == 0 y Delete Category(OfferFragment, Repository)");
                                                    Delete.table(Category.class);
                                                }
                                            }

                                            subCategories = response.body().getSubCategories();
                                            if (subCategories != null) {
                                                Utils.writelogFile(context, "subCategories != null y subCategories.size()(OfferFragment, Repository)");
                                                if (subCategories.size() > 0) {
                                                    Utils.writelogFile(context, "subCategories.size() > 0 y Delete SubCategory(OfferFragment, Repository)");
                                                    Delete.table(SubCategory.class);
                                                    Utils.writelogFile(context, "Delete SubCategory ok y transaction(OfferFragment, Repository)");
                                                    transaction = FastStoreModelTransaction
                                                            .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                            .addAll(subCategories)
                                                            .build();
                                                    database.executeTransaction(transaction);
                                                } else {
                                                    Utils.writelogFile(context, "subCategories == 0 y Delete SubCategory(OfferFragment, Repository)");
                                                    Delete.table(SubCategory.class);
                                                }
                                            }

                                            shops = response.body().getShops();
                                            if (shops != null) {
                                                Utils.writelogFile(context, "shops != null y shops.size()(OfferFragment, Repository)");
                                                if (shops.size() > 0) {
                                                    Utils.writelogFile(context, "shops.size() > 0 for(OfferFragment, Repository)");
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
                                                    Utils.writelogFile(context, "shops == 0 y Delete Shop(OfferFragment, Repository)");
                                                    Delete.table(Shop.class);
                                                }
                                            }

                                            offers = response.body().getOffers();
                                            if (offers != null) {
                                                Utils.writelogFile(context, "offers != null y offers.size()(OfferFragment, Repository)");
                                                if (offers.size() > 0) {
                                                    Utils.writelogFile(context, "offers.size() > 0 y for(OfferFragment, Repository)");
                                                    for (Offer offer : offers) {
                                                        offer.save();
                                                    }
                                                }
                                            } else {
                                                Utils.writelogFile(context, "offers == 0 y Delete Offer(OfferFragment, Repository)");
                                                Delete.table(Offer.class);
                                            }

                                            draws = response.body().getDraws();
                                            if (draws != null) {
                                                Utils.writelogFile(context, "draws != null y draws.size()(OfferFragment, Repository)");
                                                if (draws.size() > 0) {
                                                    Utils.writelogFile(context, "draws.size() > 0 y for draws(OfferFragment, Repository)");
                                                    for (Draw draw : draws) {
                                                        if (draw.getIS_ACTIVE() == 0) {
                                                            Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (OfferFragment, Repository)");
                                                            if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                                Utils.writelogFile(context, "isDrawWinner = true (OfferFragment, Repository)");
                                                                if (draw.getIS_TAKE() == 0) {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (OfferFragment, Repository)");
                                                                    if (draw.getIS_LIMITE() == 0) {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(OfferFragment, Repository)");
                                                                        draw.setIS_WINNER(1);
                                                                        draw.update();
                                                                    } else {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(OfferFragment, Repository)");
                                                                        deleteDrawAndWinner(draw);
                                                                    }
                                                                } else {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (OfferFragment, Repository)");
                                                                    deleteDrawWinner(draw.getID_DRAW_KEY());
                                                                    draw.delete();
                                                                }
                                                            } else {
                                                                Utils.writelogFile(context, "no winner delete (OfferFragment, Repository)");
                                                                draw.delete();
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(OfferFragment, Repository)");
                                                            draw.save();
                                                        }
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(OfferFragment, Repository)");
                                                    Delete.table(Draw.class);
                                                }
                                            }

                                            idsShops = response.body().getIdsShops();
                                            if (idsShops != null) {
                                                Utils.writelogFile(context, "idsShops != null y idsShops.size()(OfferFragment, Repository)");
                                                if (idsShops.size() > 0) {
                                                    Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(OfferFragment, Repository)");
                                                    for (Integer i : idsShops) {
                                                        setUpdateCatSub(context, i);
                                                    }
                                                }
                                            }

                                            idsOffers = response.body().getIdsOffers();
                                            if (idsOffers != null) {
                                                Utils.writelogFile(context, "idsOffers != null y idsOffers.size()(OfferFragment, Repository)");
                                                if (idsOffers.size() > 0) {
                                                    Utils.writelogFile(context, "idsOffers.size() > 0 y for idsOffers(OfferFragment, Repository)");
                                                    for (Integer i : idsOffers) {
                                                        int id_sub = updateShopAndIdSub(context, i);
                                                        setUpdateCatSub(context, id_sub);
                                                    }
                                                }
                                            }

                                            draws = response.body().getDraws();
                                            if (draws != null) {
                                                Utils.writelogFile(context, "draws != null y draws.size()(OfferFragment, Repository)");
                                                if (draws.size() > 0) {
                                                    Utils.writelogFile(context, "draws.size() > 0 y for draws(OfferFragment, Repository)");
                                                    for (Draw draw : draws) {
                                                        if (draw.getIS_ACTIVE() == 0) {
                                                            Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (OfferFragment, Repository)");
                                                            if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                                Utils.writelogFile(context, "isDrawWinner = true (OfferFragment, Repository)");
                                                                if (draw.getIS_TAKE() == 0) {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (OfferFragment, Repository)");
                                                                    if (draw.getIS_LIMITE() == 0) {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(OfferFragment, Repository)");
                                                                        draw.setIS_WINNER(1);
                                                                        draw.update();
                                                                    } else {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(OfferFragment, Repository)");
                                                                        deleteDrawAndWinner(draw);
                                                                    }
                                                                } else {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (OfferFragment, Repository)");
                                                                    deleteDrawWinner(draw.getID_DRAW_KEY());
                                                                    draw.delete();
                                                                }
                                                            } else {
                                                                Utils.writelogFile(context, "no winner delete (OfferFragment, Repository)");
                                                                draw.delete();
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(OfferFragment, Repository)");
                                                            draw.save();
                                                        }
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(OfferFragment, Repository)");
                                                    Delete.table(Draw.class);
                                                }
                                            }

                                            support = response.body().getSupport();
                                            if (support != null) {
                                                Utils.writelogFile(context, "support != null y delete Support(OfferFragment, Repository)");
                                                Delete.table(Support.class);
                                                Utils.writelogFile(context, "delete Support ok y save support(OfferFragment, Repository)");
                                                support.save();
                                                Utils.writelogFile(context, "save Support ok y GOTONAV(OfferFragment, Repository)");
                                            }
                                            Utils.writelogFile(context, "post CALLDRAWS(OfferFragment, Repository)");
                                            post(OfferFragmentEvent.GETOFFERSREFRESH);
                                        } else if (responseWS.getSuccess().equals("4")) {
                                            Utils.writelogFile(context, "responseWS.getSuccess().equals(4) y post WITHOUTCHANGE(OfferFragment, Repository)");
                                            post(OfferFragmentEvent.WITHOUTCHANGE);
                                        } else {
                                            Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(OfferFragment, Repository)");
                                            post(OfferFragmentEvent.ERROR, responseWS.getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "responseWS == null(OfferFragment, Repository)");
                                        post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, "!response.isSuccessful(OfferFragment, Repository)");
                                    post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "response == null(OfferFragment, Repository)");
                                post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultShop> call, Throwable t) {
                            Utils.writelogFile(context, " Call error " + t.getMessage() + "(OfferFragment, Repository)");
                            post(OfferFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } else {
                    Utils.writelogFile(context, "DateUserCity == null(OfferFragment, Repository)");
                    post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                }
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(OfferFragment, Repository)");
                post(OfferFragmentEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(OfferFragment, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_internet));
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
    public void getOfferSearch(Context context, String letter) {
        Utils.writelogFile(context, "getOffers(OfferFragment, Repository)");
        try {
            offers = SQLite.select(Offer_Table.ID_OFFER_KEY, Offer_Table.ID_CITY_FOREIGN.withTable(),
                    Offer_Table.ID_SHOP_FOREIGN, Offer_Table.IS_ACTIVE,
                    Offer_Table.DATE_UNIQUE.withTable(), Offer_Table.OFFER, Offer_Table.TITLE, Offer_Table.URL_IMAGE,
                    Shop_Table.SHOP.as("SHOP").withTable()).from(Offer.class)
                    .innerJoin(Shop.class)
                    .on(Shop_Table.ID_SHOP_KEY.eq(Offer_Table.ID_SHOP_FOREIGN.withTable()))
                    .where(OperatorGroup.clause().and(Shop_Table.SHOP.withTable().like("%" + letter + "%")))
                    .orderBy(Shop_Table.FOLLOW, false).queryList();

            post(OfferFragmentEvent.OFFERS, offers);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch: " + e.getMessage() + "(OfferFragment, Repository)");
            post(OfferFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    private DateUserCity getDateUserCity(Context context) {
        Utils.writelogFile(context, "getDateUserCity(OfferFragment, Repository)");
        try {
            return SQLite.select().from(DateUserCity.class).querySingle();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(OfferFragment, Repository)");
            post(OfferFragmentEvent.ERROR, e.getMessage());
            return null;
        }
    }

    private int updateShopAndIdSub(Context context, int id_shop) {
        Utils.writelogFile(context, "updateShopAndIdSub(OfferFragment, Repository)");
        SQLite.update(Shop.class)
                .set(Shop_Table.IS_SHOP_UPDATE.eq(1))
                .where(Shop_Table.ID_SHOP_KEY.is(id_shop))
                .async()
                .execute();
        return SQLite.select(Shop_Table.ID_SUBCATEGORY_FOREIGN).from(Shop.class).querySingle().getID_SUBCATEGORY_FOREIGN();
    }

    private boolean setUpdateCatSub(Context context, int id_sub) {
        Utils.writelogFile(context, "setUpdateCatSub(OfferFragment, Repository)");
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

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String error) {
        post(type, null, error);
    }

    private void post(int type, List<Offer> offers) {
        post(type, offers, null);
    }

    private void post(int type, List<Offer> offers, String error) {
        OfferFragmentEvent event = new OfferFragmentEvent();
        event.setType(type);
        event.setOfferList(offers);
        event.setError(error);
        eventBus.post(event);
    }
}
