package com.valdroide.mycitysshopsuser.main.navigation;


import android.content.Context;

public interface NavigationActivityInteractor {
    void getCategoriesAndSubCategories(Context context);
    void changePlace(Context context);
    void getUrlShop(Context context, int id_shop);
}
