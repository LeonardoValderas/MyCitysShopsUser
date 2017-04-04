package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;

public interface FragmentMainPresenter {
    void onCreate();
    void onDestroy();
    void getListShops(Context context, SubCategory subCategory);
    void getListOffer(Context context, int id_shop);
    void onClickFollowOrUnFollow(Context context, Shop shop, boolean isFollow);
    //void onClickUnFollow(Context context, Shop shop);
    void getDateUserCity(Context context);
    void refreshLayout(Context context, DateUserCity dateUserCity);
    void getMyFavoriteShops(Context context);
    void setUpdateOffer(Context context, int position, Shop shop);
    void onEventMainThread(FragmentMainEvent event);
}
