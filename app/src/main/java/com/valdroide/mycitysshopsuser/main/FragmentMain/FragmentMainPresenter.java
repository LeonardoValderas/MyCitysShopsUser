package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;

public interface FragmentMainPresenter {
    void onCreate();
    void onDestroy();
    void getListShops(SubCategory subCategory);
    void getListOffer(int id_shop);
    void onClickFollow(Context context, Shop shop);
    void onClickUnFollow(Context context, Shop shop);


    void getDateTable();
    void refreshLayout(Context context, String date, String category, String subcategory, String clothes, String contact);

    void onEventMainThread(FragmentMainEvent event);
}
