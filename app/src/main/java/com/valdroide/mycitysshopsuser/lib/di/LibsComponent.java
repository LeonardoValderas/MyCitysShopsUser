package com.valdroide.mycitysshopsuser.lib.di;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserAppModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {LibsModule.class, MyCitysShopsUserAppModule.class})
public interface LibsComponent {
}
