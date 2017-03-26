package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import com.valdroide.mycitysshopsuser.entities.shop.Shop;

public interface OnItemClickListener {
    void onClickFollow(int position, Shop shop);
    void onClickUnFollow(int position, Shop shop);
    void onClickOffer(Shop shop);
    void onClickMap(Shop shop);
    void onClickContact(Shop shop);
}
