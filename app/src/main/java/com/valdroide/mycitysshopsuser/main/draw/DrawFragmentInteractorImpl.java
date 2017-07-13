package com.valdroide.mycitysshopsuser.main.draw;

import android.content.Context;

import com.valdroide.mycitysshopsuser.entities.shop.Draw;

public class DrawFragmentInteractorImpl implements DrawFragmentInteractor {
    DrawFragmentRepository repository;

    public DrawFragmentInteractorImpl(DrawFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getDraws(Context context) {
        repository.getDraws(context);
    }

    @Override
    public void participate(Context context, Draw draw, String dni, String name) {
        repository.participate(context, draw, dni, name);
    }

    @Override
    public void refreshLayout(Context context) {
        repository.refreshLayout(context);
    }

    @Override
    public void getDrawSearch(Context context, String letter) {
        repository.getDrawSearch(context, letter);
    }
}
