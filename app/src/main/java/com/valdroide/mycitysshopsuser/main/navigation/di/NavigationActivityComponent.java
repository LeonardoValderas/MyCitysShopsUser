package com.valdroide.mycitysshopsuser.main.navigation.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.navigation.ui.NavigationActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NavigationActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface NavigationActivityComponent {
    void inject(NavigationActivity activity);
}
