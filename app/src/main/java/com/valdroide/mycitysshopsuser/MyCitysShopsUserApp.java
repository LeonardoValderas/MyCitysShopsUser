package com.valdroide.mycitysshopsuser;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.FragmentMain.di.DaggerFragmentMainComponent;
import com.valdroide.mycitysshopsuser.main.FragmentMain.di.FragmentMainComponent;
import com.valdroide.mycitysshopsuser.main.FragmentMain.di.FragmentMainModule;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMainView;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters.OnItemClickListener;
import com.valdroide.mycitysshopsuser.main.navigation.di.DaggerNavigationActivityComponent;
import com.valdroide.mycitysshopsuser.main.navigation.di.NavigationActivityComponent;
import com.valdroide.mycitysshopsuser.main.navigation.di.NavigationActivityModule;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivityView;
import com.valdroide.mycitysshopsuser.main.offer.di.DaggerOfferActivityComponent;
import com.valdroide.mycitysshopsuser.main.offer.di.OfferActivityComponent;
import com.valdroide.mycitysshopsuser.main.offer.di.OfferActivityModule;
import com.valdroide.mycitysshopsuser.main.offer.ui.OfferActivityView;
import com.valdroide.mycitysshopsuser.main.place.di.DaggerPlaceActivityComponent;
import com.valdroide.mycitysshopsuser.main.place.di.PlaceActivityComponent;
import com.valdroide.mycitysshopsuser.main.place.di.PlaceActivityModule;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivityView;
import com.valdroide.mycitysshopsuser.main.splash.di.DaggerSplashActivityComponent;
import com.valdroide.mycitysshopsuser.main.splash.di.SplashActivityComponent;
import com.valdroide.mycitysshopsuser.main.splash.di.SplashActivityModule;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivityView;

public class MyCitysShopsUserApp extends Application {
    private LibsModule libsModule;
    private MyCitysShopsUserAppModule myCitysShopsUserAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
        initDB();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DBTearDown();
    }

    private void DBTearDown() {
        FlowManager.destroy();
    }

    private void initDB() {
        FlowManager.init(this);
    }


    private void initModules() {
        libsModule = new LibsModule();
        myCitysShopsUserAppModule = new MyCitysShopsUserAppModule(this);
    }

    public NavigationActivityComponent getNavigationActivityComponent(Activity activity, Context context, NavigationActivityView view) {
        return DaggerNavigationActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .navigationActivityModule(new NavigationActivityModule(context, view))
                .build();
    }

    public SplashActivityComponent getSplashActivityComponent(SplashActivityView view, Activity activity) {
        return DaggerSplashActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .splashActivityModule(new SplashActivityModule(view, activity))
                .build();
    }
    public PlaceActivityComponent getPlaceActivityComponent(PlaceActivityView view, Activity activity) {
        return DaggerPlaceActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .placeActivityModule(new PlaceActivityModule(view, activity))
                .build();
    }

    public FragmentMainComponent getFragmentMainComponent(FragmentMainView view, Fragment fragment, OnItemClickListener onItemClickListener) {
        return DaggerFragmentMainComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(fragment))
                .fragmentMainModule(new FragmentMainModule(view, fragment, onItemClickListener))
                .build();
    }

    public OfferActivityComponent getOfferActivityComponent(OfferActivityView view, Activity activity) {
        return DaggerOfferActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .offerActivityModule(new OfferActivityModule(view, activity))
                .build();
    }
    /*
        public AccountActivityComponent getAccountActivityComponent(AccountActivityView view, Activity activity) {
        return DaggerAccountActivityComponent
                .builder()
                .myCitysShopsAdmAppModule(myCitysShopsAdmAppModule)
                .libsModule(new LibsModule(activity))
                .accountActivityModule(new AccountActivityModule(view, activity))
                .build();
    }
    */

}
