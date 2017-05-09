package com.valdroide.mycitysshopsuser.main.legal;

import android.content.Context;


public class LegalActivityInteractorImpl implements LegalActivityInteractor {
    LegalActivityRepository repository;

    public LegalActivityInteractorImpl(LegalActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getDataLegal(Context context) {
        repository.getDataLegal(context);
    }
}
