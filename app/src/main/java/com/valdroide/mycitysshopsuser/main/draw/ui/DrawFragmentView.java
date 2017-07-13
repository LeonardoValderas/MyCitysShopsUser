package com.valdroide.mycitysshopsuser.main.draw.ui;

import com.valdroide.mycitysshopsuser.entities.shop.Draw;

import java.util.List;

public interface DrawFragmentView {
    void setDraws(List<Draw> draws);
    void onClickToolBar();
    void setError(String error);
    void participationSuccess();
    void withoutChange();
    void setDrawsRefresh();
    void showProgressDialog();
    void hideProgressDialog();
}
