package com.valdroide.mycitysshopsuser.main.navigation;


import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;

public interface NavigationActivityInteractor {
    void getCategoriesAndSubCategories(Context context);
    void changePlace(Context context);
    void setUpdateCategory(Context context, String category);
    void setUpdateSubCategory(Context context, SubCategory subCategory);
}
