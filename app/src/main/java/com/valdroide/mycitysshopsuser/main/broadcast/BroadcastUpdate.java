package com.valdroide.mycitysshopsuser.main.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
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
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastUpdate extends BroadcastReceiver {
    ShopClient client = new ShopClient();
    private APIService service;
    private ResponseWS responseWS;
    private List<Offer> offers;
    private List<Shop> shops;
    private List<Category> categories;
    private List<SubCategory> subCategories;
    private DateUserCity dateUserCity;
    private DatabaseDefinition database = FlowManager.getDatabase(ShopsDatabase.class);
    private FastStoreModelTransaction transaction;
    private List<Draw> draws;
    private List<Integer> idsShops;
    private List<Integer> idsOffers;


    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.writelogFile(context, "Se inicia onReceive(BroadcastUpdate)");
        try {
            service = client.getAPIService();
            dateUserCity = SQLite.select().from(DateUserCity.class).querySingle();
            if (dateUserCity != null)
                validateDateShop(context, dateUserCity);
            else
                Utils.writelogFile(context, "dateUserCity null(BroadcastUpdate)");
        } catch (Exception e) {
            Utils.writelogFile(context, "onReceive error: " + e.getMessage() + "(BroadcastUpdate)");
        }
    }


    public void validateDateShop(final Context context, DateUserCity dateUserCityWS) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(BroadcastUpdate)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateUser(BroadcastUpdate)");
                Call<ResultShop> validateDateUser = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getSHOP_DATE(),
                        dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDRAW_DATE(), dateUserCityWS.getSUPPORT_DATE(), dateUserCityWS.getDATE_USER_CITY());
                validateDateUser.enqueue(new Callback<ResultShop>() {
                    @Override
                    public void onResponse(Call<ResultShop> call, Response<ResultShop> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "Response Successful y get ResponseWS(BroadcastUpdate)");
                            responseWS = response.body().getResponseWS();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "ResponseWS != null y valida getSuccess(BroadcastUpdate)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, " getSuccess = 0 y dateUserCity(BroadcastUpdate)");
                                    dateUserCity = response.body().getDateUserCity();
                                    if (dateUserCity != null) {
                                        Utils.writelogFile(context, "dateUserCity != null y delete DateUserCity(BroadcastUpdate)");
                                        Delete.table(DateUserCity.class);
                                        Utils.writelogFile(context, "delete DateUserCity ok y save dateUserCity(BroadcastUpdate)");
                                        dateUserCity.save();
                                        Utils.writelogFile(context, "save dateUserCity y getCategories(BroadcastUpdate)");
                                    }

                                    categories = response.body().getCategories();
                                    if (categories != null) {
                                        Utils.writelogFile(context, "categories != null y categories.size()(BroadcastUpdate)");
                                        if (categories.size() > 0) {
                                            Utils.writelogFile(context, "categories.size() > 0 y Delete.Category()(BroadcastUpdate)");
                                            Delete.table(Category.class);
                                            Utils.writelogFile(context, "Delete Category ok y transaction(BroadcastUpdate, BroadcastUpdate)");
                                            transaction = FastStoreModelTransaction
                                                    .saveBuilder(FlowManager.getModelAdapter(Category.class))
                                                    .addAll(categories)
                                                    .build();
                                            database.executeTransaction(transaction);                                        } else {
                                            Utils.writelogFile(context, "categories.size() == 0 y delete Category(BroadcastUpdate)");
                                            Delete.table(Category.class);
                                        }
                                    }

                                    subCategories = response.body().getSubCategories();
                                    if (subCategories != null) {
                                        Utils.writelogFile(context, "subCategories != null y subCategories.size()(BroadcastUpdate)");
                                        if (subCategories.size() > 0) {
                                            Utils.writelogFile(context, "subCategories.size() > 0 y Delete.SubCategory()(BroadcastUpdate)");
                                            Delete.table(SubCategory.class);
                                            Utils.writelogFile(context, "Delete SubCategory ok y transaction(Splash, BroadcastUpdate)");
                                            transaction = FastStoreModelTransaction
                                                    .insertBuilder(FlowManager.getModelAdapter(SubCategory.class))
                                                    .addAll(subCategories)
                                                    .build();
                                            database.executeTransaction(transaction);
                                        } else {
                                            Utils.writelogFile(context, "subCategories.size() == 0 y delete SubCategory(BroadcastUpdate)");
                                            Delete.table(SubCategory.class);
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y shops.size()(BroadcastUpdate)");
                                        if (shops.size() > 0) {
                                            Utils.writelogFile(context, "shops.size() > 0 for(Splash, BroadcastUpdate)");
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
                                            setCounterBadge(context);

                                        } else {
                                            Utils.writelogFile(context, "shops.size() == 0 y delete Shop(BroadcastUpdate)");
                                            Delete.table(Shop.class);
                                        }
                                    }

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y offers.size()(BroadcastUpdate)");
                                        if (offers.size() > 0) {
                                            Utils.writelogFile(context, "offers.size() > 0 y for(Splash, BroadcastUpdate)");
                                            for (Offer offer : offers) {
                                                offer.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "offers.size() == 0 y delete Offer(BroadcastUpdate)");
                                            Delete.table(Offer.class);
                                        }
                                    }

                                    idsShops = response.body().getIdsShops();
                                    if (idsShops != null) {
                                        Utils.writelogFile(context, "idsShops != null y idsShops.size()(Splash, BroadcastUpdate)");
                                        if (idsShops.size() > 0) {
                                            Utils.writelogFile(context, "idsShops.size() > 0 y for idsShops(Splash, BroadcastUpdate)");
                                            for (Integer i : idsShops) {
                                                setUpdateCatSub(context, i);
                                            }
                                        }
                                    }

                                    idsOffers = response.body().getIdsOffers();
                                    if (idsOffers != null) {
                                        Utils.writelogFile(context, "idsOffers != null y idsOffers.size()(Splash, BroadcastUpdate)");
                                        if (idsOffers.size() > 0) {
                                            Utils.writelogFile(context, "idsOffers.size() > 0 y for idsOffers(Splash, BroadcastUpdate)");
                                            for (Integer i : idsOffers) {
                                                int id_sub = updateShopAndIdSub(context, i);
                                                setUpdateCatSub(context, id_sub);
                                            }
                                        }
                                    }

                                    draws = response.body().getDraws();
                                    if (draws != null) {
                                        Utils.writelogFile(context, "draws != null y draws.size()(Splash, BroadcastUpdate)");
                                        if (draws.size() > 0) {
                                            Utils.writelogFile(context, "draws.size() > 0 y for draws(Splash, BroadcastUpdate)");
                                            for (Draw draw : draws) {
                                                if (draw.getIS_ACTIVE() == 0) {
                                                    Utils.writelogFile(context, "draw.getIS_ACTIVE() == 0 (Splash, BroadcastUpdate)");
                                                    if (isDrawWinner(draw.getID_DRAW_KEY())) {
                                                        Utils.writelogFile(context, "isDrawWinner = true (Splash, BroadcastUpdate)");
                                                        if (draw.getIS_TAKE() == 0) {
                                                            Utils.writelogFile(context, "draw.getIS_TAKE() == 0 (Splash, BroadcastUpdate)");
                                                            if (draw.getIS_LIMITE() == 0) {
                                                                Utils.writelogFile(context, "draw.getIS_LIMITE() == 0(Splash, BroadcastUpdate)");
                                                                draw.setIS_WINNER(1);
                                                                draw.update();
                                                            } else {
                                                                Utils.writelogFile(context, "draw.getIS_LIMITE() == 1(Splash, BroadcastUpdate)");
                                                                deleteDrawAndWinner(draw);
                                                            }
                                                        } else {
                                                            Utils.writelogFile(context, "draw.getIS_TAKE() == 1 (Splash, BroadcastUpdate)");
                                                            deleteDrawWinner(draw.getID_DRAW_KEY());
                                                            draw.delete();
                                                        }
                                                    } else {
                                                        Utils.writelogFile(context, "no winner delete (Splash, BroadcastUpdate)");
                                                        draw.delete();
                                                    }
                                                } else {
                                                    Utils.writelogFile(context, "save draw: " + draw.getID_DRAW_KEY() + "(Splash, BroadcastUpdate)");
                                                    draw.save();
                                                }
                                            }
                                        } else {
                                            Utils.writelogFile(context, "draws.size()= 0 y DeleteDraw(Splash, BroadcastUpdate)");
                                            Delete.table(Draw.class);
                                        }
                                    }

                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4) sin cambios(BroadcastUpdate)");
                                } else {
                                    Utils.writelogFile(context, "responseWS.getSuccess().error: " + responseWS.getMessage() + "(BroadcastUpdate)");
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(BroadcastUpdate)");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultShop> call, Throwable t) {
                        Utils.writelogFile(context, " Call error " + t.getMessage() + "(BroadcastUpdate)");
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, " catch error " + e.getMessage() + "(BroadcastUpdate)");
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(BroadcastUpdate)");
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
        SQLite.delete().from(DrawWinner.class).where(DrawWinner_Table.ID_DRAW_FOREIGN.is(id_draw)).querySingle();
    }

    private void deleteDrawAndWinner(Draw draw) {
        deleteDrawWinner(draw.getID_DRAW_KEY());
        draw.delete();
    }

    private int updateShopAndIdSub(Context context, int id_shop) {
        Utils.writelogFile(context, "updateShopAndIdSub(OfferFragment, BroadcastUpdate)");
        SQLite.update(Shop.class)
                .set(Shop_Table.IS_OFFER_UPDATE.eq(1))
                .where(Shop_Table.ID_SHOP_KEY.is(id_shop))
                .async()
                .execute();
        return SQLite.select(Shop_Table.ID_SUBCATEGORY_FOREIGN).from(Shop.class).querySingle().getID_SUBCATEGORY_FOREIGN();
    }

    private boolean setUpdateCatSub(Context context, int id_sub) {

        Utils.writelogFile(context, "metodo setUpdateCatSub(BroadcastUpdate)");

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

    private void setCounterBadge(Context context) {
        Utils.writelogFile(context, "setCounterBadge(BroadcastUpdate)");
        ShortcutBadger.applyCount(context, Utils.setCounterBadge(context)); //for 1.1.4+
    }
}
