package com.valdroide.mycitysshopsuser.main.navigation;


import android.content.Context;

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
    public void getUrlShop(Context context, int id_shop) {
        repository.getUrlShop(context, id_shop);
    }

}
