package com.valdroide.mycitysshopsuser.main.offer;

import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.offer.events.OfferActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;


public class OfferActivityRepositoryImpl implements OfferActivityRepository {
    private EventBus eventBus;
    private List<Offer> offerList;
 //   private APIService service;
//    private List<ResponseWS> responseWses;
//    private List<Category> categories;
//    private List<SubCategory> subCategories;
//    private ShopFollow shopFollow;

    public OfferActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getListOffer(int id_shop) {

        ConditionGroup conditionGroup = ConditionGroup.clause();
        conditionGroup.and(Condition.column(new NameAlias("Offer.ID_SHOP_FOREIGN")).is(id_shop));
        try {
            offerList = SQLite.select()
                    .from(Offer.class)
                    .where(conditionGroup).queryList();
            if (offerList != null)
                post(OfferActivityEvent.GETLISTOFFER, offerList);
            else
                post(OfferActivityEvent.ERROR, Utils.ERROR_DATA_BASE);
        } catch (Exception e) {
            post(OfferActivityEvent.ERROR, e.getMessage());
        }
    }

    public void post(int type) {
        post(type, null, null);
    }

    public void post(int type, List<Offer> offers) {
        post(type, offers, null);
    }

    public void post(int type, String error) {
        post(type, null, error);
    }

    public void post(int type, List<Offer> offers, String error) {
        OfferActivityEvent event = new OfferActivityEvent();
        event.setType(type);
        event.setOfferList(offers);
        event.setError(error);
        eventBus.post(event);
    }
}
