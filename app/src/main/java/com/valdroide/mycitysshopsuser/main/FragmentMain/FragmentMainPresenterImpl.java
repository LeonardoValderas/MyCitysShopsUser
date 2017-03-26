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
    public void getListShops(SubCategory subCategory) {
        interactor.getListShops(subCategory);
    }

    @Override
    public void getListOffer(int id_shop) {
        interactor.getListOffer(id_shop);
    }

    @Override
    public void getDateTable() {
        interactor.getDateTable();
    }

    @Override
    public void refreshLayout(Context context, String date, String category, String subcategory, String clothes, String contact) {
        interactor.refreshLayout(context, date, category, subcategory, clothes, contact);
    }

    @Override
    public void onClickFollow(Context context, Shop shop) {
        interactor.onClickFollow(context, shop);
    }

    @Override
    public void onClickUnFollow(Context context, Shop shop) {
        interactor.onClickUnFollow(context, shop);
    }

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
                case FragmentMainEvent.FOLLOW:
                    view.followSuccess(event.getShop());
                    break;
                case FragmentMainEvent.UNFOLLOW:
                    view.unFollowSuccess(event.getShop());
                    break;
                case FragmentMainEvent.WITHOUTCHANGE:
                    view.withoutChange();
                    break;
                case FragmentMainEvent.CALLSHOPS:
                    view.callShops();
                    break;
                case FragmentMainEvent.GETDATETABLE:
                   // view.setDateTable(event.getDateTables());
                    break;
                case FragmentMainEvent.ERROR:
                    view.setError(event.getError());
                    break;
            }
        }
    }
}
