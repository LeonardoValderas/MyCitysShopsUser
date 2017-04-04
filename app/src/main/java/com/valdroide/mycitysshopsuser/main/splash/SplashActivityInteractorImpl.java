package com.valdroide.mycitysshopsuser.main.splash;

import android.content.Context;
import android.content.Intent;

import com.valdroide.mycitysshopsuser.entities.shop.Token;

public class SplashActivityInteractorImpl implements SplashActivityInteractor {

    private SplashActivityRepository repository;

    public SplashActivityInteractorImpl(SplashActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateDatePlace(Context context, Intent intent) {
        repository.validateDatePlace(context, intent);
    }

    @Override
    public void validateDateShop(Context context, Intent intent) {
        repository.validateDateShop(context, intent);
    }

    @Override
    public void sendEmail(Context context, String comment) {
        repository.sendEmail(context, comment);
    }

    @Override
    public void getToken(Context context) {
        repository.getToken(context);
    }
}
