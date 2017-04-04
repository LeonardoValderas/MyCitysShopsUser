package com.valdroide.mycitysshopsuser.main.support;


import android.content.Context;

public class SupportActivityInteractorImpl implements SupportActivityInteractor {
    private SupportActivityRepository repository;

    public SupportActivityInteractorImpl(SupportActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendEmail(Context context, String comment) {
        repository.sendEmail(context, comment);
    }
}
