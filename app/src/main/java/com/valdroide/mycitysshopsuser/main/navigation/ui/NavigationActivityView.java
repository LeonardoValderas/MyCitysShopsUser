package com.valdroide.mycitysshopsuser.main.navigation.ui;

import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import java.util.List;

public interface NavigationActivityView {
   void setListCategoriesAndSubCategories(List<Category> categories, List<SubCategory> subCategories);
   void setError(String msg);
   void goToPlace();
}
