package com.valdroide.mycitysshopsuser.main.FragmentMain.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainInteractor;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainInteractorImpl;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainPresenter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainPresenterImpl;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainRepository;
import com.valdroide.mycitysshopsuser.main.FragmentMain.FragmentMainRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMainView;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.FragmentMainAdapter;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class FragmentMainModule {
    FragmentMainView view;
    Fragment fragment;
    OnItemClickListener onItemClickListener;

    public FragmentMainModule(FragmentMainView view, Fragment fragment, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.fragment = fragment;
        this.onItemClickListener = onItemClickListener;
    }

    @Provides
    @Singleton
    FragmentMainView provideEditClothesView() {
        return this.view;
    }

    @Provides
    @Singleton
    FragmentMainPresenter provideFragmentMainPresenter(FragmentMainView view, EventBus eventBus, FragmentMainInteractor interactor) {
        return new FragmentMainPresenterImpl(view, eventBus, interactor);
    }

    @Provides
    @Singleton
    FragmentMainInteractor provideFragmentMainInteractor(FragmentMainRepository repository) {
        return new FragmentMainInteractorImpl(repository);
    }

    @Provides
    @Singleton
    FragmentMainRepository provideFragmentMainRepository(EventBus eventBus, APIService service) {
        return new FragmentMainRepositoryImpl(eventBus, service);
    }

    @Provides
    @Singleton
    List<Shop> provideShopList() {
        return new ArrayList<Shop>();
    }

    @Provides
    @Singleton
    FragmentMainAdapter providesFragmentMainAdapter(List<Shop> shopList, OnItemClickListener onItemClickListener, Fragment fragment) {
        return new FragmentMainAdapter(shopList, onItemClickListener, fragment);
    }

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener() {
        return this.onItemClickListener;
    }

    @Provides
    @Singleton
    APIService provideAPIService () {
        ShopClient client = new ShopClient();
        return client.getAPIService();
    }
}
