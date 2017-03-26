package com.valdroide.mycitysshopsuser.main.navigation;

import com.valdroide.mycitysshopsuser.main.navigation.events.NavigationActivityEvent;

public interface NavigationActivityPresenter {
    void onCreate();
    void onDestroy();
    void getCategoriesAndSubCategories();
    void onEventMainThread(NavigationActivityEvent event);
}
