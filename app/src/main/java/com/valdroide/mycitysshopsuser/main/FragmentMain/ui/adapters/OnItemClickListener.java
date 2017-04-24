package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import android.view.View;

import com.valdroide.mycitysshopsuser.entities.shop.Shop;

public interface OnItemClickListener {
    void onClickFollowOrUnFollow(int position, Shop shop, boolean isFollow);
    void onClickOffer(int position, Shop shop);
    void onClickMap(Shop shop, View v);
    void onClickContact(Shop shop);
}
