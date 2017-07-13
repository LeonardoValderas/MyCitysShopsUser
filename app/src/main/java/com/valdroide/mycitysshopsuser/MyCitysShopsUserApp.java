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
import com.valdroide.mycitysshopsuser.main.draw.di.DaggerDrawFragmentComponent;
import com.valdroide.mycitysshopsuser.main.draw.di.DrawFragmentComponent;
import com.valdroide.mycitysshopsuser.main.draw.di.DrawFragmentModule;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragmentView;
import com.valdroide.mycitysshopsuser.main.legal.di.DaggerLegalActivityComponent;
import com.valdroide.mycitysshopsuser.main.legal.di.LegalActivityComponent;
import com.valdroide.mycitysshopsuser.main.legal.di.LegalActivityModule;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivityView;
import com.valdroide.mycitysshopsuser.main.navigation.di.DaggerNavigationActivityComponent;
import com.valdroide.mycitysshopsuser.main.navigation.di.NavigationActivityComponent;
import com.valdroide.mycitysshopsuser.main.navigation.di.NavigationActivityModule;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivityView;
import com.valdroide.mycitysshopsuser.main.offers.di.DaggerOfferFragmentComponent;
import com.valdroide.mycitysshopsuser.main.offers.di.OfferFragmentComponent;
import com.valdroide.mycitysshopsuser.main.offers.di.OfferFragmentModule;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragmentView;
import com.valdroide.mycitysshopsuser.main.place.di.DaggerPlaceActivityComponent;
import com.valdroide.mycitysshopsuser.main.place.di.PlaceActivityComponent;
import com.valdroide.mycitysshopsuser.main.place.di.PlaceActivityModule;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivityView;
import com.valdroide.mycitysshopsuser.main.splash.di.DaggerSplashActivityComponent;
import com.valdroide.mycitysshopsuser.main.splash.di.SplashActivityComponent;
import com.valdroide.mycitysshopsuser.main.splash.di.SplashActivityModule;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivityView;
import com.valdroide.mycitysshopsuser.main.support.di.DaggerSupportActivityComponent;
import com.valdroide.mycitysshopsuser.main.support.di.SupportActivityComponent;
import com.valdroide.mycitysshopsuser.main.support.di.SupportActivityModule;
import com.valdroide.mycitysshopsuser.main.support.ui.SupportActivityView;

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

    public OfferFragmentComponent getOfferFragmentComponent(OfferFragmentView view, Fragment fragment, com.valdroide.mycitysshopsuser.main.offers.ui.adapters.OnItemClickListener onItemClickListener) {
        return DaggerOfferFragmentComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(fragment))
                .offerFragmentModule(new OfferFragmentModule(view, fragment, onItemClickListener))
                .build();
    }

    public DrawFragmentComponent getDrawFragmentComponent(DrawFragmentView view, Fragment fragment, com.valdroide.mycitysshopsuser.main.draw.ui.adapters.OnItemClickListener onItemClickListener) {
        return DaggerDrawFragmentComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(fragment))
                .drawFragmentModule(new DrawFragmentModule(view, fragment, onItemClickListener))
                .build();
    }

    public LegalActivityComponent getLegalActivityComponent(LegalActivityView view, Activity activity) {
        return DaggerLegalActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .legalActivityModule(new LegalActivityModule(view, activity))
                .build();
    }

    public SupportActivityComponent getSupportActivityComponent(SupportActivityView view, Activity activity) {
        return DaggerSupportActivityComponent
                .builder()
                .myCitysShopsUserAppModule(myCitysShopsUserAppModule)
                .libsModule(new LibsModule(activity))
                .supportActivityModule(new SupportActivityModule(view, activity))
                .build();
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
}
