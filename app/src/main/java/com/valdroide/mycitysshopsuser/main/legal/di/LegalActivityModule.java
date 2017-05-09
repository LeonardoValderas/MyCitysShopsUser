package com.valdroide.mycitysshopsuser.main.legal.di;

import android.app.Activity;

import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityInteractor;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityInteractorImpl;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityPresenter;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityPresenterImpl;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityRepository;
import com.valdroide.mycitysshopsuser.main.legal.LegalActivityRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivityView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LegalActivityModule {
    LegalActivityView view;
    Activity activity;

    public LegalActivityModule(LegalActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    LegalActivityView provideLegalActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    LegalActivityPresenter provideLegalActivityPresenter(EventBus eventBus, LegalActivityView view, LegalActivityInteractor interactor) {
        return new LegalActivityPresenterImpl(eventBus, view, interactor);
    }

    @Provides
    @Singleton
    LegalActivityInteractor provideLegalActivityInteractor(LegalActivityRepository repository) {
        return new LegalActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    LegalActivityRepository provedeLegalActivityRepository(EventBus eventBus) {
        return new LegalActivityRepositoryImpl(eventBus);
    }
}
