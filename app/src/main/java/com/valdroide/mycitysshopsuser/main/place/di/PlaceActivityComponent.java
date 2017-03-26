package com.valdroide.mycitysshopsuser.main.place.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.place.ui.PlaceActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PlaceActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface PlaceActivityComponent {
    void inject(PlaceActivity activity);
}
