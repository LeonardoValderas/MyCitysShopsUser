package com.valdroide.mycitysshopsuser.main.navigation.events;

import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import java.util.List;

public class NavigationActivityEvent {
    private int type;
    public static final int GETCATEGORIESANDSUBCATEGORIES = 0;
    public static final int ERROR = 1;
    private String error;
    private List<Category> categories;
    private List<SubCategory> subCategories;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
