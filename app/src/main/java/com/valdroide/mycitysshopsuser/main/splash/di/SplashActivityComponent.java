package com.valdroide.mycitysshopsuser.main.splash.di;
import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SplashActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface SplashActivityComponent {
    void inject(SplashActivity activity);
}
