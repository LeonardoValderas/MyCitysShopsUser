package com.valdroide.mycitysshopsuser.main.navigation.ui.adapters;

import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpandableListDataSource {
    public static Map<String, List<SubCategory>> getData(List<Category> categories, List<SubCategory> subCategories) {
        Map<String, List<SubCategory>> expandableListData = new TreeMap<>();

        for (int i = 0; i < categories.size(); i++) {
            List<SubCategory> subCategoriesAux = new ArrayList<>();
            for (int j = 0; j < subCategories.size(); j++) {
                if (categories.get(i).getID_CATEGORY_KEY() == subCategories.get(j).getID_CATEGORY_FOREIGN())
                    subCategoriesAux.add(subCategories.get(j));
            }
            expandableListData.put(categories.get(i).getCATEGORY(), subCategoriesAux);
        }
        return expandableListData;
    }
}
