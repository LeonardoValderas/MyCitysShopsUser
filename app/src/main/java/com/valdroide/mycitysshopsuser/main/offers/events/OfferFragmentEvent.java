package com.valdroide.mycitysshopsuser.main.offers.events;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;

import java.util.List;

public class OfferFragmentEvent {
    private int type;
    private String error;
    private List<Offer> offerList;
    public static final int OFFERS = 0;
    public static final int ERROR = 1;
    public static final int WITHOUTCHANGE = 2;
    public static final int GETOFFERSREFRESH = 3;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }
}
