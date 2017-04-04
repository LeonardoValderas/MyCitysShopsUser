package com.valdroide.mycitysshopsuser.main.FragmentMain;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.entities.shop.DateUserCity;
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
    public void getDateUserCity(Context context) {
        repository.getDateUserCity(context);
    }

    @Override
    public void refreshLayout(Context context, DateUserCity dateUserCity) {
        repository.refreshLayout(context, dateUserCity);
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

//    @Override
//    public void onClickUnFollow(Context context, Shop shop) {
//        repository.onClickUnFollow(context, shop);
//    }
}
