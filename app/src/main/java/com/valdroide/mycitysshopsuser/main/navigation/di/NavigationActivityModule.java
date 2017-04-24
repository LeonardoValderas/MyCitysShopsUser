package com.valdroide.mycitysshopsuser.main.navigation.di;

import android.content.Context;

import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.category.Category;
import com.valdroide.mycitysshopsuser.entities.category.SubCategory;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityInteractor;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityInteractorImpl;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityPresenter;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityPresenterImpl;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityRepository;
import com.valdroide.mycitysshopsuser.main.navigation.NavigationActivityRepositoryImpl;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivityView;
import com.valdroide.mycitysshopsuser.main.navigation.ui.adapters.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationActivityModule {
    NavigationActivityView view;
    Context context;

    public NavigationActivityModule(Context context, NavigationActivityView view) {
        this.context = context;
        this.view = view;
    }

    @Provides
    @Singleton
    NavigationActivityView providesNavigationActivityView() {
        return this.view;
    }

    @Provides
    @Singleton
    NavigationActivityPresenter providesNavigationActivityPresenter(EventBus eventBus, NavigationActivityView view, NavigationActivityInteractor listInteractor) {
        return new NavigationActivityPresenterImpl(view, eventBus, listInteractor);
    }

    @Provides
    @Singleton
    NavigationActivityInteractor providesNavigationActivityInteractor(NavigationActivityRepository repository) {
        return new NavigationActivityInteractorImpl(repository);
    }

    @Provides
    @Singleton
    NavigationActivityRepository providesNavigationActivityRepository(EventBus eventBus) {
        return new NavigationActivityRepositoryImpl(eventBus);
    }

    @Provides
    @Singleton
    Map<String, List<SubCategory>> providesExpandableListDetail() {
        return new TreeMap<>();
    }

    @Provides
    @Singleton
    List<String> providesExpandableListTitle() {
        return new ArrayList<>();
    }


    @Provides
    @Singleton
    CustomExpandableListAdapter providesCustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                                                    Map<String, List<SubCategory>> expandableListDetail) {
        return new CustomExpandableListAdapter(context, expandableListTitle, expandableListDetail);
    }
}
