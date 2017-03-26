package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;

public class SplashActivityInteractorImpl implements SplashActivityInteractor {

    private SplashActivityRepository repository;

    public SplashActivityInteractorImpl(SplashActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateDatePlace(Context context) {
        repository.validateDatePlace(context);
    }

    @Override
    public void validateDateShop(Context context) {
        repository.validateDateShop(context);
    }

}
