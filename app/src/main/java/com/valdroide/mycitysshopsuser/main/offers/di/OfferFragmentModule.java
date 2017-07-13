package com.valdroide.mycitysshopsuser.main.offers.di;

import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentInteractor;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentInteractorImpl;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentPresenter;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentPresenterImpl;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentRepository;
import com.valdroide.mycitysshopsuser.main.offers.OfferFragmentRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragmentView;
import com.valdroide.mycitysshopsuser.main.offers.ui.adapters.OfferFragmentAdapter;
import com.valdroide.mycitysshopsuser.main.offers.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class OfferFragmentModule {
    OfferFragmentView view;
    Fragment fragment;
    OnItemClickListener listener;

    public OfferFragmentModule(OfferFragmentView view, Fragment fragment, OnItemClickListener listener) {
        this.view = view;
        this.fragment = fragment;
        this.listener = listener;
    }

    @Singleton
    @Provides
    OfferFragmentView provideOfferFragmentView() {
        return this.view;
    }

    @Singleton
    @Provides
    OfferFragmentPresenter provideOfferFragmentPresenter(OfferFragmentView view, EventBus eventBus, OfferFragmentInteractor interactor) {
        return new OfferFragmentPresenterImpl(view, eventBus, interactor);
    }

    @Singleton
    @Provides
    OfferFragmentInteractor provideOfferFragmentInteractor(OfferFragmentRepository repository) {
        return new OfferFragmentInteractorImpl(repository);
    }

    @Singleton
    @Provides
    OfferFragmentRepository provideOfferFragmentRepository(APIService service, EventBus eventBus) {
        return new OfferFragmentRepositoryImpl(service, eventBus);
    }

    @Singleton
    @Provides
    OfferFragmentAdapter provideOfferFragmentAdapter(List<Offer> OfferList, OnItemClickListener onItemClickListener, Fragment fragment) {
        return new OfferFragmentAdapter(OfferList, onItemClickListener, fragment);
    }

    @Singleton
    @Provides
    List<Offer> provideListOffer() {
        return new ArrayList<>();
    }

    @Singleton
    @Provides
    APIService provideAPIService() {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }

    @Singleton
    @Provides
    OnItemClickListener provideOnItemClickListener() {
        return listener;
    }
}
