package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;

public class FragmentMainInteractorImpl implements FragmentMainInteractor {

    private FragmentMainRepository repository;

    public FragmentMainInteractorImpl(FragmentMainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getListShops(Context context, SubCategory subCategory) {
        repository.getListShops(context, subCategory);
    }

    @Override
    public void getListOffer(Context context, int id_shop) {
        repository.getListOffer(context, id_shop);
    }

    @Override
    public void refreshLayout(Context context, boolean isMyShop) {
        repository.refreshLayout(context, isMyShop);
    }

    @Override
    public void getMyFavoriteShops(Context context) {
        repository.getMyFavoriteShops(context);
    }

    @Override
    public void setUpdateOffer(Context context, int position, Shop shop) {
        repository.setUpdateOffer(context, position, shop);
    }

    @Override
    public void onClickFollowOrUnFollow(Context context, Shop shop, boolean isFollow) {
        repository.onClickFollowOrUnFollow(context, shop, isFollow);
    }
}
