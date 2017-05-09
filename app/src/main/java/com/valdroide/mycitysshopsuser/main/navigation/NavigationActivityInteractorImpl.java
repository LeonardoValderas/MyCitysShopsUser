package com.valdroide.mycitysshopsuser.main.navigation;


import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;

public class NavigationActivityInteractorImpl implements NavigationActivityInteractor {
    private NavigationActivityRepository repository;

    public NavigationActivityInteractorImpl(NavigationActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getCategoriesAndSubCategories(Context context) {
        repository.getCategoriesAndSubCategories(context);
    }

    @Override
    public void changePlace(Context context) {
        repository.changePlace(context);
    }

    @Override
    public void setUpdateCategory(Context context, String category) {
        repository.setUpdateCategory(context, category);
    }

    @Override
    public void setUpdateSubCategory(Context context, SubCategory subCategory) {
        repository.setUpdateSubCategory(context, subCategory);
    }
}
