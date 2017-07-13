package com.valdroide.mycitysshopsuser.main.navigation.ui.adapters;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;

public interface NavigationManager {
    void showFragmentAction(SubCategory subCategory, boolean isMyShops);
    void showDrawFragment();
    void showOfferFragment();
}

