package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
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
    public void refreshLayout(Context context, boolean isMyShop, boolean isFollow) {
        interactor.refreshLayout(context, isMyShop, isFollow);
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
    public void getShopsSearch(Context context, SubCategory subCategory, String letter) {
        interactor.getShopsSearch(context, subCategory, letter);
    }

    @Override
    public void getShopsSearchFavorites(Context context, String letter) {
        interactor.getShopsSearchFavorites(context, letter);
    }

    @Override
    public void onClickFollowOrUnFollow(Context context, Shop shop, boolean isMyShop, boolean isFollow) {
        interactor.onClickFollowOrUnFollow(context, shop, isMyShop, isFollow);
    }

    @Override
    @Subscribe
    public void onEventMainThread(FragmentMainEvent event) {
        if (this.view != null) {
            switch (event.getType()) {
                case FragmentMainEvent.GETLISTSHOPS:
                    view.setListShops(event.getShopsList());
                    view.hideProgressDialog();
                    break;
                case FragmentMainEvent.GETLISTOFFER:
                    view.hideProgressDialog();
                    view.setListOffer(event.getOffers());
                    break;
                case FragmentMainEvent.FOLLOWORUNFOLLOW:
                    view.followUnFollowSuccess(event.getShop());
                    view.hideProgressDialog();
                    break;
                case FragmentMainEvent.WITHOUTCHANGE:
                    view.withoutChange();
                    break;
                case FragmentMainEvent.CALLSHOPS:
                    view.callShops();
                    break;
                case FragmentMainEvent.MYSHOPS:
                    view.callMyShops();
                    break;
                case FragmentMainEvent.ISUPDATE:
                    view.isUpdate();
                    view.hideProgressDialog();
                    break;
                case FragmentMainEvent.ERROR:
                    view.hideProgressDialog();
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
