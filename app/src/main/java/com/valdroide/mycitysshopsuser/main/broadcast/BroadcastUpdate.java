package com.valdroide.mycitysshopsuser.main.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.category.CatSubCity;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.response.ResultShop;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

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
    private List<CatSubCity> catSubCities;
    private DateUserCity dateUserCity;

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
            Toast.makeText(context, "alarm started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Utils.writelogFile(context, "onReceive error: " + e.getMessage() + "(BroadcastUpdate)");
            //don't use post because this Broadcast work in background
        }
    }


    public void validateDateShop(final Context context, DateUserCity dateUserCityWS) {
        Utils.writelogFile(context, "Metodo validateDateShop y Se valida conexion a internet(BroadcastUpdate)");
        if (Utils.isNetworkAvailable(context)) {
            try {
                Utils.writelogFile(context, "Call validateDateUser(BroadcastUpdate)");
                Call<ResultShop> validateDateUser = service.validateDateUser(Utils.getIdCity(context), dateUserCityWS.getCATEGORY_DATE(),
                        dateUserCityWS.getSUBCATEGORY_DATE(), dateUserCityWS.getCAT_SUB_CITY_DATE(),
                        dateUserCityWS.getSHOP_DATE(), dateUserCityWS.getOFFER_DATE(), dateUserCityWS.getDATE_USER_CITY());
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
                                            Utils.writelogFile(context, "Delete.Category ok FOR category(BroadcastUpdate)");
                                            for (Category category : categories) {
                                                Utils.writelogFile(context, "save Category " + category.getID_CATEGORY_KEY() + "(BroadcastUpdate)");
                                                category.save();
                                            }
                                        } else {
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
                                            Utils.writelogFile(context, " Delete.SubCategory ok FOR subCategories(BroadcastUpdate)");
                                            for (SubCategory subCategory : subCategories) {
                                                Utils.writelogFile(context, "save subCategory " + subCategory.getID_SUBCATEGORY_KEY() + "(BroadcastUpdate)");
                                                subCategory.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "subCategories.size() == 0 y delete SubCategory(BroadcastUpdate)");
                                            Delete.table(SubCategory.class);
                                        }
                                    }

                                    catSubCities = response.body().getCatSubCities();
                                    if (catSubCities != null) {
                                        Utils.writelogFile(context, "catSubCities != null y catSubCities.size()(BroadcastUpdate)");
                                        if (catSubCities.size() > 0) {
                                            Utils.writelogFile(context, "catSubCities.size() > 0 y Delete.CatSubCity()(BroadcastUpdate)");
                                            Delete.table(CatSubCity.class);
                                            Utils.writelogFile(context, " Delete.CatSubCity ok FOR catSubCities(BroadcastUpdate)");
                                            for (CatSubCity catSubCity : catSubCities) {
                                                Utils.writelogFile(context, "save catSubCity " + catSubCity.getID_CAT_SUB_KEY() + "(BroadcastUpdate)");
                                                catSubCity.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "catSubCities.size() == 0 y delete CatSubCity(BroadcastUpdate)");
                                            Delete.table(CatSubCity.class);
                                        }
                                    }

                                    shops = response.body().getShops();
                                    if (shops != null) {
                                        Utils.writelogFile(context, "shops != null y shops.size()(BroadcastUpdate)");
                                        if (shops.size() > 0) {
                                            Utils.writelogFile(context, "shops.size() > 0 y FOR shops(BroadcastUpdate)");
                                            for (Shop shop : shops) {
                                                Utils.writelogFile(context, "save shops " + shop.getID_SHOP_KEY() + "(BroadcastUpdate)");
                                                shop.setIS_SHOP_UPDATE(1);
                                                shop.save();
                                            }
                                        } else {
                                            Utils.writelogFile(context, "shops.size() == 0 y delete Shop(BroadcastUpdate)");
                                            Delete.table(Shop.class);
                                        }
                                    }

                                    offers = response.body().getOffers();
                                    if (offers != null) {
                                        Utils.writelogFile(context, "offers != null y offers.size()(BroadcastUpdate)");
                                        if (offers.size() > 0) {
                                            Utils.writelogFile(context, "offers.size() > 0 y FOR offers(BroadcastUpdate)");
                                            for (Offer offer : offers) {
                                                Utils.writelogFile(context, "save offer " + offer.getID_OFFER_KEY() + "(BroadcastUpdate)");
                                                offer.save();
                                                Utils.writelogFile(context, " getShop (BroadcastUpdate)");
                                                Shop shop = getShop(offer.getID_SHOP_FOREIGN());
                                                if(shop != null) {
                                                    Utils.writelogFile(context, " shop != null y save (BroadcastUpdate)");
                                                    shop.setIS_OFFER_UPDATE(1);
                                                    shop.save();
                                                }
                                            }
                                        } else {
                                            Utils.writelogFile(context, "offers.size() == 0 y delete Offer(BroadcastUpdate)");
                                            Delete.table(Offer.class);
                                        }
                                    }
                                } else if (responseWS.getSuccess().equals("4")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(4) sin cambios(BroadcastUpdate)");
                                } else {
                                    Utils.writelogFile(context, "responseWS.getSuccess().error: " + responseWS.getMessage() + "(BroadcastUpdate)");
                                }
                            }
                        } else {
                            Utils.writelogFile(context, " Base de datos error " + Utils.ERROR_DATA_BASE + "(BroadcastUpdate)");
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
            Utils.writelogFile(context, " Internet error " + Utils.ERROR_INTERNET + "(BroadcastUpdate)");
        }
    }
    public Shop getShop(int id_shop) {
        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Shop.ID_SHOP_KEY")).is(id_shop));
        return SQLite.select().from(Shop.class).where(conditionGroup).querySingle();
    }
}
