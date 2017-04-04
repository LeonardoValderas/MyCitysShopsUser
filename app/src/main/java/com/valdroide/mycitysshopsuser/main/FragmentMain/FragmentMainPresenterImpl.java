package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.FragmentMain.events.FragmentMainEvent;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMainView;

import org.greenrobot.eventbus.Subscribe;

public class FragmentMainPresenterImpl implements FragmentMainPresenter {

    private FragmentMainView view;
    private EventBus eventBus;
    private FragmentMainInteractor interactor;

    public FragmentMainPresenterImpl(FragmentMainView view, EventBus eventBus, FragmentMainInteractor interactor) {
        this.view = view;
        this.eventBus = eventBus;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getListShops(Context context, SubCategory subCategory) {
        interactor.getListShops(context, subCategory);
    }

    @Override
    public void getListOffer(Context context, int id_shop) {
        interactor.getListOffer(context, id_shop);
    }

    @Override
    public void getDateUserCity(Context context) {
        interactor.getDateUserCity(context);
    }

    @Override
    public void refreshLayout(Context context, DateUserCity dateUserCity) {
        interactor.refreshLayout(context, dateUserCity);
    }

    @Override
    public void getMyFavoriteShops(Context context) {
        interactor.getMyFavoriteShops(context);
    }

    @Override
    public void setUpdateOffer(Context context, int position, Shop shop) {
        interactor.setUpdateOffer(context, position, shop);
    }

    @Override
    public void onClickFollowOrUnFollow(Context context, Shop shop, boolean isFollow) {
        interactor.onClickFollowOrUnFollow(context, shop, isFollow);
    }

//    @Override
//    public void onClickUnFollow(Context context, Shop shop) {
//        interactor.onClickUnFollow(context, shop);
//    }

    @Override
    @Subscribe
    public void onEventMainThread(FragmentMainEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case FragmentMainEvent.GETLISTSHOPS:
                    view.setListShops(event.getShopsList());
                    break;
                case FragmentMainEvent.GETLISTOFFER:
                    view.setListOffer(event.getOffers());
                    break;
                case FragmentMainEvent.FOLLOWORUNFOLLOW:
                    view.followUnFollowSuccess(event.getShop());
                    break;
                case FragmentMainEvent.WITHOUTCHANGE:
                    view.withoutChange();
                    break;
                case FragmentMainEvent.CALLSHOPS:
                    view.callShops();
                    break;
                case FragmentMainEvent.GETDATEUSERCITY:
                    view.setDateUserCity(event.getDateUserCity());
                    break;
                case FragmentMainEvent.ISUPDATE:
                    view.isUpdate();
                    break;
                case FragmentMainEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
