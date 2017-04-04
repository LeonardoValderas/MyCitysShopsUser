package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;

public interface FragmentMainRepository {
    void getListShops(Context context, SubCategory subCategory);
    void getListOffer(Context context, int id_shop);
    void onClickFollowOrUnFollow(Context context, Shop shop, boolean isFollow);
    //void onClickUnFollow(Context context, Shop shop);
    void refreshLayout(Context context, DateUserCity dateUserCity);
    void getDateUserCity(Context context);
    void getMyFavoriteShops(Context context);
    void setUpdateOffer(Context context, int position, Shop shop);
}
