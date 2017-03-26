package com.valdroide.mycitysshopsuser.main.offer.ui;

import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import java.util.List;

public interface OfferActivityView {
    void setListOffer(List<Offer> offers);
    void setError(String mgs);
}
