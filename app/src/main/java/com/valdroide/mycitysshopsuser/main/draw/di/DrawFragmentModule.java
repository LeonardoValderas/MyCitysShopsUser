package com.valdroide.mycitysshopsuser.main.draw.di;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentInteractor;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentInteractorImpl;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentPresenter;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentPresenterImpl;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentRepository;
import com.valdroide.mycitysshopsuser.main.draw.DrawFragmentRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragmentView;
import com.valdroide.mycitysshopsuser.main.draw.ui.adapters.DrawFragmentAdapter;
import com.valdroide.mycitysshopsuser.main.draw.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DrawFragmentModule {
    DrawFragmentView view;
    Fragment fragment;
    OnItemClickListener listener;

    public DrawFragmentModule(DrawFragmentView view, Fragment fragment, OnItemClickListener listener) {
        this.view = view;
        this.fragment = fragment;
        this.listener = listener;
    }

    @Singleton
    @Provides
    DrawFragmentView provideDrawFragmentView() {
        return this.view;
    }

    @Singleton
    @Provides
    DrawFragmentPresenter provideDrawFragmentPresenter(DrawFragmentView view, EventBus eventBus, DrawFragmentInteractor interactor) {
        return new DrawFragmentPresenterImpl(view, eventBus, interactor);
    }

    @Singleton
    @Provides
    DrawFragmentInteractor provideDrawFragmentInteractor(DrawFragmentRepository repository) {
        return new DrawFragmentInteractorImpl(repository);
    }

    @Singleton
    @Provides
    DrawFragmentRepository provideDrawFragmentRepository(APIService service, EventBus eventBus) {
        return new DrawFragmentRepositoryImpl(service, eventBus);
    }

    @Singleton
    @Provides
    DrawFragmentAdapter provideDrawFragmentAdapter(List<Draw> drawList, OnItemClickListener onItemClickListener, Fragment fragment) {
        return new DrawFragmentAdapter(drawList, onItemClickListener, fragment);
    }

    @Singleton
    @Provides
    List<Draw> provideListDraw() {
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
