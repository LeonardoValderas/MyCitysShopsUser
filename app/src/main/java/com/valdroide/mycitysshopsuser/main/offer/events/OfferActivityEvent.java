package com.valdroide.mycitysshopsuser.main.offer.events;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import java.util.List;

public class OfferActivityEvent {
    private int type;
    public static final int GETLISTOFFER = 0;
    public static final int ERROR = 1;

    private List<Offer> offerList;
    private String error;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
