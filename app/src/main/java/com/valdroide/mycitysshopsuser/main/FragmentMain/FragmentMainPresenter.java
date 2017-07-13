package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;

public interface FragmentMainPresenter {
    void onCreate();
    void onDestroy();
    void getListShops(Context context, SubCategory subCategory);
    void getListOffer(Context context, int id_shop);
    void onClickFollowOrUnFollow(Context context, Shop shop, boolean isMyShop, boolean isFollow);
    void refreshLayout(Context context, boolean isMyShop, boolean isFollow);
    void getMyFavoriteShops(Context context);
    void setUpdateOffer(Context context, int position, Shop shop);
    void getShopsSearch(Context context, SubCategory subCategory, String letter);
    void getShopsSearchFavorites(Context context, String letter);
    void onEventMainThread(FragmentMainEvent event);
}
