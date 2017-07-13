package com.valdroide.mycitysshopsuser.main.FragmentMain.ui;

import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;

import java.util.List;

public interface FragmentMainView {
    void setListShops(List<Shop> shops);
    void setListOffer(List<Offer> offers);
    void followUnFollowSuccess(Shop shop);
    void setError(String mgs);
    void withoutChange();
    void callShops();
    void callMyShops();
    void isUpdate();
    void showProgressDialog();
    void hideProgressDialog();
}
