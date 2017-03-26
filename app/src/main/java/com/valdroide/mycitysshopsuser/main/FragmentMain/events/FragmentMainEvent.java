package com.valdroide.mycitysshopsuser.main.FragmentMain.events;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;

import java.util.List;

public class FragmentMainEvent {
    private int type;
    public static final int GETLISTSHOPS = 0;
    public static final int GETLISTOFFER = 1;
    public static final int ERROR = 2;
    public static final int FOLLOW = 3;
    public static final int UNFOLLOW = 4;
    public static final int WITHOUTCHANGE = 5;
    public static final int CALLSHOPS = 6;
    public static final int GETDATETABLE = 7;

    private List<Shop> shopsList;
    private List<Offer> offers;
    private Shop shop;
    //private List<DateTable> dateTables;
    private String error;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Shop> getShopsList() {
        return shopsList;
    }

    public void setShopsList(List<Shop> shopsList) {
        this.shopsList = shopsList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}