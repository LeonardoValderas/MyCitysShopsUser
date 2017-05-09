package com.valdroide.mycitysshopsuser.main.legal.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;
import com.valdroide.mycitysshopsuser.lib.di.LibsModule;
import com.valdroide.mycitysshopsuser.main.legal.ui.LegalActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LegalActivityModule.class, LibsModule.class, MyCitysShopsUserAppModule.class})
public interface LegalActivityComponent {
    void inject(LegalActivity activity);
}
