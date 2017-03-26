package com.valdroide.mycitysshopsuser.main.offer.di;

import android.app.Activity;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityInteractor;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityInteractorImpl;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityPresenter;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityPresenterImpl;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityRepository;
import com.valdroide.mycitysshopsuser.main.offer.OfferActivityRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.offer.ui.OfferActivityView;
import com.valdroide.mycitysshopsuser.main.offer.ui.adapters.OfferActivityAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class OfferActivityModule {
    OfferActivityView view;
    Activity activity;

    public OfferActivityModule(OfferActivityView view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    @Provides
    @Singleton
    OfferActivityView provideOfferActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    OfferActivityPresenter provideOfferActivityPresenter(OfferActivityView view, EventBus eventBus, OfferActivityInteractor interactor) {
        return new OfferActivityPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    OfferActivityInteractor provideOfferActivityInteractor(OfferActivityRepository repository) {
        return new OfferActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    OfferActivityRepository provideOfferActivityRepository(EventBus eventBus) {
        return new OfferActivityRepositoryImpl(eventBus);
    }

    @Provides
    @Singleton
    List<Offer> provideOfferList() {
        return new ArrayList<Offer>();
    }

    @Provides
    @Singleton
    OfferActivityAdapter providesOfferActivityAdapter(List<Offer> offerList, Activity activity) {
        return new OfferActivityAdapter(offerList, activity);
    }
}
