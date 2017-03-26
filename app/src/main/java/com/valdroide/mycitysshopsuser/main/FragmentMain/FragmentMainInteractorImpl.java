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
    public void getListShops(SubCategory subCategory) {
        repository.getListShops(subCategory);
    }

    @Override
    public void getListOffer(int id_shop) {
        repository.getListOffer(id_shop);
    }

    @Override
    public void getDateTable() {
        repository.getDateTable();
    }

    @Override
    public void refreshLayout(Context context, String date, String category, String subcategory, String clothes, String contact) {
        repository.refreshLayout(context, date, category, subcategory, clothes, contact);
    }

    @Override
    public void onClickFollow(Context context, Shop shop) {
        repository.onClickFollow(context, shop);
    }

    @Override
    public void onClickUnFollow(Context context, Shop shop) {
        repository.onClickUnFollow(context, shop);
    }
}
