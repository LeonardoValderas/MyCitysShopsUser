package com.valdroide.mycitysshopsuser.main.FragmentMain.ui;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;

import java.util.List;

public interface FragmentMainView {
    void setListShops(List<Shop> shops);
    void setListOffer(List<Offer> offers);
    void followSuccess(Shop shop);
    void unFollowSuccess(Shop shop);
    void setError(String mgs);
    void withoutChange();
    void callShops();

   // void setDateTable(List<DateTable> dateTable);
}
