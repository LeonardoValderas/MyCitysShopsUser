package com.valdroide.mycitysshopsuser.main.navigation;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;

public interface NavigationActivityPresenter {
    void onCreate();
    void onDestroy();
    void getCategoriesAndSubCategories(Context context);
    void changePlace(Context context);
    void setUpdateCategory(Context context, String category);
    void setUpdateSubCategory(Context context, SubCategory subCategory);
    void onEventMainThread(NavigationActivityEvent event);
}
