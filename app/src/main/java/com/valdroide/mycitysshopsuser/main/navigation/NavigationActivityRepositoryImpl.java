package com.valdroide.mycitysshopsuser.main.navigation;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.Category_Table;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory_Table;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

public class NavigationActivityRepositoryImpl implements NavigationActivityRepository {
    private EventBus eventBus;

    public NavigationActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getCategoriesAndSubCategories(Context context) {
        Utils.writelogFile(context, "getCategoriesAndSubCategories(Navigation)");
        try {
            List<Category> categories = SQLite.select().from(Category.class).orderBy(Category_Table.CATEGORY, true).queryList();
            List<SubCategory> subCategories = SQLite.select().from(SubCategory.class).orderBy(SubCategory_Table.ID_CATEGORY_FOREIGN, true).queryList();
            if (categories != null && subCategories != null) {
                Utils.writelogFile(context, "categories != null && subCategories != null y post GETCATEGORIESANDSUBCATEGORIES(Navigation)");
                post(NavigationActivityEvent.GETCATEGORIESANDSUBCATEGORIES, categories, subCategories);
            } else {
                Utils.writelogFile(context, "Base de datos error(Navigation, Repository)");
                post(NavigationActivityEvent.ERROR, context.getString(R.string.error_data_base));
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
            Utils.writelogFile(context, "delete Draw(Navigation, Repository)");
            Delete.table(Draw.class);
            Utils.writelogFile(context, "reset id city shared y post CHANGEPLACE(Navigation, Repository)");
            Utils.resetIdCity(context);
            post(NavigationActivityEvent.CHANGEPLACE);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Navigation, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void setUpdateCategory(Context context, String category) {
        Utils.writelogFile(context, "metodo setUpdateCategory(Navigation, Repository)");
        try {

            SQLite.update(Category.class)
                    .set(Category_Table.IS_UPDATE.eq(0))
                    .where(Category_Table.CATEGORY.is(category))
                    .async()
                    .execute();

            post(NavigationActivityEvent.CATEGORORYSUBCATEGORYUPDATE);
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Navigation, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    @Override
    public void setUpdateSubCategory(Context context, SubCategory subCategory) {
        Utils.writelogFile(context, "metodo setUpdateSubCategory(Navigation, Repository)");
        try {
            if (subCategory != null) {
                if (subCategory.getIS_UPDATE() != 0) {
                    Utils.writelogFile(context, "subCategory != null y update(Navigation, Repository)");
                    subCategory.setIS_UPDATE(0);
                    subCategory.update();
                    Utils.writelogFile(context, "post CATEGORORYSUBCATEGORYUPDATE(Navigation, Repository)");
                    post(NavigationActivityEvent.CATEGORORYSUBCATEGORYUPDATE);
                }
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(Navigation, Repository)");
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }

    private void post(int type, List<Category> categories, List<SubCategory> subCategories) {
        post(type, categories, subCategories, null);
    }

    private void post(int type) {
        post(type, null, null, null);
    }

    private void post(int type, String error) {
        post(type, null, null, error);
    }

    private void post(int type, List<Category> categories, List<SubCategory> subCategories, String error) {
        NavigationActivityEvent event = new NavigationActivityEvent();
        event.setType(type);
        event.setCategories(categories);
        event.setSubCategories(subCategories);
        event.setError(error);
        eventBus.post(event);
    }
}
