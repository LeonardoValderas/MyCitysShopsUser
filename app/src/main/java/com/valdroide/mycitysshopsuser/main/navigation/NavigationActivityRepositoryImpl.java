package com.valdroide.mycitysshopsuser.main.navigation;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;
import java.util.List;

public class NavigationActivityRepositoryImpl implements NavigationActivityRepository {
    private EventBus eventBus;
    private APIService service;

    public NavigationActivityRepositoryImpl(EventBus eventBus, APIService service) {
        this.eventBus = eventBus;
        this.service = service;
    }

    @Override
    public void getCategoriesAndSubCategories() {
        try {
            List<Category> categories = SQLite.select().from(Category.class).orderBy(new NameAlias("CATEGORY"), true).queryList();
            List<SubCategory> subCategories = SQLite.select().from(SubCategory.class).orderBy(new NameAlias("ID_CATEGORY"), true).queryList();
            if (categories != null && subCategories != null) {
//                Category category = new Category();
//                category.setID_CATEGORY_KEY(1);
//                category.setCATEGORY("Leo");
//                categories.add(category);
//                SubCategory subCategory = new SubCategory();
//                subCategory.setID_SUBCATEGORY_KEY(1);
//                subCategory.setID_CATEGORY(1);
//                subCategory.setSUBCATEGORY("Capo");
//                subCategories.add(subCategory);
                post(NavigationActivityEvent.GETCATEGORIESANDSUBCATEGORIES, categories, subCategories);
            } else
                post(NavigationActivityEvent.ERROR, "Error en la base de datos.");
        } catch (Exception e) {
            post(NavigationActivityEvent.ERROR, e.getMessage());
        }
    }


    public void post(int type) {
        post(type, null, null, null);
    }

    public void post(int type, List<Category> categories, List<SubCategory> subCategories) {
        post(type, categories, subCategories, null);
    }

    public void post(int type, String error) {
        post(type, null, null, error);
    }

    public void post(int type, List<Category> categories, List<SubCategory> subCategories, String error) {
        NavigationActivityEvent event = new NavigationActivityEvent();
        event.setType(type);
        event.setCategories(categories);
        event.setSubCategories(subCategories);
        event.setError(error);
        eventBus.post(event);
    }
}
