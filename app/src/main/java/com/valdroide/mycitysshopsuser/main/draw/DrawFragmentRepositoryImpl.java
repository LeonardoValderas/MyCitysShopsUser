package com.valdroide.mycitysshopsuser.main.draw;

import android.content.Context;

import com.bumptech.glide.util.Util;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
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
import com.valdroide.mycitysshopsuser.entities.shop.Draw_Table;
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
import com.valdroide.mycitysshopsuser.main.draw.events.DrawFragmentEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawFragmentRepositoryImpl implements DrawFragmentRepository {
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

    public DrawFragmentRepositoryImpl(APIService service, EventBus eventBus) {
        this.service = service;
        this.eventBus = eventBus;
    }

    @Override
    public void getDraws(Context context) {
        Utils.writelogFile(context, "getDraws(DrawFragment, Repository)");
        try {
            draws = SQLite.select().from(Draw.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(ShopFollow_Table.ID_SHOP_FOREIGN.eq(Draw_Table.ID_SHOP_FOREIGN_DRAW.withTable()))
                    .where(OperatorGroup.clause().and(Shop_Table.ID_CITY_FOREIGN.eq(Draw_Table.ID_CITY_FOREIGN.withTable())).and(Draw_Table.FOR_FOLLOWING.is(1)).or(ShopFollow_Table.ID_SHOP_FOLLOW_KEY.isNotNull()))
                    .orderBy(Draw_Table.ID_DRAW_KEY, false).queryList();

            if (draws.size() > 0) {

                Iterator<Draw> iteratorDraw = draws.iterator();
                while (iteratorDraw.hasNext()) {
                    Draw draw = iteratorDraw.next();
                    if (Utils.validateExpirateCurrentTime(draw.getEND_DATE(), "yyyy-MM-dd HH:mm:ss")) {
                        if (draw.getIS_WINNER() == 0) {
                            iteratorDraw.remove();
                        }
                    }
                }
            }
            post(DrawFragmentEvent.DRAWS, draws);
        } catch (Exception e) {
            Utils.writelogFile(context, "getListShops(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
    }

    @Override
    public void participate(final Context context, final Draw draw, String dni, String name) {
        Utils.writelogFile(context, "Metodo participate y Se valida conexion a internet(DrawFragment, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "internet ok y Call participateDraw metodo (DrawFragment, Repository)");
                Call<ResponseWS> drawService = service.participateDraw(Utils.getIdCity(context), draw.getID_DRAW_KEY(),
                        getIdToken(context), dni, name);
                drawService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Utils.writelogFile(context, "Response Successful y get ResponseWS(DrawFragment, Repository)");
                                responseWS = response.body();
                                if (responseWS != null) {
                                    Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(DrawFragment, Repository)");
                                    if (responseWS.getSuccess().equals("0")) {
                                        Utils.writelogFile(context, "getSuccess = 0 y update(DrawFragment, Repository)");
                                        draw.setPARTICIPATION(1);
                                        draw.update();
                                        Utils.writelogFile(context, "update ok y post(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.PARTICIPATESUCCESS);
                                    } else if (responseWS.getSuccess().equals("4")) {
                                        Utils.writelogFile(context, "responseWS.getSuccess().equals(4)DNI ya registrado(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.ERROR, context.getString(R.string.errro_dni));
                                    } else {
                                        Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.ERROR, responseWS.getMessage());
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS == null(DrawFragment, Repository)");
                                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "!response.isSuccessful(DrawFragment, Repository)");
                                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        } else {
                            Utils.writelogFile(context, "response == null(DrawFragment, Repository)");
                            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(DrawFragment, Repository)");
                        post(DrawFragmentEvent.ERROR, t.getMessage());
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
                post(DrawFragmentEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_internet));
        }
    }

    @Override
    public void refreshLayout(final Context context) {
        Utils.writelogFile(context, "Metodo refreshLayout y Se valida conexion a internet(DrawFragment, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                dateUserCity = getDateUserCity(context);
                if (dateUserCity != null) {
                    Utils.writelogFile(context, "dateUserCity != null y Call validateDateUser metodo refreshLayout(DrawFragment, Repository)");
                    Call<ResultShop> splashService = service.validateDateUser(Utils.getIdCity(context), dateUserCity.getCATEGORY_DATE(),
                            dateUserCity.getSUBCATEGORY_DATE(), dateUserCity.getSHOP_DATE(),
                            dateUserCity.getOFFER_DATE(), dateUserCity.getDRAW_DATE(), dateUserCity.getSUPPORT_DATE(), dateUserCity.getDATE_USER_CITY());
                    splashService.enqueue(new Callback<ResultShop>() {
                        @Override
                        public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                            if (response != null) {
                                if (response.isSuccessful()) {
                                    Utils.writelogFile(context, "Response Successful y get ResponseWS(DrawFragment, Repository)");
                                    responseWS = response.body().getResponseWS();
                                    if (responseWS != null) {
                                        Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(DrawFragment, Repository)");
                                        if (responseWS.getSuccess().equals("0")) {
                                            Utils.writelogFile(context, " getSuccess = 0 y getDateUserCity(DrawFragment, Repository)");
                                            dateUserCity = response.body().getDateUserCity();
                                            if (dateUserCity != null) {
                                                Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(DrawFragment, Repository)");
                                                Delete.table(DateUserCity.class);
                                                Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(DrawFragment, Repository)");
                                                dateUserCity.save();
                                                Utils.writelogFile(context, "save dateUserCity y getCategories(DrawFragment, Repository)");
                                            }

                                            categories = response.body().getCategories();
                                            if (categories != null) {
                                                Utils.writelogFile(context, "categories != null y categories.size()(DrawFragment, Repository)");
                                                if (categories.size() > 0) {
                                                    Utils.writelogFile(context, "categories.size() > 0 y Delete Category(DrawFragment, Repository)");
                                                    Delete.table(Category.class);
                                                    Utils.writelogFile(context, "Delete Category y FOR categories(DrawFragment, Repository)");
                                                    transaction = FastStoreModelTransaction
                                                            .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                            .addAll(categories)
                                                            .build();
                                                    database.executeTransaction(transaction);
                                                } else {
                                                    Utils.writelogFile(context, "categories == 0 y Delete Category(DrawFragment, Repository)");
                                                    Delete.table(Category.class);
                                                }
                                            }

                                            subCategories = response.body().getSubCategories();
                                            if (subCategories != null) {
                                                Utils.writelogFile(context, "subCategories != null y subCategories.size()(DrawFragment, Repository)");
                                                if (subCategories.size() > 0) {
                                                    Utils.writelogFile(context, "subCategories.size() > 0 y Delete SubCategory(DrawFragment, Repository)");
                                                    Delete.table(SubCategory.class);
                                                    Utils.writelogFile(context, "Delete SubCategory y FOR subCategories(DrawFragment, Repository)");
                                                    transaction = FastStoreModelTransaction
                                                            .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                            .addAll(subCategories)
                                                            .build();
                                                    database.executeTransaction(transaction);
                                                } else {
                                                    Utils.writelogFile(context, "subCategories == 0 y Delete SubCategory(DrawFragment, Repository)");
                                                    Delete.table(SubCategory.class);
                                                }
                                            }

                                            shops = response.body().getShops();
                                            if (shops != null) {
                                                Utils.writelogFile(context, "shops != null y shops.size()(DrawFragment, Repository)");
                                                if (shops.size() > 0) {
                                                    Utils.writelogFile(context, "shops.size() > 0 for(DrawFragment, Repository)");
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
                                                    Utils.writelogFile(context, "shops == 0 y Delete Shop(DrawFragment, Repository)");
                                                    Delete.table(Shop.class);
                                                }
                                            }

                                            offers = response.body().getOffers();
                                            if (offers != null) {
                                                Utils.writelogFile(context, "offers != null y offers.size()(DrawFragment, Repository)");
                                                if (offers.size() > 0) {
                                                    Utils.writelogFile(context, "offers.size() > 0 y for(DrawFragment, Repository)");
                                                    for (Offer offer : offers) {
                                                        offer.save();
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "offers == 0 y Delete Offer(DrawFragment, Repository)");
                                                    Delete.table(Offer.class);
                                                }
                                            }

                                            idsShops = response.body().getIdsShops();
                                            if (idsShops != null) {
                                                Utils.writelogFile(context, "idsShops != null y idsShops.size()(DrawFragment, Repository)");
                                                if (idsShops.size() > 0) {
                                                    Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(DrawFragment, Repository)");
                                                    for (Integer i : idsShops) {
                                                        setUpdateCatSub(context, i);
                                                    }
                                                }
                                            }

                                            idsOffers = response.body().getIdsOffers();
                                            if (idsOffers != null) {
                                                Utils.writelogFile(context, "idsOffers != null y idsOffers.size()(DrawFragment, Repository)");
                                                if (idsOffers.size() > 0) {
                                                    Utils.writelogFile(context, "idsOffers.size() > 0 y for idsOffers(DrawFragment, Repository)");
                                                    for (Integer i : idsOffers) {
                                                        int id_sub = updateShopAndIdSub(context, i);
                                                        setUpdateCatSub(context, id_sub);
                                                    }
                                                }
                                            }

                                            draws = response.body().getDraws();
                                            if (draws != null) {
                                                Utils.writelogFile(context, "draws != null y draws.size()(DrawFragment, Repository)");
                                                if (draws.size() > 0) {
                                                    Utils.writelogFile(context, "draws.size() > 0 y for draws(DrawFragment, Repository)");
                                                    for (Draw draw : draws) {
                                                        if (draw.getIS_ACTIVE() == 0) {
                                                            Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (DrawFragment, Repository)");
                                                            if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                                Utils.writelogFile(context, "isDrawWinner = true (DrawFragment, Repository)");
                                                                if (draw.getIS_TAKE() == 0) {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (DrawFragment, Repository)");
                                                                    if (draw.getIS_LIMITE() == 0) {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(DrawFragment, Repository)");
                                                                        draw.setIS_WINNER(1);
                                                                        draw.update();
                                                                    } else {
                                                                        Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(DrawFragment, Repository)");
                                                                        deleteDrawAndWinner(draw);
                                                                    }
                                                                } else {
                                                                    Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (DrawFragment, Repository)");
                                                                    deleteDrawWinner(draw.getID_DRAW_KEY());
                                                                    draw.delete();
                                                                }
                                                            } else {
                                                                Utils.writelogFile(context, "no winner delete (DrawFragment, Repository)");
                                                                draw.delete();
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(DrawFragment, Repository)");
                                                            draw.save();
                                                        }
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(DrawFragment, Repository)");
                                                    Delete.table(Draw.class);
                                                }
                                            }

                                            support = response.body().getSupport();
                                            if (support != null) {
                                                Utils.writelogFile(context, "support != null y delete Support(DrawFragment, Repository)");
                                                Delete.table(Support.class);
                                                Utils.writelogFile(context, "delete Support ok y save support(DrawFragment, Repository)");
                                                support.save();
                                                Utils.writelogFile(context, "save Support ok y GOTONAV(DrawFragment, Repository)");
                                            }
                                            Utils.writelogFile(context, "post CALLDRAWS(DrawFragment, Repository)");
                                            post(DrawFragmentEvent.GETDRAWSREFRESH);
                                        } else if (responseWS.getSuccess().equals("4")) {
                                            Utils.writelogFile(context, "responseWS.getSuccess().equals(4) y post WITHOUTCHANGE(DrawFragment, Repository)");
                                            post(DrawFragmentEvent.WITHOUTCHANGE);
                                        } else {
                                            Utils.writelogFile(context, "getSuccess = error " + responseWS.getMessage() + "(DrawFragment, Repository)");
                                            post(DrawFragmentEvent.ERROR, responseWS.getMessage());
                                        }
                                    } else {
                                        Utils.writelogFile(context, "responseWS == null(DrawFragment, Repository)");
                                        post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                    }
                                } else {
                                    Utils.writelogFile(context, "!response.isSuccessful(DrawFragment, Repository)");
                                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                                }
                            } else {
                                Utils.writelogFile(context, "response == null(DrawFragment, Repository)");
                                post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultShop> call, Throwable t) {
                            Utils.writelogFile(context, " Call error " + t.getMessage() + "(DrawFragment, Repository)");
                            post(DrawFragmentEvent.ERROR, t.getMessage());
                        }
                    });
                } else {
                    Utils.writelogFile(context, "DateUserCity == null(DrawFragment, Repository)");
                    post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
                }
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
                post(DrawFragmentEvent.ERROR, e.getMessage());
            }
        } else {
            Utils.writelogFile(context, "Internet error(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_internet));
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

    @Override
    public void getDrawSearch(Context context, String letter) {
        Utils.writelogFile(context, "getDraws(DrawFragment, Repository)");
        OperatorGroup operators = OperatorGroup.clause().and(Draw_Table.SHOP_NAME.like("%" + letter + "%")).and(
                OperatorGroup.clause().and(Shop_Table.ID_CITY_FOREIGN.eq(Draw_Table.ID_CITY_FOREIGN.withTable())).
                        and(Draw_Table.FOR_FOLLOWING.is(1)).or(ShopFollow_Table.ID_SHOP_FOLLOW_KEY.isNotNull()));
        draws.clear();
        try {
            draws = SQLite.select().from(Draw.class)
                    .leftOuterJoin(ShopFollow.class)
                    .on(ShopFollow_Table.ID_SHOP_FOREIGN.eq(Draw_Table.ID_SHOP_FOREIGN_DRAW.withTable()))
                    .where(operators)
                    .orderBy(Draw_Table.ID_DRAW_KEY, false).queryList();
            post(DrawFragmentEvent.DRAWS, draws);
        } catch (Exception e) {
            Utils.writelogFile(context, "getListShops(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, context.getString(R.string.error_data_base));
        }
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

    private DateUserCity getDateUserCity(Context context) {
        Utils.writelogFile(context, "getDateUserCity(DrawFragment, Repository)");
        try {
            return SQLite.select().from(DateUserCity.class).querySingle();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DrawFragment, Repository)");
            post(DrawFragmentEvent.ERROR, e.getMessage());
            return null;
        }
    }

    private boolean setUpdateCatSub(Context context, int id_sub) {
        Utils.writelogFile(context, "setUpdateCatSub(DrawFragment, Repository)");

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
        Utils.writelogFile(context, "updateShopAndIdSub(DrawFragment, Repository)");
        SQLite.update(Shop.class)
                .set(Shop_Table.IS_OFFER_UPDATE.eq(1))
                .where(Shop_Table.ID_SHOP_KEY.is(id_shop))
                .async()
                .execute();
        return SQLite.select(Shop_Table.ID_SUBCATEGORY_FOREIGN).from(Shop.class).querySingle().getID_SUBCATEGORY_FOREIGN();
    }

    private int getIdToken(Context context) {
        Utils.writelogFile(context, "getIdToken(DrawFragment, Repository)");
        return SQLite.select(Token_Table.ID_TOKEN_KEY).from(Token.class).where(Token_Table.ID_CITY_FOREIGN.eq(Utils.getIdCity(context))).querySingle().getID_TOKEN_KEY();
    }

    private void post(int type) {
        post(type, null, null);
    }

    private void post(int type, String error) {
        post(type, null, error);
    }

    private void post(int type, List<Draw> draws) {
        post(type, draws, null);
    }

    private void post(int type, List<Draw> draws, String error) {
        DrawFragmentEvent event = new DrawFragmentEvent();
        event.setType(type);
        event.setDrawList(draws);
        event.setError(error);
        eventBus.post(event);
    }
}
