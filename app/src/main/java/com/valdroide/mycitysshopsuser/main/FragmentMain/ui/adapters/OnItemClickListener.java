package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import com.valdroide.mycitysshopsuser.entities.shop.Shop;

public interface OnItemClickListener {
    void onClickFollowOrUnFollow(int position, Shop shop, boolean isFollow);
   // void onClickUnFollow(int position, Shop shop);
    void onClickOffer(int position, Shop shop);
    void onClickMap(Shop shop);
    void onClickContact(Shop shop);
}
