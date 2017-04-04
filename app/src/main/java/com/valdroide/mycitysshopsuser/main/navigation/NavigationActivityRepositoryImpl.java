package com.valdroide.mycitysshopsuser.main.navigation;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.entities.shop.Shop_Table;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

public class NavigationActivityRepositoryImpl implements NavigationActivityRepository {
    private EventBus eventBus;
    private APIService service;//ver si lo voy a usar

    public NavigationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getCategoriesAndSubCategories(Context context) {
        Utils.writelogFile(context, "getCategoriesAndSubCategories(Navigation)");
        try {
            List<Category> categories = SQLite.select().from(Category.class).orderBy(new NameAlias("CATEGORY"), true).queryList();
            List<SubCategory> subCategories = SQLite.select().from(SubCategory.class).orderBy(new NameAlias("ID_CATEGORY"), true).queryList();
            if (categories != null && subCategories != null) {
                Utils.writelogFile(context, "categories != null && subCategories != null y post GETCATEGORIESANDSUBCATEGORIES(Navigation)");
                post(NavigationActivityEvent.GETCATEGORIESANDSUBCATEGORIES, categories, subCategories);
            } else {
                Utils.writelogFile(context, " Base de datos error " + Utils.ERROR_DATA_BASE + "(Navigation, Repository)");
                post(NavigationActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
            }
        } catch (Exception e) {
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void changePlace(Context context) {
        Utils.writelogFile(context, "Metodo changePlace (Navigation, Repository)");
        try {
            Utils.writelogFile(context, "delete MyPlace(Navigation, Repository)");
            Delete.table(MyPlace.class);
            Utils.writelogFile(context, "delete Shop(Navigation, Repository)");
            Delete.table(Shop.class);
            Utils.writelogFile(context, "delete Offer(Navigation, Repository)");
            Delete.table(Offer.class);
            Utils.writelogFile(context, "delete DateUserCity(Navigation, Repository)");
            Delete.table(DateUserCity.class);
            Utils.writelogFile(context, "reset id city shared y post CHANGEPLACE(Navigation, Repository)");
            Utils.resetIdCity(context);
            post(NavigationActivityEvent.CHANGEPLACE);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Navigation, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void getUrlShop(Context context, int id_shop) {
        Utils.writelogFile(context, "Metodo getUrlShop (Navigation, Repository)");
        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(Condition.column(new NameAlias("Shop.ID_SHOP_KEY")).is(id_shop));
        try {
          String url = SQLite.select(Shop_Table.URL_LOGO).from(Shop.class).where(conditions).querySingle().getURL_LOGO();
            if (url != null) {
                Utils.writelogFile(context, "url != null y post GETURL(Navigation)");
                post(NavigationActivityEvent.GETURL, url, true);
            } else {
                Utils.writelogFile(context, " Base de datos error " + Utils.ERROR_DATA_BASE + "(Navigation, Repository)");
                post(NavigationActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
            }
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Navigation, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type, List<Category> categories, List<SubCategory> subCategories) {
        post(type, categories, subCategories, null, null);
    }

    public void post(int type) {
        post(type, null, null, null, null);
    }

    public void post(int type, String error) {
        post(type, null, null, null, error);
    }
    public void post(int type, String url, boolean isURL) {
        post(type, null, null, url, null);
    }

    public void post(int type, List<Category> categories, List<SubCategory> subCategories, String url, String error) {
        NavigationActivityEvent event = new NavigationActivityEvent();
        event.setType(type);
        event.setCategories(categories);
        event.setSubCategories(subCategories);
        event.setUrl(url);
        event.setError(error);
        eventBus.post(event);
    }
}
